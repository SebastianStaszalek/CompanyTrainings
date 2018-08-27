package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.mapper.EmployeeMapper;
import com.capgemini.jstk.companytrainings.mapper.TrainingMapper;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepository;
import com.capgemini.jstk.companytrainings.service.EmployeeService;
import com.capgemini.jstk.companytrainings.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepository;

    private TrainingMapper trainingMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository,
                               TrainingMapper trainingMapper) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public EmployeeTO findEmployeeById(Long id) {
        return employeeMapper.map(employeeRepository.findOne(id));
    }

    @Override
    public List<EmployeeTO> getAllEmployees() {
        return employeeMapper.map2TO(employeeRepository.findAll());
    }

    @Override
    public EmployeeTO save(EmployeeTO employee) {
        Long superiorId = employee.getSuperiorId();


        EmployeeEntity employeeEntity = employeeMapper.map(employee);

        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findOne(superiorId);
            employeeEntity.setSuperior(superior);
        }
        return employeeMapper.map(employeeRepository.save(employeeEntity));
    }

    @Override
    public EmployeeTO update(EmployeeTO employee) {
        Long superiorId = employee.getSuperiorId();

        EmployeeEntity employeeEntity = employeeRepository.findOne(employee.getId());

        if(!employee.getVersion().equals(employeeEntity.getVersion())) {
            throw new OptimisticLockException();
        }

        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findOne(superiorId);
            employeeEntity.setSuperior(superior);
        }

        EmployeeEntity employeeToUpdate = employeeMapper.map(employee, employeeEntity);

        return employeeMapper.map(employeeToUpdate);
    }

    @Override
    public void addSuperiorToEmployee(EmployeeTO employee, EmployeeTO superior) {
        EmployeeEntity subordinate = employeeRepository.findOne(employee.getId());
        EmployeeEntity boss = employeeRepository.findOne(superior.getId());

        subordinate.setSuperior(boss);
    }

    @Override
    public void deleteEmployee(EmployeeTO employee) {
        employeeRepository.delete(employee.getId());
    }

    @Override
    public int countTrainingsForEmployeeInGivenTimePeriod(Long employeeId, LocalDate from, LocalDate to) {
        List<TrainingEntity> trainingsList = employeeRepository.findAllTrainingsByStudentAndTimePeriod(employeeId, from, to);

        if (trainingsList != null) {
        return trainingsList.size();
        }
        return 0;
    }

    @Override
    public Integer countTotalCostOfEmployeeTrainings(Long employeeId) {
        return employeeRepository.countCostOfStudentTrainings(employeeId);
    }

    @Override
    public List<EmployeeTO> findEmployeesWithMaxHoursSpentOnTrainings() {
        List<EmployeeEntity> employeesList = employeeRepository.findStudentsWithMaxHoursSpentOnTrainings();

        return employeeMapper.map2TO(employeesList);
    }

    @Override
    public Set<TrainingTO> getAllEmployeeTrainingsAsStudent(Long studentId) {
        EmployeeEntity employeeEntity = employeeRepository.findOne(studentId);

        Set<TrainingEntity> trainingsList = employeeEntity.getTrainingsAsStudent();

        return trainingMapper.map2TOSet(trainingsList);
    }

    @Override
    public Set<TrainingTO> getAllEmployeeTrainingsAsCouch(Long couchId) {
        EmployeeEntity employeeEntity = employeeRepository.findOne(couchId);

        Set<TrainingEntity> trainingsList = employeeEntity.getTrainingsAsStudent();

        return trainingMapper.map2TOSet(trainingsList);
    }
}
