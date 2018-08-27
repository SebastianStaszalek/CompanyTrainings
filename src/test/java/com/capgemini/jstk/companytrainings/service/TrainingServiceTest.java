package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import com.capgemini.jstk.companytrainings.dto.TrainingCriteriaSearchTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.BudgetExceededException;
import com.capgemini.jstk.companytrainings.exception.EmployeeTrainingException;
import com.capgemini.jstk.companytrainings.exception.TrainingStatusException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
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
    ExternalCouchService externalCouchService;

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
        //update
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

    @Test
    public void shouldAddExternalCouchToTrainingAndFindHimById() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();
        ExternalCouchTO savedCouch = externalCouchService.save(externalCouch);

        Set<ExternalCouchTO> couches = trainingService.findAllExternalCoachesByTrainingId(savedTraining.getId());

        //when
        trainingService.addExternalCouchToTraining(savedTraining, savedCouch);

        Set<ExternalCouchTO> couchesToCheck = trainingService.findAllExternalCoachesByTrainingId(savedTraining.getId());

        //then
        assertThat(couches.size()).isNotEqualTo(couchesToCheck.size());
        assertThat(couchesToCheck.size()).isEqualTo(couches.size()+1);

    }

    @Test
    public void shouldRemoveTrainingReferenceWhenDeletingTraining() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        EmployeeTO student = testTO.createFirstEmployee();
        EmployeeTO savedStudent = employeeService.save(student);

        EmployeeTO couch = testTO.createFirstEmployee();
        EmployeeTO savedCouch = employeeService.save(couch);

        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();
        ExternalCouchTO savedExternalCouch = externalCouchService.save(externalCouch);

        trainingService.addStudentToTraining(savedTraining, savedStudent);
        trainingService.addCoachToTraining(savedTraining, savedCouch);
        trainingService.addExternalCouchToTraining(savedTraining, savedExternalCouch);

        TrainingTO trainingToDelete = trainingService.findTrainingById(savedTraining.getId());

        //when
        trainingService.deleteTraining(trainingToDelete);

        TrainingTO trainingToCheck = trainingService.findTrainingById(savedTraining.getId());

        Set<TrainingTO> studentTrainings = employeeService.getAllEmployeeTrainingsAsStudent(savedStudent.getId());
        Set<TrainingTO> couchTrainings = employeeService.getAllEmployeeTrainingsAsCouch(savedCouch.getId());
        Set<TrainingTO> externalCouchTrainings = externalCouchService.getAllTrainingsByCouchId(savedExternalCouch.getId());

        EmployeeTO studentToCheck = employeeService.findEmployeeById(savedStudent.getId());
        EmployeeTO couchToCheck = employeeService.findEmployeeById(savedCouch.getId());

        //then
        assertThat(studentTrainings).isNullOrEmpty();
        assertThat(couchTrainings).isNullOrEmpty();
        assertThat(externalCouchTrainings).isNullOrEmpty();
        assertThat(studentToCheck).isNotNull();
        assertThat(couchToCheck).isNotNull();
        assertThat(trainingToCheck).isNull();
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

    @Test
    public void shouldFindTrainingsByTag() {
        //update
        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setTags("html, java, js");
        training2.setTags("angular, html, agile");
        training3.setTags("http");

        trainingService.save(training1);
        trainingService.save(training2);
        trainingService.save(training3);

        //when
        String tagToSearch = "html";

        List<TrainingTO> trainingsList = trainingService.findTrainingsByTag(tagToSearch);

        //then
        assertThat(trainingsList.size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldFindSumOfTrainingsHoursByCouchAndYear() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setDuration(3D);
        training2.setDuration(2.5D);
        training3.setDuration(2.5D);

        training1.setStartDate(LocalDate.of(2017, 4, 13));
        training2.setStartDate(LocalDate.of(2018, 10, 13));
        training3.setStartDate(LocalDate.of(2018, 2, 13));

        training1.setEndDate(LocalDate.of(2017, 4, 20));
        training2.setEndDate(LocalDate.of(2018, 10, 20));
        training3.setEndDate(LocalDate.of(2018, 2, 20));

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);

        trainingService.addCoachToTraining(savedTraining1, savedEmployee);
        trainingService.addCoachToTraining(savedTraining2, savedEmployee);
        trainingService.addCoachToTraining(savedTraining3, savedEmployee);

        savedTraining1.setStatus(TrainingStatus.FINISHED);
        savedTraining2.setStatus(TrainingStatus.FINISHED);
        savedTraining3.setStatus(TrainingStatus.FINISHED);

        trainingService.update(savedTraining1);
        trainingService.update(savedTraining2);
        trainingService.update(savedTraining3);

        //when
        Double sum = trainingService.findSumOfTrainingHoursByCoachAndYear(savedEmployee.getId(), 2018);

        //then
        assertThat(sum).isEqualTo(5D);
    }

    @Test()
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldFindTrainingsByMultipleCriteriaWithAllParametersFilled() {
        //given
        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setTitle("Spring boot 5");
        training2.setTitle("Elastic search");
        training3.setTitle("German level 1");

        training1.setTrainingCharacter(TrainingCharacter.INTERNAL);
        training2.setTrainingCharacter(TrainingCharacter.INTERNAL);
        training3.setTrainingCharacter(TrainingCharacter.INTERNAL);

        training1.setStartDate(LocalDate.of(2018, 4, 13));
        training2.setStartDate(LocalDate.of(2018, 5, 13));
        training3.setStartDate(LocalDate.of(2018, 6, 13));

        training1.setEndDate(LocalDate.of(2018, 4, 16));
        training2.setEndDate(LocalDate.of(2018, 5, 16));
        training3.setEndDate(LocalDate.of(2018, 6, 16));

        training1.setCostPerStudent(1000);
        training2.setCostPerStudent(1000);
        training3.setCostPerStudent(1000);

        trainingService.save(training1);
        trainingService.save(training2);
        trainingService.save(training3);

        TrainingCriteriaSearchTO criteria = TrainingCriteriaSearchTO.builder()
                .title("Spring boot 5")
                .trainingCharacter(TrainingCharacter.INTERNAL)
                .date(LocalDate.of(2018, 4, 14))
                .costFrom(500)
                .costTo(1200)
                .build();

        //when
        List<TrainingTO> trainingsList = trainingService.findTrainingsByMultipleCriteria(criteria);
        TrainingTO foundTraining = trainingsList.get(0);

        //then
        assertThat(trainingsList.size()).isEqualTo(1);
        assertThat(foundTraining.getTitle()).isEqualToIgnoringCase(criteria.getTitle());
    }

    @Test()
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldFindTrainingsBySearchCriteriaWithOnlyOneCostParameter() {
        //given
        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setCostPerStudent(1000);
        training2.setCostPerStudent(5000);
        training3.setCostPerStudent(2000);

        trainingService.save(training1);
        trainingService.save(training2);
        trainingService.save(training3);

        TrainingCriteriaSearchTO criteria = TrainingCriteriaSearchTO.builder()
                .costFrom(2000)
                .build();

        //when
        List<TrainingTO> trainingsList = trainingService.findTrainingsByMultipleCriteria(criteria);

        //then
        assertThat(trainingsList.size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldFindTrainingsWithTheLargestNumberOfEditionAccordingToTitle() {
        //given
        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();
        TrainingTO training4 = testTO.createFirstTraining();
        TrainingTO training5 = testTO.createFirstTraining();

        training1.setTitle("Spring boot 5");
        training2.setTitle("Spring boot 5");
        training3.setTitle("German level 1");
        training4.setTitle("German level 1");
        training5.setTitle("Building Android Apps");

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);
        TrainingTO savedTraining4 = trainingService.save(training4);
        TrainingTO savedTraining5 = trainingService.save(training5);

        savedTraining1.setStatus(TrainingStatus.FINISHED);
        savedTraining2.setStatus(TrainingStatus.FINISHED);
        savedTraining3.setStatus(TrainingStatus.FINISHED);
        savedTraining4.setStatus(TrainingStatus.FINISHED);
        savedTraining5.setStatus(TrainingStatus.FINISHED);

        trainingService.update(savedTraining1);
        trainingService.update(savedTraining2);
        trainingService.update(savedTraining3);
        trainingService.update(savedTraining4);
        trainingService.update(savedTraining5);

        //when
        List<TrainingTO> trainingsList = trainingService.findTrainingsWithLargestNumberOfEditions();

        //then
        assertThat(trainingsList.size()).isEqualTo(4);
    }

    @Test(expected = TrainingStatusException.class)
    public void shouldThrowStatusExceptionWhenTryToAddStudentToCancelledTraining() {
        //given
        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        savedTraining.setStatus(TrainingStatus.FINISHED);
        trainingService.update(savedTraining);

        //when
        trainingService.addStudentToTraining(savedTraining, savedEmployee);

    }

}
