package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;

import java.sql.Date;
import java.util.List;

public interface TrainingRepositoryCustom {

    List<TrainingEntity> findTrainingsByTagsQueryDSL(String tag);

    Double findSumOfTrainingHoursByCoachAndYear(Long id, int year);


    Double findSumWithJPQL(Long id,  Date date);
}
