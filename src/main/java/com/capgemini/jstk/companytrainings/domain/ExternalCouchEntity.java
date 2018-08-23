package com.capgemini.jstk.companytrainings.domain;

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
@Table(name = "EXTERNAL_COUCH")
public class ExternalCouchEntity extends AbstractEntity implements Serializable {

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String secondName;

    @Column(nullable = false, length = 30)
    private String companyName;

    @ManyToMany
    @JoinTable(name = "TRAININGS_WITH_EXTERNAL_COUCHES",
            joinColumns = {@JoinColumn(name = "EXTERNAL_COUCH_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TRAINING_ID")}
    )
    Set<TrainingEntity> trainings = new HashSet<>();



}
