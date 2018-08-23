package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long>, TrainingRepositoryCustom {

}
