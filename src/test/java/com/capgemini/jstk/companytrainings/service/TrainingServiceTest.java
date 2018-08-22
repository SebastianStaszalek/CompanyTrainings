package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.BudgetExceededException;
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

    @Test(expected = BudgetExceededException.class)
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

    @Test
    public void shouldAddStudentWithHigherBudgetToTraining() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        employee.setGrade(Grade.FOURTH);
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();
        TrainingTO training4 = testTO.createFirstTraining();

        training1.setCostPerStudent(10000);
        training2.setCostPerStudent(10000);
        training3.setCostPerStudent(10000);
        training4.setCostPerStudent(12000);

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);
        TrainingTO savedTraining4 = trainingService.save(training4);

        //when
        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);
        trainingService.addStudentToTraining(savedTraining3, savedEmployee);
        trainingService.addStudentToTraining(savedTraining4, savedEmployee);

        Set<EmployeeTO> studentsToCheck1 = trainingService.findAllStudentsByTrainingId(savedTraining1.getId());
        Set<EmployeeTO> studentsToCheck2 = trainingService.findAllStudentsByTrainingId(savedTraining1.getId());
        Set<EmployeeTO> studentsToCheck3 = trainingService.findAllStudentsByTrainingId(savedTraining1.getId());
        Set<EmployeeTO> studentsToCheck4 = trainingService.findAllStudentsByTrainingId(savedTraining1.getId());

        //then
        assertThat(studentsToCheck1.size()).isEqualTo(1);
        assertThat(studentsToCheck2.size()).isEqualTo(1);
        assertThat(studentsToCheck3.size()).isEqualTo(1);
        assertThat(studentsToCheck4.size()).isEqualTo(1);

    }

    @Test(expected = BudgetExceededException.class)
    public void shouldThrowExceptionWhenExceedingExtraTrainingsBudget() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        employee.setGrade(Grade.FIFTH);
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();

        training1.setCostPerStudent(10000);
        training2.setCostPerStudent(42000);

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);


        //when
        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);

    }

    @Test(expected =  EmployeeTrainingException.class)
    public void shouldThrowExceptionIfStudentWasAlreadyAddedToTraining() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);


        //when
        trainingService.addStudentToTraining(savedTraining, savedEmployee);
        trainingService.addStudentToTraining(savedTraining, savedEmployee);

    }

    @Test
    public void shouldAddCouchToTrainingAndFindThemByTrainingId() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        Set<EmployeeTO> couches = trainingService.findAllCoachesByTrainingId(savedTraining.getId());

        //when
        trainingService.addCoachToTraining(savedTraining, savedEmployee);

        Set<EmployeeTO> couchesToCheck = trainingService.findAllCoachesByTrainingId(savedTraining.getId());

        //then
        assertThat(couches.size()).isNotEqualTo(couchesToCheck.size());
        assertThat(couchesToCheck.size()).isEqualTo(couches.size()+1);

    }

    @Test(expected = EmployeeTrainingException.class)
    public void shouldThrowExceptionIfEmployeeIsAlreadyDefinedAsCouchInTheSameTraining() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);


        //when
        trainingService.addCoachToTraining(savedTraining, savedEmployee);
        trainingService.addStudentToTraining(savedTraining, savedEmployee);
    }

    @Test(expected = EmployeeTrainingException.class)
    public void shouldThrowExceptionIfCouchIsAlreadyAddedToTraining() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);

        //when
        trainingService.addCoachToTraining(savedTraining, savedEmployee);
        trainingService.addCoachToTraining(savedTraining, savedEmployee);
    }

    @Test(expected = EmployeeTrainingException.class)
    public void shouldThrowExceptionIfCouchIsAlreadyAddedAsStudent() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training = testTO.createFirstTraining();

        TrainingTO savedTraining = trainingService.save(training);

        //when
        trainingService.addStudentToTraining(savedTraining, savedEmployee);
        trainingService.addCoachToTraining(savedTraining, savedEmployee);
    }


}
