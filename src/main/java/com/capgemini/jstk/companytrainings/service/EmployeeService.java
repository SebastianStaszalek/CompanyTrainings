package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    Set<TrainingTO> getAllEmployeeTrainingsAsStudent(Long studentId);

    Set<TrainingTO> getAllEmployeeTrainingsAsCouch(Long couchId);

}
