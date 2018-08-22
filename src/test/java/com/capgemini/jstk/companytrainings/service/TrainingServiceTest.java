package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.EmployeeTrainingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class TrainingServiceTest {

    @Autowired
    TrainingService trainingService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    TestTO testTO;


    @Test
    public void shouldAddTrainingToDatabase() {
        //given
        TrainingTO training = testTO.createFirstTraining();

        //when
        TrainingTO savedTraining = trainingService.save(training);

        //then
        assertThat(savedTraining.getId()).isNotNull();
        assertThat(training.getCostPerStudent()).isEqualTo(savedTraining.getCostPerStudent());
    }

    @Test
    public void shouldFindTrainingById() {
        //given
        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);

        //when
        TrainingTO trainingToCheck = trainingService.findTrainingById(savedTraining.getId());

        //then
        assertThat(savedTraining.getId()).isEqualTo(trainingToCheck.getId());
        assertThat(training.getTrainingCharacter()).isEqualTo(trainingToCheck.getTrainingCharacter());
    }

    @Test
    public void shouldUpdateTraining() {
        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);

        //when
        savedTraining.setTrainingType(TrainingType.MANAGEMENT);
        savedTraining.setCostPerStudent(500);

        TrainingTO updatedTraining = trainingService.update(savedTraining);

        //then
        assertThat(savedTraining.getId()).isEqualTo(updatedTraining.getId());
        assertThat(updatedTraining.getCostPerStudent()).isEqualTo(500);
        assertThat(updatedTraining.getTrainingType()).isEqualTo(TrainingType.MANAGEMENT);
    }

    @Test
    public void shouldGetAllTrainings() {
        //given
        List<TrainingTO> trainingsList = trainingService.getAllTrainings();

        //when
        TrainingTO training = testTO.createFirstTraining();
        trainingService.save(training);

        List<TrainingTO> listToCheck = trainingService.getAllTrainings();

        //then
        assertThat(listToCheck.size()).isEqualTo(trainingsList.size() + 1);
    }

    @Test
    public void shouldDeleteTraining() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        List<TrainingTO> trainingsList = trainingService.getAllTrainings();

        //when
        trainingService.deleteTraining(savedTraining);
        TrainingTO trainingToCheck = trainingService.findTrainingById(savedTraining.getId());

        List<TrainingTO> listToCheck = trainingService.getAllTrainings();

        //then
        assertThat(trainingToCheck).isNull();
        assertThat(listToCheck.size()).isNotEqualTo(trainingsList);
    }

    //TODO: dodaj jeszcze sprawdzanie czy dodaje traning do listy treningow pracownika
    @Test
    public void shouldAddStudentToTrainingAndFindThemByTrainingId() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        Set<EmployeeTO> students = trainingService.findAllStudentsByTrainingId(savedTraining.getId());

        //when
        trainingService.addStudentToTraining(savedTraining, savedEmployee);

        Set<EmployeeTO> studentsToCheck = trainingService.findAllStudentsByTrainingId(savedTraining.getId());

        //then
        assertThat(students.size()).isNotEqualTo(studentsToCheck.size());
        assertThat(studentsToCheck.size()).isEqualTo(students.size()+1);

    }

    @Test(expected = EmployeeTrainingException.class)
    public void shouldThrowEmployeeTrainingExceptionWhenAddingMoreThan3TrainingsPerYear() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();
        TrainingTO training4 = testTO.createFirstTraining();

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);
        TrainingTO savedTraining4 = trainingService.save(training4);

        //when
        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);
        trainingService.addStudentToTraining(savedTraining3, savedEmployee);
        trainingService.addStudentToTraining(savedTraining4, savedEmployee);
    }

    @Test(expected = EmployeeTrainingException.class)
    public void shouldThrowEmployeeTrainingExceptionWhenExceedingTrainingsBudget() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();

        training1.setCostPerStudent(10000);
        training2.setCostPerStudent(6000);

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);


        //when
        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);

    }
}
