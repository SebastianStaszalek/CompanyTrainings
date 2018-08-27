package com.capgemini.jstk.companytrainings.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EXTERNAL_COUCH")
public class ExternalCouchEntity extends AbstractEntity implements Serializable {

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, length = 30)
    private String companyName;

    @ManyToMany
    @JoinTable(name = "TRAININGS_WITH_EXTERNAL_COUCHES",
            joinColumns = {@JoinColumn(name = "EXTERNAL_COUCH_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TRAINING_ID")}
    )
    Set<TrainingEntity> trainings = new HashSet<>();


    protected void updateTrainingAsExternalCouchReference(TrainingEntity trainingEntity) {
        this.trainings.add(trainingEntity);
    }

    protected void removeTrainingAsExternalCouch(TrainingEntity trainingEntity) {
        this.trainings.remove(trainingEntity);
    }

}
