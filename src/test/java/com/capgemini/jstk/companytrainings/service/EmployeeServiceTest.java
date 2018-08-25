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
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

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
//TODO: pomysl jak lepiej skonstruowac ten test
    @Test
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
