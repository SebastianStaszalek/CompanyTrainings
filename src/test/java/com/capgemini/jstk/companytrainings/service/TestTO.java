package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import org.springframework.stereotype.Component;

import java.sql.Date;

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

    public TrainingTO createFirstTraining() {
        return TrainingTO.builder()
                .title("Effective speaking")
                .trainingType(TrainingType.SOFT)
                .duration(2.5)
                .trainingCharacter(TrainingCharacter.INTERNAL)
                .startDate(Date.valueOf("2018-08-20"))
                .endDate(Date.valueOf("2018-08-21"))
                .costPerStudent(200)
                .tags("presentation, negotiation")
                .build();

    }
}
