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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
public class EmployeeEntity implements Serializable {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public void addTrainingAsCouch(TrainingEntity newTraining) {
        this.trainingsAsCouch.add(newTraining);
        newTraining.updateCouchReference(this);
    }

    protected void updateTrainigAsCouchReference(TrainingEntity trainingEntity) {
        this.trainingsAsCouch.add(trainingEntity);
    }

    public void addTrainingAsStudent(TrainingEntity newTraining) {
        this.trainingsAsStudent.add(newTraining);
        newTraining.updateStudentReference(this);
    }

    protected void updateTrainigAsStudentReference(TrainingEntity trainingEntity) {
        this.trainingsAsStudent.add(trainingEntity);
    }
}
