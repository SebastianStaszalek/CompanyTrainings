package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImp implements EmployeeService {

    @Override
    public EmployeeTO findEmployeeById(Long id) {
        return null;
    }

    @Override
    public List<EmployeeTO> getAllEmployees() {
        return null;
    }

    @Override
    public EmployeeTO saveOrUpdate(EmployeeTO employee) {
        return null;
    }

    @Override
    public void deleteEmployee(EmployeeTO employee) {

    }
}
