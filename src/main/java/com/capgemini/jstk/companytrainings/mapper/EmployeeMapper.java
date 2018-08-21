package com.capgemini.jstk.companytrainings.mapper;

import com.capgemini.jstk.companytrainings.domain.EmployeeEntity;
import com.capgemini.jstk.companytrainings.dto.EmployeeTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
//TODO: czy w TO potrzebujemy superior ID?
    public EmployeeTO map(EmployeeEntity employeeEntity) {
        if(employeeEntity != null) {
            return EmployeeTO.builder()
                    .id(employeeEntity.getId())
                    .firstName(employeeEntity.getFirstName())
                    .lastName(employeeEntity.getLastName())
                    .employeePosition(employeeEntity.getEmployeePosition())
                    .grade(employeeEntity.getGrade())
                    .superiorId(employeeEntity.getSuperior().getId())
                    .build();
        }
        return null;
    }

    public EmployeeEntity map(EmployeeTO employeeTO) {
        if(employeeTO != null) {
            return EmployeeEntity.builder()
                    .id(employeeTO.getId())
                    .firstName(employeeTO.getFirstName())
                    .lastName(employeeTO.getLastName())
                    .employeePosition(employeeTO.getEmployeePosition())
                    .grade(employeeTO.getGrade())
                    .build();
        }
        return null;
    }

    public List<EmployeeTO> map2TO(List<EmployeeEntity> employeeEntities) {
        return employeeEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
