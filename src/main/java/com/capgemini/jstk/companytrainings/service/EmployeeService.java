package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.EmployeeTO;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    EmployeeTO findEmployeeById(Long id);

    List<EmployeeTO> getAllEmployees();

    EmployeeTO save(EmployeeTO employee);

    EmployeeTO update(EmployeeTO employee);

    void addSuperiorToEmployee(EmployeeTO employee, EmployeeTO superior);

    void deleteEmployee(EmployeeTO employee);

    int countTrainingsForEmployeeInGivenTimePeriod(Long employeeId, LocalDate from, LocalDate to);

    Integer countTotalCostOfEmployeeTrainings(Long employeeId);

    List<EmployeeTO> findEmployeesWithMaxHoursSpentOnTrainings();

}
