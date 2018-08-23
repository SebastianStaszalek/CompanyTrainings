package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

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
        //EmployeeTO newVersionEmployee = employeeService.findEmployeeById(savedEmployee.getId());
        //then
        assertThat(savedEmployee.getId()).isEqualTo(updatedEmployee.getId());
        assertThat(savedEmployee.getVersion()).isNotEqualTo(updatedEmployee.getVersion());
        //assertThat(savedEmployee.getVersion()).isNotEqualTo(newVersionEmployee.getVersion());
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
}
