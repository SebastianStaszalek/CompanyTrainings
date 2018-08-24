package com.capgemini.jstk.companytrainings.dto;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTO {

    private Long id;
    private Long version;
    private String title;
    private TrainingType trainingType;
    private Double duration;
    private TrainingCharacter trainingCharacter;
    private LocalDate startDate;
    private LocalDate endDate;
    private int costPerStudent;
    private TrainingStatus status;
    private String tags;

}
