package com.capgemini.jstk.companytrainings.mapper;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeTO map(EmployeeEntity employeeEntity) {
        if(employeeEntity != null) {
            EmployeeTO employeeTO = new EmployeeTO();

            employeeTO.setId(employeeEntity.getId());
            employeeTO.setVersion(employeeEntity.getVersion());
            employeeTO.setFirstName(employeeEntity.getFirstName());
            employeeTO.setLastName(employeeEntity.getLastName());
            employeeTO.setEmployeePosition(employeeEntity.getEmployeePosition());

            if (employeeEntity.getGrade() != null){
                employeeTO.setGrade(employeeEntity.getGrade());
            }
            if (employeeEntity.getSuperior() != null) {
                employeeTO.setSuperiorId(employeeEntity.getSuperior().getId());
            }

            return employeeTO;
        }
        return null;
    }

    public EmployeeEntity map(EmployeeTO employeeTO) {
        return map(employeeTO, new EmployeeEntity());
    }

    public EmployeeEntity map(EmployeeTO employeeTO, EmployeeEntity employeeEntity) {
        if(employeeTO != null) {
            employeeEntity.setId(employeeTO.getId());
            employeeEntity.setFirstName(employeeTO.getFirstName());
            employeeEntity.setLastName(employeeTO.getLastName());
            employeeEntity.setEmployeePosition(employeeTO.getEmployeePosition());
            employeeEntity.setGrade(employeeTO.getGrade());

            return employeeEntity;
        }
        return null;
    }

    public List<EmployeeTO> map2TO(List<EmployeeEntity> employeeEntities) {
        return employeeEntities.stream().map(this::map).collect(Collectors.toList());
    }

    public Set<EmployeeTO> map2TOSet(Set<EmployeeEntity> employeeEntities) {
        return employeeEntities.stream().map(this::map).collect(Collectors.toSet());
    }
}
