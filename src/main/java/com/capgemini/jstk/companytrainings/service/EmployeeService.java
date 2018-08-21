package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.EmployeeTO;

import java.util.List;

public interface EmployeeService {

    EmployeeTO findEmployeeById(Long id);

    List<EmployeeTO> getAllEmployees();

    EmployeeTO saveOrUpdate(EmployeeTO employee);

    void deleteEmployee(EmployeeTO employee);


}
