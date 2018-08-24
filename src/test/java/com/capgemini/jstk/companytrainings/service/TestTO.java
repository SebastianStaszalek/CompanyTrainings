package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.domain.enums.*;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
                .startDate(LocalDate.of(2018, 2, 20))
                .endDate(LocalDate.of(2018, 2, 21))
                .costPerStudent(200)
                .status(TrainingStatus.PLANNED)
                .tags("presentation, negotiation")
                .build();

    }
}
