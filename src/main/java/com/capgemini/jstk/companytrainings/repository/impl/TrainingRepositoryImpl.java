package com.capgemini.jstk.companytrainings.repository.impl;

import com.capgemini.jstk.companytrainings.domain.QEmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.QTrainingEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.dto.TrainingCriteriaSearchTO;
import com.capgemini.jstk.companytrainings.repository.TrainingRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

        if (searchCriteria.getTitle() != null) {
            condition.and(training.title.equalsIgnoreCase(searchCriteria.getTitle()));
        }

        if (searchCriteria.getTrainingCharacter() != null) {
            condition.and(training.trainingCharacter.eq(searchCriteria.getTrainingCharacter()));
        }

        if (searchCriteria.getDate() != null) {
            condition.and(training.startDate.before(searchCriteria.getDate())
                    .and(training.endDate.after(searchCriteria.getDate())));
        }

        if (searchCriteria.getCostFrom() != null && searchCriteria.getCostTo() != null) {
            condition.and(training.costPerStudent
                    .between(searchCriteria.getCostFrom(), searchCriteria.getCostTo()));

        } else if (searchCriteria.getCostFrom() != null && searchCriteria.getCostTo() == null) {
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

        return query
                .select(training.duration.sum())
                .from(training)
                .join(training.couches, couches)
                .where(couches.id.eq(id)
                        .and(training.startDate.year().eq(year))
                        .and(training.status.eq(TrainingStatus.FINISHED)))
                .fetchOne();


    }

    @Override
    public List<TrainingEntity> findTrainingsWithLargestNumberOfEditions() {
        JPAQuery<Long> query = new JPAQuery<>(em);
        JPAQuery<TrainingEntity> query2 = new JPAQuery<>(em);
        JPAQuery<TrainingEntity> query3 = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;

        Long maxCounter = query
                .select(training.count())
                .from(training)
                .where(training.status.eq(TrainingStatus.FINISHED))
                .groupBy(training.title)
                .orderBy(training.count().desc())
                .fetchFirst();

        List<String> titles = query2
                .select(training.title)
                .from(training)
                .where(training.status.eq(TrainingStatus.FINISHED))
                .groupBy(training.title)
                .having(training.count().eq(maxCounter))
                .fetch();

        return query3
                .select(training)
                .from(training)
                .where(training.status.eq(TrainingStatus.FINISHED)
                    .and(training.title.in(titles)))
                .fetch();

    }

}


