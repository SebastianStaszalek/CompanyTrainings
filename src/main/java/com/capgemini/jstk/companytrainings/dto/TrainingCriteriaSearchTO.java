package com.capgemini.jstk.companytrainings.dto;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCriteriaSearchTO {

    private String title;
    private TrainingCharacter trainingCharacter;
    private LocalDate date;
    private Integer costFrom;
    private Integer costTo;
}
