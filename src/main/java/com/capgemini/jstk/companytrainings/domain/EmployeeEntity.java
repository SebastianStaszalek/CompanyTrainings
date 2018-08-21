package com.capgemini.jstk.companytrainings.domain;

import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Enumerated
    @Column(nullable = false)
    private EmployeePosition employeePosition;

    @Enumerated
    private Grade grade;

    @ManyToOne
    private EmployeeEntity superior;

    @ManyToMany
    @JoinTable(name = "EMPLOYEES_AS_COUCHES",
            joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TRAINING_ID")}
    )
    private Set<TrainingEntity> trainingsAsCouch = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "EMPLOYEES_AS_STUDENTS",
            joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TRAINING_ID")}
    )
    private Set<TrainingEntity> trainingsAsStudent = new HashSet<>();


}
