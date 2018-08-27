package com.capgemini.jstk.companytrainings.domain;

import com.capgemini.jstk.companytrainings.domain.enums.TrainingCharacter;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingStatus;
import com.capgemini.jstk.companytrainings.domain.enums.TrainingType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRAINING")
public class TrainingEntity extends AbstractEntity implements Serializable {

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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int costPerStudent;

    @Column(length = 100)
    private String tags;

    @Enumerated
    @Column(nullable = false)
    private TrainingStatus status = TrainingStatus.PLANNED;

    @ManyToMany(mappedBy = "trainingsAsCouch")
    private Set<EmployeeEntity> couches = new HashSet<>();

    @ManyToMany(mappedBy = "trainingsAsStudent")
    private Set<EmployeeEntity> students = new HashSet<>();

    @ManyToMany(mappedBy = "trainings")
    private Set<ExternalCouchEntity> externalCouches = new HashSet<>();

    public void addCouch(EmployeeEntity newCouch) {
        this.couches.add(newCouch);
        newCouch.updateTrainingAsCouchReference(this);
    }

    public void addStudent(EmployeeEntity newStudent) {
        this.students.add(newStudent);
        newStudent.updateTrainingAsStudentReference(this);
    }

    public void addExternalCouch(ExternalCouchEntity newCouch) {
        this.externalCouches.add(newCouch);
        newCouch.updateTrainingAsExternalCouchReference(this);
    }

    public void removeCouchesReferences() {
        for (EmployeeEntity c : couches) {
            c.removeTrainingAsCouch(this);
        }
    }

    public void removeStudentsReferences() {
        for (EmployeeEntity s : students) {
            s.removeTrainingAsStudent(this);
        }
    }

    public void removeExternalCouchesReferences() {
        for (ExternalCouchEntity ex : externalCouches) {
            ex.removeTrainingAsExternalCouch(this);
        }
    }

}
