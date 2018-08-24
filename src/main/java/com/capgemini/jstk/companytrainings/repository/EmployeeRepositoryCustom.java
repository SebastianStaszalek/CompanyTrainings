package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepositoryCustom {

    List<TrainingEntity> findAllTrainingsByStudentAndTimePeriod(Long studentId, LocalDate from, LocalDate to);

    Integer countCostOfStudentTrainings(Long studentId);

    List<EmployeeEntity> findStudentsWithMaxHoursSpentOnTrainings();

}
