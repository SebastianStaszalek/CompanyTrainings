package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {

    EmployeeEntity findById(Long id);

    List<EmployeeEntity> findAll();

    EmployeeEntity save(EmployeeEntity employee);

    void deleteById(Long id);
}
