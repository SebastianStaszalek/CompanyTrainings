package com.capgemini.jstk.companytrainings.dto;

import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTO {

    private Long id;
    private Long version;
    private String firstName;
    private String lastName;
    private EmployeePosition employeePosition;
    private Grade grade;
    private Long superiorId;


}
