package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.EmployeeTrainingException;
import com.capgemini.jstk.companytrainings.exception.message.Message;
import com.capgemini.jstk.companytrainings.mapper.TrainingMapper;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepository;
import com.capgemini.jstk.companytrainings.repository.TrainingRepository;
import com.capgemini.jstk.companytrainings.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//TODO: null na get?
//TODO: jak testowac relacje skoro nie mapujemy wszystkich id?

@Service
@Transactional
public class TrainingServiceImp implements TrainingService {

    private static final int ALLOWED_NUMBER_OF_TRAININGS = 3;

    EmployeeRepository employeeRepository;

    TrainingMapper trainingMapper;
    TrainingRepository trainingRepository;


    @Autowired
    public TrainingServiceImp(EmployeeRepository employeeRepository, TrainingMapper trainingMapper,
                              TrainingRepository trainingRepository) {
        this.employeeRepository = employeeRepository;
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public TrainingTO findTrainingById(Long id) {
        return trainingMapper.map(trainingRepository.findById(id));
    }

    @Override
    public List<TrainingTO> getAllTrainings() {
        return trainingMapper.map2TO(trainingRepository.findAll());
    }

    @Override
    public TrainingTO save(TrainingTO training) {
        TrainingEntity trainingEntity = trainingMapper.map(training);

        return trainingMapper.map(trainingRepository.save(trainingEntity));
    }

    @Override
    public TrainingTO update(TrainingTO training) {
        TrainingEntity trainingEntity = trainingRepository.findById(training.getId());

        TrainingEntity trainingToUpdate = trainingMapper.map(training, trainingEntity);

        return trainingMapper.map(trainingRepository.save(trainingToUpdate));
    }

    @Override
    public void deleteTraining(TrainingTO training) {
        trainingRepository.deleteById(training.getId());
    }

    @Override
    public void addStudentToTraining(TrainingTO training, EmployeeTO employee) {
        TrainingEntity trainingEntity = trainingRepository.findById(training.getId());
        EmployeeEntity employeeEntity = employeeRepository.findById(employee.getId());

        Set<TrainingEntity> completedTrainings = employeeEntity.getTrainingsAsStudent();
        List<TrainingEntity> trainingsInTheYear = completedTrainings.stream()
                .filter(time -> time.getStartDate().getYear() == trainingEntity.getStartDate().getYear())
                .collect(Collectors.toList());

        if(trainingsInTheYear.size() >= ALLOWED_NUMBER_OF_TRAININGS) {
            throw new EmployeeTrainingException(Message.TRAININGS_EXCEEDED);
        }


    trainingEntity.addStudent(employeeEntity);
    }

    @Override
    public void addCoachToTraining(TrainingTO training, EmployeeTO employee) {

    }
}
