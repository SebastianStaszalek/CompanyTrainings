package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.TrainingCriteriaSearchTO;

import java.util.List;

public interface TrainingRepositoryCustom {

    List<TrainingEntity> findTrainingsByMultipleCriteria(TrainingCriteriaSearchTO searchCriteria);

    List<TrainingEntity> findTrainingsByTagsQueryDSL(String tag);

    Double findSumOfTrainingHoursByCoachAndYear(Long id, int year);

    List<TrainingEntity> findTrainingsWithLargestNumberOfEditions();

}
