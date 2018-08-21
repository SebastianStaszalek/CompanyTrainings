package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import org.springframework.stereotype.Component;

@Component
public class TestTO {

    public EmployeeTO createFirstEmployee() {
        return EmployeeTO.builder()
                .firstName("Jacek")
                .lastName("Sobkowiak")
                .employeePosition(EmployeePosition.MANAGER)
                .grade(Grade.FIRST)
                .build();
    }
}
