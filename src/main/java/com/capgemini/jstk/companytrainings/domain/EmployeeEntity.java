package com.capgemini.jstk.companytrainings.domain;

import com.capgemini.jstk.companytrainings.domain.enums.Grade;
import com.capgemini.jstk.companytrainings.domain.enums.EmployeePosition;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
public class EmployeeEntity extends AbstractEntity implements Serializable {


    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeePosition employeePosition;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @ManyToOne
    private EmployeeEntity superior;

    @ManyToMany(fetch = FetchType.EAGER)
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


    protected void updateTrainingAsCouchReference(TrainingEntity trainingEntity) {
        this.trainingsAsCouch.add(trainingEntity);
    }

    protected void updateTrainingAsStudentReference(TrainingEntity trainingEntity) {
        this.trainingsAsStudent.add(trainingEntity);
    }

    protected void removeTrainingAsStudent(TrainingEntity trainingEntity) {
        this.trainingsAsStudent.remove(trainingEntity);
    }

    protected void removeTrainingAsCouch(TrainingEntity trainingEntity) {
        this.trainingsAsCouch.remove(trainingEntity);
    }
}
