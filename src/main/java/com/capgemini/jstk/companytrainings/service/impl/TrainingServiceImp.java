package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.BudgetExceededException;
import com.capgemini.jstk.companytrainings.exception.EmployeeTrainingException;
import com.capgemini.jstk.companytrainings.exception.message.Message;
import com.capgemini.jstk.companytrainings.mapper.EmployeeMapper;
import com.capgemini.jstk.companytrainings.mapper.TrainingMapper;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepository;
import com.capgemini.jstk.companytrainings.repository.TrainingRepository;
import com.capgemini.jstk.companytrainings.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: jak testowac relacje skoro nie mapujemy wszystkich id?
//TODO: usun sygnatury metod z repositories

@Service
@Transactional
public class TrainingServiceImp implements TrainingService {

    private static final int ALLOWED_NUMBER_OF_TRAININGS = 3;
    private static final int BUDGET_PER_STUDENT = 15000;
    private static final int BUDGET_FOR_STUDENT_WITH_HIGHER_GRADE = 50000;


    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    TrainingMapper trainingMapper;
    TrainingRepository trainingRepository;


    @Autowired
    public TrainingServiceImp(EmployeeRepository employeeRepository, TrainingMapper trainingMapper,
                              TrainingRepository trainingRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public TrainingTO findTrainingById(Long id) {
        return trainingMapper.map(trainingRepository.findOne(id));
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
        TrainingEntity trainingEntity = trainingRepository.findOne(training.getId());

        if(!training.getVersion().equals(trainingEntity.getVersion())) {
            throw new OptimisticLockException();
        }

        TrainingEntity trainingToUpdate = trainingMapper.map(training, trainingEntity);

        return trainingMapper.map(trainingToUpdate);
        //return trainingMapper.map(trainingRepository.save(trainingToUpdate));
    }

    @Override
    public void deleteTraining(TrainingTO training) {
        trainingRepository.delete(training.getId());
    }

    @Override
    public Set<EmployeeTO> findAllStudentsByTrainingId(Long id) {
        TrainingEntity trainingEntity = trainingRepository.findOne(id);
        return employeeMapper.map2TOSet(trainingEntity.getStudents());
    }

    @Override
    public Set<EmployeeTO> findAllCoachesByTrainingId(Long id) {
        TrainingEntity trainingEntity = trainingRepository.findOne(id);
        return employeeMapper.map2TOSet(trainingEntity.getCouches());
    }

    //TODO: podmienic na nowszy typ daty!?
    @Override
    public void addStudentToTraining(TrainingTO training, EmployeeTO employee) {
        TrainingEntity trainingEntity = trainingRepository.findOne(training.getId());
        EmployeeEntity employeeEntity = employeeRepository.findOne(employee.getId());

        Set<EmployeeEntity> students = trainingEntity.getStudents();
        students.stream()
                .filter(student -> student.getId().equals(employeeEntity.getId()))
                .findAny()
                .ifPresent(student -> {throw new EmployeeTrainingException(Message.STUDENT_ALREADY_EXISTS);});

        Set<EmployeeEntity> coaches = trainingEntity.getCouches();
        boolean exists = coaches.stream()
                .anyMatch(coach -> coach.getId().equals(employeeEntity.getId()));
        if(exists) {
            throw new EmployeeTrainingException(Message.STUDENT_ALREADY_ADDED_AS_COUCH);
        }

        Set<TrainingEntity> employeeTrainings = employeeEntity.getTrainingsAsStudent();
        List<TrainingEntity> trainingsInTheYear = employeeTrainings.stream()
                .filter(time -> time.getStartDate().getYear() == trainingEntity.getStartDate().getYear())
                .collect(Collectors.toList());

        int costOfCompletedTrainings = 0;
        for (TrainingEntity tr : trainingsInTheYear) {
            costOfCompletedTrainings += tr.getCostPerStudent();
        }

        boolean higherGrade = false;

        if(employeeEntity.getGrade() == Grade.FOURTH || employeeEntity.getGrade() == Grade.FIFTH) {
            higherGrade = true;
            if (costOfCompletedTrainings + training.getCostPerStudent() > BUDGET_FOR_STUDENT_WITH_HIGHER_GRADE ) {
                throw new BudgetExceededException(Message.EXTRA_BUDGET_EXCEEDED);
            }
        } else if(costOfCompletedTrainings + training.getCostPerStudent() > BUDGET_PER_STUDENT) {
            throw new BudgetExceededException(Message.BUDGET_EXCEEDED);
        }

        if(!higherGrade) {
            if (trainingsInTheYear.size() >= ALLOWED_NUMBER_OF_TRAININGS) {
                throw new EmployeeTrainingException(Message.TRAININGS_EXCEEDED);
            }
        }

    trainingEntity.addStudent(employeeEntity);
    }

    @Override
    public void addCoachToTraining(TrainingTO training, EmployeeTO employee) {
        TrainingEntity trainingEntity = trainingRepository.findOne(training.getId());
        EmployeeEntity employeeEntity = employeeRepository.findOne(employee.getId());

        Set<EmployeeEntity> students = trainingEntity.getStudents();
        students.stream()
                .filter(student -> student.getId().equals(employeeEntity.getId()))
                .findAny()
                .ifPresent(student -> {throw new EmployeeTrainingException(Message.COUCH_ALREADY_ADDED_AS_STUDENT);});

        Set<EmployeeEntity> coaches = trainingEntity.getCouches();
        coaches.stream()
                .filter(coach -> coach.getId().equals(employeeEntity.getId()))
                .findAny()
                .ifPresent(student -> {throw new EmployeeTrainingException(Message.COUCH_ALREADY_EXISTS);});

        trainingEntity.addCouch(employeeEntity);
    }

    @Override
    public List<TrainingTO> findTrainingsByTag(String tag) {
        List<TrainingEntity> trainings = trainingRepository.findTrainingsByTagsQueryDSL(tag);
        return trainingMapper.map2TO(trainings);
    }

    @Override
    public Double findSumOfTrainingHoursByCoachAndYear(Long id, Date date) {
        return trainingRepository.findSumOfTrainingHoursByCoachAndYear(id, date);
    }
}
