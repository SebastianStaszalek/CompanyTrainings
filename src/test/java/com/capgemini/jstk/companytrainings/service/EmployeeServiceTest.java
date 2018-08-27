package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
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
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    TrainingService trainingService;

    @Autowired
    TestTO testTO;


    @Test
    public void shouldAddEmployeeToDatabase() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();

        //when
        EmployeeTO savedEmployee = employeeService.save(employee);

        //then
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(employee.getEmployeePosition()).isEqualTo(savedEmployee.getEmployeePosition());
    }

    @Test
    public void shouldFindEmployeeById() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();

        EmployeeTO savedEmployee = employeeService.save(employee);

        //when
        EmployeeTO employeeToCheck = employeeService.findEmployeeById(savedEmployee.getId());

        //then
        assertThat(savedEmployee.getId()).isEqualTo(employeeToCheck.getId());
        assertThat(employee.getLastName()).isEqualTo(employeeToCheck.getLastName());

    }

    @Test
    public void shouldUpdateEmployee() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();

        EmployeeTO savedEmployee = employeeService.save(employee);

        //when
        savedEmployee.setGrade(Grade.SECOND);
        savedEmployee.setEmployeePosition(EmployeePosition.ACCOUNTANT);

        EmployeeTO updatedEmployee = employeeService.update(savedEmployee);
        EmployeeTO newVersionEmployee = employeeService.findEmployeeById(savedEmployee.getId());
        //then
        assertThat(savedEmployee.getId()).isEqualTo(updatedEmployee.getId());
        assertThat(savedEmployee.getVersion()).isNotEqualTo(newVersionEmployee.getVersion());
    }

    @Test
    public void shouldGetAllEmployees() {
        //given
        List<EmployeeTO> employeesList = employeeService.getAllEmployees();

        //when
        EmployeeTO employee = testTO.createFirstEmployee();
        employeeService.save(employee);

        List<EmployeeTO> listToCheck = employeeService.getAllEmployees();

        //then
        assertThat(listToCheck.size()).isEqualTo(employeesList.size()+1);
    }

    @Test
    public void shouldDeleteEmployee() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        List<EmployeeTO> employeesList = employeeService.getAllEmployees();

        //when
        employeeService.deleteEmployee(savedEmployee);
        EmployeeTO employeeToCheck = employeeService.findEmployeeById(savedEmployee.getId());

        List<EmployeeTO> listToCheck = employeeService.getAllEmployees();

        //then
        assertThat(employeeToCheck).isNull();
        assertThat(employeesList.size()).isNotEqualTo(listToCheck);

    }

    @Test
    public void shouldDeleteEmployeeWithReferencesToTraining() {
        //given
        EmployeeTO student = testTO.createFirstEmployee();
        EmployeeTO savedStudent = employeeService.save(student);

        EmployeeTO couch = testTO.createFirstEmployee();
        EmployeeTO savedCouch = employeeService.save(couch);

        TrainingTO training = testTO.createFirstTraining();
        TrainingTO savedTraining = trainingService.save(training);

        trainingService.addStudentToTraining(savedTraining, savedStudent);
        trainingService.addCoachToTraining(savedTraining, savedCouch);

        //when
        employeeService.deleteEmployee(savedStudent);
        employeeService.deleteEmployee(savedCouch);

        TrainingTO trainingToCheck = trainingService.findTrainingById(savedTraining.getId());

        Set<EmployeeTO> students = trainingService.findAllStudentsByTrainingId(savedTraining.getId());
        Set<EmployeeTO> couches = trainingService.findAllCoachesByTrainingId(savedTraining.getId());

        EmployeeTO studentToCheck = employeeService.findEmployeeById(savedStudent.getId());
        EmployeeTO couchToCheck = employeeService.findEmployeeById(savedCouch.getId());

        //then
        assertThat(trainingToCheck).isNotNull();
        assertThat(students).isNullOrEmpty();
        assertThat(couches).isNullOrEmpty();
        assertThat(studentToCheck).isNull();
        assertThat(couchToCheck).isNull();
    }

    @Test
    public void shouldAddSuperiorToEmployee() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO superior = testTO.createFirstEmployee();

        EmployeeTO savedEmployee = employeeService.save(employee);
        EmployeeTO savedSuperior = employeeService.save(superior);


        //when
        employeeService.addSuperiorToEmployee(savedEmployee, savedSuperior);

        EmployeeTO employeeToCheck = employeeService.findEmployeeById(savedEmployee.getId());

        //then
        assertThat(employeeToCheck.getSuperiorId()).isEqualTo(savedSuperior.getId());
    }

    @Test
    public void shouldFindCountOfTrainingsParticipatedByGivenStudentAndPeriodOfTime() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setEndDate(LocalDate.of(2018, 4, 20));
        training2.setEndDate(LocalDate.of(2018, 5, 13));
        training3.setEndDate(LocalDate.of(2018, 6, 5));

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);

        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);
        trainingService.addStudentToTraining(savedTraining3, savedEmployee);

        //when
        LocalDate from = LocalDate.of(2018, 4, 21);
        LocalDate to = LocalDate.of(2018, 6, 10);

        int count = employeeService.countTrainingsForEmployeeInGivenTimePeriod(savedEmployee.getId(), from, to);

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void shouldCountTotalCostOfEmployeeTrainings() {
        //given
        EmployeeTO employee = testTO.createFirstEmployee();
        EmployeeTO savedEmployee = employeeService.save(employee);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setCostPerStudent(5000);
        training2.setCostPerStudent(1000);
        training3.setCostPerStudent(2000);

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);

        trainingService.addStudentToTraining(savedTraining1, savedEmployee);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee);
        trainingService.addStudentToTraining(savedTraining3, savedEmployee);

        savedTraining1.setStatus(TrainingStatus.FINISHED);
        savedTraining2.setStatus(TrainingStatus.FINISHED);
        savedTraining3.setStatus(TrainingStatus.FINISHED);

        trainingService.update(savedTraining1);
        trainingService.update(savedTraining2);
        trainingService.update(savedTraining3);

        //when
        Integer count = employeeService.countTotalCostOfEmployeeTrainings(savedEmployee.getId());

        //then
        assertThat(count).isEqualTo(8000);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldFindEmployeesWithMaxHoursSpendOnAllFinishedTrainings() {
        //given
        EmployeeTO employee1 = testTO.createFirstEmployee();
        EmployeeTO employee2 = testTO.createFirstEmployee();
        EmployeeTO employee3 = testTO.createFirstEmployee();

        EmployeeTO savedEmployee1 = employeeService.save(employee1);
        EmployeeTO savedEmployee2 = employeeService.save(employee2);
        EmployeeTO savedEmployee3 = employeeService.save(employee3);

        TrainingTO training1 = testTO.createFirstTraining();
        TrainingTO training2 = testTO.createFirstTraining();
        TrainingTO training3 = testTO.createFirstTraining();

        training1.setDuration(5D);
        training2.setDuration(3.5D);
        training3.setDuration(3D);

        TrainingTO savedTraining1 = trainingService.save(training1);
        TrainingTO savedTraining2 = trainingService.save(training2);
        TrainingTO savedTraining3 = trainingService.save(training3);

        trainingService.addStudentToTraining(savedTraining1, savedEmployee1);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee1);

        trainingService.addStudentToTraining(savedTraining1, savedEmployee2);
        trainingService.addStudentToTraining(savedTraining2, savedEmployee2);

        trainingService.addStudentToTraining(savedTraining1, savedEmployee3);
        trainingService.addStudentToTraining(savedTraining3, savedEmployee3);

        savedTraining1.setStatus(TrainingStatus.FINISHED);
        savedTraining2.setStatus(TrainingStatus.FINISHED);
        savedTraining3.setStatus(TrainingStatus.FINISHED);

        trainingService.update(savedTraining1);
        trainingService.update(savedTraining2);
        trainingService.update(savedTraining3);

        //when
        List<EmployeeTO> employeesList = employeeService.findEmployeesWithMaxHoursSpentOnTrainings();

        //then
        assertThat(employeesList.size()).isEqualTo(2);
    }
}
