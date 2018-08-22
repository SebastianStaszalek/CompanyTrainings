package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.mapper.EmployeeMapper;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepository;
import com.capgemini.jstk.companytrainings.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeTO findEmployeeById(Long id) {
        return employeeMapper.map(employeeRepository.findById(id));
    }

    @Override
    public List<EmployeeTO> getAllEmployees() {
        return employeeMapper.map2TO(employeeRepository.findAll());
    }
//TODO: jak zrobic update w tym przypadku zeby nie stracic powiazan?
    @Override
    public EmployeeTO save(EmployeeTO employee) {
        Long superiorId = employee.getSuperiorId();


        EmployeeEntity employeeEntity = employeeMapper.map(employee);

        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findById(superiorId);
            employeeEntity.setSuperior(superior);
        }
        return employeeMapper.map(employeeRepository.save(employeeEntity));
    }

    @Override
    public EmployeeTO update(EmployeeTO employee) {
        Long superiorId = employee.getSuperiorId();
        EmployeeEntity employeeEntity = employeeRepository.findById(employee.getId());
        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findById(superiorId);
            employeeEntity.setSuperior(superior);
        }

        EmployeeEntity employeeToUpdate = employeeMapper.map(employee, employeeEntity);

        return employeeMapper.map(employeeRepository.save(employeeToUpdate));

    }

    @Override
    public void deleteEmployee(EmployeeTO employee) {
        employeeRepository.deleteById(employee.getId());
    }
}
