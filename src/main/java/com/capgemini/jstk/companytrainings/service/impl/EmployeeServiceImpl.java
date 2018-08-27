package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.EmployeeNotFoundException;
import com.capgemini.jstk.companytrainings.exception.ResultNotFoundException;
import com.capgemini.jstk.companytrainings.exception.message.Message;
import com.capgemini.jstk.companytrainings.mapper.EmployeeMapper;
import com.capgemini.jstk.companytrainings.mapper.TrainingMapper;
import com.capgemini.jstk.companytrainings.repository.EmployeeRepository;
import com.capgemini.jstk.companytrainings.service.EmployeeService;
import com.google.common.base.Preconditions;
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
        Preconditions.checkNotNull(id, Message.EMPTY_ID);
        return employeeMapper.map(employeeRepository.findOne(id));
    }

    @Override
    public List<EmployeeTO> getAllEmployees() {
        return employeeMapper.map2TO(employeeRepository.findAll());
    }

    @Override
    public EmployeeTO save(EmployeeTO employee) {
        Preconditions.checkNotNull(employee, Message.EMPTY_OBJECT);

        EmployeeEntity employeeEntity = employeeMapper.map(employee);

        Long superiorId = employee.getSuperiorId();
        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findOne(superiorId);
            employeeEntity.setSuperior(superior);
        }
        return employeeMapper.map(employeeRepository.save(employeeEntity));
    }

    @Override
    public EmployeeTO update(EmployeeTO employee) {
        Preconditions.checkNotNull(employee.getId(), Message.EMPTY_ID);

        EmployeeEntity employeeEntity = employeeRepository.findOne(employee.getId());

        if(employeeEntity == null) {
            throw new EmployeeNotFoundException(Message.EMPLOYEE_NOT_IN_DB);
        }

        if(!employee.getVersion().equals(employeeEntity.getVersion())) {
            throw new OptimisticLockException();
        }

        Long superiorId = employee.getSuperiorId();
        if (superiorId != null) {
            EmployeeEntity superior = employeeRepository.findOne(superiorId);
            employeeEntity.setSuperior(superior);
        }

        EmployeeEntity employeeToUpdate = employeeMapper.map(employee, employeeEntity);

        return employeeMapper.map(employeeToUpdate);
    }

    @Override
    public void addSuperiorToEmployee(EmployeeTO employee, EmployeeTO superior) {
        Preconditions.checkNotNull(employee.getId(), Message.EMPTY_ID);
        Preconditions.checkNotNull(superior.getId(), Message.EMPTY_ID);

        EmployeeEntity subordinate = employeeRepository.findOne(employee.getId());
        EmployeeEntity boss = employeeRepository.findOne(superior.getId());

        if(subordinate == null || boss == null) {
            throw new EmployeeNotFoundException(Message.EMPLOYEE_NOT_IN_DB);
        }

        subordinate.setSuperior(boss);
    }

    @Override
    public void deleteEmployee(EmployeeTO employee) {
        Preconditions.checkNotNull(employee.getId(), Message.EMPTY_ID);
        employeeRepository.delete(employee.getId());
    }

    @Override
    public int countTrainingsForEmployeeInGivenTimePeriod(Long employeeId, LocalDate from, LocalDate to) {
        Preconditions.checkNotNull(employeeId, Message.EMPTY_ID);
        Preconditions.checkNotNull(from, Message.EMPTY_FIELD);
        Preconditions.checkNotNull(to, Message.EMPTY_FIELD);

        List<TrainingEntity> trainingsList = employeeRepository.findAllTrainingsByStudentAndTimePeriod(employeeId, from, to);

        if (trainingsList.isEmpty()) {
            throw new ResultNotFoundException(Message.NO_RESULT);
        }

        return trainingsList.size();
    }

    @Override
    public Integer countTotalCostOfEmployeeTrainings(Long employeeId) {
        Preconditions.checkNotNull(employeeId, Message.EMPTY_ID);

        Integer result = employeeRepository.countCostOfStudentTrainings(employeeId);
        if(result == null) {
            throw new ResultNotFoundException(Message.NO_RESULT);
        }

        return result;
    }

    @Override
    public List<EmployeeTO> findEmployeesWithMaxHoursSpentOnTrainings() {
        List<EmployeeEntity> employeesList = employeeRepository.findStudentsWithMaxHoursSpentOnTrainings();

        if(employeesList.isEmpty()) {
            throw new EmployeeNotFoundException(Message.EMPLOYEE_NOT_FOUND);
        }

        return employeeMapper.map2TO(employeesList);
    }

    @Override
    public Set<TrainingTO> getAllEmployeeTrainingsAsStudent(Long studentId) {
        Preconditions.checkNotNull(studentId, Message.EMPTY_ID);
        EmployeeEntity employeeEntity = employeeRepository.findOne(studentId);

        if(employeeEntity == null) {
            throw new EmployeeNotFoundException(Message.EMPLOYEE_NOT_IN_DB);
        }

        Set<TrainingEntity> trainingsList = employeeEntity.getTrainingsAsStudent();

        return trainingMapper.map2TOSet(trainingsList);
    }

    @Override
    public Set<TrainingTO> getAllEmployeeTrainingsAsCouch(Long couchId) {
        Preconditions.checkNotNull(couchId, Message.EMPTY_ID);
        EmployeeEntity employeeEntity = employeeRepository.findOne(couchId);

        if(employeeEntity == null) {
            throw new EmployeeNotFoundException(Message.EMPLOYEE_NOT_IN_DB);
        }

        Set<TrainingEntity> trainingsList = employeeEntity.getTrainingsAsCouch();

        return trainingMapper.map2TOSet(trainingsList);
    }
}
