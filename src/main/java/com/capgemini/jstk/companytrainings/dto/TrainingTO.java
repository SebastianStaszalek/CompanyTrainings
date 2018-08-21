package com.capgemini.jstk.companytrainings.dto;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTO {

    private Long id;
    private String title;
    private TrainingType trainingType;
    private Double duration;
    private TrainingCharacter trainingCharacter;
    private Date startDate;
    private Date endDate;
    private int costPerStudent;
    private String tags;

    private Set<Long> studentsIds;
    private Set<Long> couchesIds;
}
