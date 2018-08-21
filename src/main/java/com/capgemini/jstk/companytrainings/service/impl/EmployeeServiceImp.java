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
public class EmployeeServiceImp implements EmployeeService {

    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImp(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository) {
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
    public EmployeeTO saveOrUpdate(EmployeeTO employee) {
        EmployeeEntity employeeEntity = employeeMapper.map(employee);
        return employeeMapper.map(employeeRepository.save(employeeEntity));
    }

    @Override
    public void deleteEmployee(EmployeeTO employee) {
        employeeRepository.deleteById(employee.getId());
    }
}
