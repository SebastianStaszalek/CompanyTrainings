package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends CrudRepository<TrainingEntity, Long> {

    TrainingEntity findById(Long id);

    List<TrainingEntity> findAll();

    @SuppressWarnings("unchecked")
    TrainingEntity save(TrainingEntity training);

    void deleteById(Long id);

}
