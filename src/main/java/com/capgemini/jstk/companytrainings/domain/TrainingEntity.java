package com.capgemini.jstk.companytrainings.domain;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRAINING")
public class TrainingEntity implements Serializable {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingType trainingType;

    @Column(nullable = false)
    private Double duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingCharacter trainingCharacter;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private int costPerStudent;

    @Column(length = 245)
    private String tags;

    @ManyToMany(mappedBy = "trainingsAsCouch")
    private Set<EmployeeEntity> couches = new HashSet<>();

    @ManyToMany(mappedBy = "trainingsAsStudent")
    private Set<EmployeeEntity> students = new HashSet<>();

    @ManyToMany(mappedBy = "trainings")
    private Set<ExternalCouchEntity> externalCouches = new HashSet<>();

    public void addCouch(EmployeeEntity newCouch) {
        this.couches.add(newCouch);
        newCouch.updateTrainigAsCouchReference(this);
    }

    protected void updateCouchReference(EmployeeEntity employeeEntity) {
        this.couches.add(employeeEntity);
    }

    public void addStudent(EmployeeEntity newStudent) {
        this.students.add(newStudent);
        newStudent.updateTrainigAsStudentReference(this);
    }

    protected void updateStudentReference(EmployeeEntity employeeEntity) {
        this.students.add(employeeEntity);
    }

}
