package com.capgemini.jstk.companytrainings.repository.impl;

import com.capgemini.jstk.companytrainings.domain.QEmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.QTrainingEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.dto.TrainingCriteriaSearchTO;
import com.capgemini.jstk.companytrainings.repository.TrainingRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

@Repository
public class TrainingRepositoryImpl implements TrainingRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TrainingEntity> findTrainingsByMultipleCriteria(TrainingCriteriaSearchTO searchCriteria) {
        JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;

        BooleanBuilder condition = new BooleanBuilder();

        if(searchCriteria.getTitle() != null) {
            condition.and(training.title.equalsIgnoreCase(searchCriteria.getTitle()));
        }

        if(searchCriteria.getTrainingCharacter() != null) {
            condition.and(training.trainingCharacter.eq(searchCriteria.getTrainingCharacter()));
        }

        if(searchCriteria.getDate() != null) {
            condition.and(training.startDate.before(searchCriteria.getDate())
                        .and(training.endDate.after(searchCriteria.getDate())));
        }

        if(searchCriteria.getCostFrom() != null && searchCriteria.getCostTo() != null) {
            condition.and(training.costPerStudent
                    .between(searchCriteria.getCostFrom(), searchCriteria.getCostTo()));
        } else if(searchCriteria.getCostFrom() != null && searchCriteria.getCostTo() == null) {
            condition.and(training.costPerStudent.goe(searchCriteria.getCostFrom()));
        } else if (searchCriteria.getCostFrom() == null && searchCriteria.getCostTo() != null) {
            condition.and(training.costPerStudent.loe(searchCriteria.getCostTo()));
        }

        return query.select(training)
                .from(training)
                .where(condition)
                .fetch();
    }

    //TODO: dlaczego final?
    @Override
    public List<TrainingEntity> findTrainingsByTagsQueryDSL(String tag) {
        final JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        final QTrainingEntity training = QTrainingEntity.trainingEntity;

        return query.from(training).where(training.tags.containsIgnoreCase(tag)).fetch();
    }

    @Override
    public Double findSumOfTrainingHoursByCoachAndYear(Long id, int year) {
        JPAQuery<Double> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;
        QEmployeeEntity couches = QEmployeeEntity.employeeEntity;

        return query.from(training)
                .select(training.duration.sum())
                .join(training.couches, couches)
                .where(couches.id.eq(id)
                    .and(training.startDate.year().eq(year))
                    .and(training.status.eq(TrainingStatus.FINISHED)))
                .fetchOne();


    }

    @Override
    public List<TrainingEntity> findTrainingsWithLargestNumberOfEditions() {
        JPAQuery<Double> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;

        return null;
    }

    @Override
    public Double findSumWithJPQL(Long id, Date date) {
        TypedQuery<Double> query = em.createQuery(
             "select sum(tr.duration) from TrainingEntity tr inner join tr.couches c " +
                     "where c.id = :id", Double.class
        );

        query.setParameter("id", id);

        return query.getSingleResult();
    }


}
