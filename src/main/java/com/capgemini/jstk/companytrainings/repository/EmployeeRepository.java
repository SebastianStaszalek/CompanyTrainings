package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, EmployeeRepositoryCustom {

}
