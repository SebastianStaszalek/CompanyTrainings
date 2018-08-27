package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, EmployeeRepositoryCustom {

}
