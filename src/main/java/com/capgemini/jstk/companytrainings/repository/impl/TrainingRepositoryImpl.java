package com.capgemini.jstk.companytrainings.repository.impl;

import com.capgemini.jstk.companytrainings.domain.QEmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.QTrainingEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.repository.TrainingRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class TrainingRepositoryImpl implements TrainingRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    //TODO: dlaczego final?
    @Override
    public List<TrainingEntity> findTrainingsByTagsQueryDSL(String tag) {
        final JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        final QTrainingEntity trainingEntity = QTrainingEntity.trainingEntity;

        return query.from(trainingEntity).where(trainingEntity.tags.containsIgnoreCase(tag)).fetch();
    }

    @Override
    public Double findSumOfTrainingHoursByCoachAndYear(Long id, Date date) {
        JPAQuery<Double> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;
        QEmployeeEntity couches = QEmployeeEntity.employeeEntity;

        return query.from(training)
                .select(training.duration.sum())
                .join(training.couches, couches)
                .where(couches.id.eq(id)
                    .and(training.startDate.year().eq(date.getYear())))
                .fetchOne();

    }

//    @Override
//    public Double findSumWithJPQL(Long id, Date date) {
//        TypedQuery<Double> query = em.createQuery(
//             "select sum(tr.duration) from TrainingEntity tr inner join tr.couches c " +
//                     "where c.id = :id and extract tr.startDate = year(:date)", Double.class
//        );
//
//        query.setParameter("id", id);
//        query.setParameter("date", date);
//
//        return query.getSingleResult();
//    }


}
