package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=mysql")
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
        EmployeeTO savedEmployee = employeeService.saveOrUpdate(employee);

        //then
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(employee.getEmployeePosition()).isEqualTo(savedEmployee.getEmployeePosition());
    }
}
