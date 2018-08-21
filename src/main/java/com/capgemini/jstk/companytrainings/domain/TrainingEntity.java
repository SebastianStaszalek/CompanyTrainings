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

    @Enumerated
    @Column(nullable = false)
    private TrainingType trainingType;

    @Column(nullable = false)
    private Double duration;

    @Enumerated
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


}
