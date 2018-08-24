package com.capgemini.jstk.companytrainings.service;


import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;

import java.util.List;
import java.util.Set;

public interface TrainingService {

    TrainingTO findTrainingById(Long id);

    List<TrainingTO> getAllTrainings();

    TrainingTO save(TrainingTO training);

    TrainingTO update(TrainingTO training);

    void deleteTraining(TrainingTO training);

    Set<EmployeeTO> findAllStudentsByTrainingId(Long id);

    Set<EmployeeTO> findAllCoachesByTrainingId(Long id);

    void addStudentToTraining(TrainingTO training, EmployeeTO employee);

    void addCoachToTraining(TrainingTO training, EmployeeTO employee);

    List<TrainingTO> findTrainingsByTag(String tag);

    Double findSumOfTrainingHoursByCoachAndYear(Long id, int year);
}
