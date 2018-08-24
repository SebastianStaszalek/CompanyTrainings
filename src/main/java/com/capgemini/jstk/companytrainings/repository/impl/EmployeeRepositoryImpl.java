package com.capgemini.jstk.companytrainings.repository.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.QEmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.QTrainingEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepositoryCustom;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TrainingEntity> findAllTrainingsByStudentAndTimePeriod(Long id, LocalDate from, LocalDate to) {
        JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;
        QEmployeeEntity students = QEmployeeEntity.employeeEntity;

        return query.from(training)
                .select(training)
                .join(training.students, students)
                .where(students.id.eq(id)
                        .and(training.endDate.between(from, to)))
                .fetch();
    }

    @Override
    public Integer countCostOfStudentTrainings(Long studentId) {
        JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;
        QEmployeeEntity students = QEmployeeEntity.employeeEntity;

        return query.from(training)
                .select(training.costPerStudent.sum())
                .innerJoin(training.students, students)
                .where(students.id.eq(studentId)
                    .and(training.status.eq(TrainingStatus.FINISHED)))
                .fetchOne();


    }

    @Override
    public List<EmployeeEntity> findStudentsWithMaxHoursSpentOnTrainings() {
        JPAQuery<TrainingEntity> query = new JPAQuery<>(em);
        QTrainingEntity training = QTrainingEntity.trainingEntity;
        QEmployeeEntity students = QEmployeeEntity.employeeEntity;

        return query.from(students)
                .select(students)
                .join(students.trainingsAsStudent, training)
                .where((training.duration.sum().eq(
                        JPAExpressions.select(training.duration.sum().max())
                        .from(training).join(training.students, students)
                        .groupBy(students.id)))
                    .and(training.status.eq(TrainingStatus.FINISHED)))
                .groupBy(students.id)
                .fetch();
    }
}
