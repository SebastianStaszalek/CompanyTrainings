package com.capgemini.jstk.companytrainings.service;


import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;

import java.util.List;

public interface TrainingService {

    TrainingTO findTrainingById(Long id);

    List<TrainingTO> getAllTrainings();

    TrainingTO save(TrainingTO training);

    TrainingTO update(TrainingTO training);

    void deleteTraining(TrainingTO training);

    void addStudentToTraining(TrainingTO training, EmployeeTO employee);

    void addCoachToTraining(TrainingTO training, EmployeeTO employee);
}
