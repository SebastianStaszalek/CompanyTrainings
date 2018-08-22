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
            return EmployeeTO.builder()
                    .id(employeeEntity.getId())
                    .version(employeeEntity.getVersion())
                    .firstName(employeeEntity.getFirstName())
                    .lastName(employeeEntity.getLastName())
                    .employeePosition(employeeEntity.getEmployeePosition())
                    .grade(employeeEntity.getGrade())
                    .build();
        }
        return null;
    }

    public EmployeeEntity map(EmployeeTO employeeTO) {
        return map(employeeTO, new EmployeeEntity());
    }

    public EmployeeEntity map(EmployeeTO employeeTO, EmployeeEntity employeeEntity) {
        if(employeeTO != null) {
            employeeEntity.setId(employeeTO.getId());
            employeeEntity.setVersion(employeeTO.getVersion());
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
