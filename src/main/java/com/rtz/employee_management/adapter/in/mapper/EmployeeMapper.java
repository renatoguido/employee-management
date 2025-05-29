package com.rtz.employee_management.adapter.in.mapper;

import com.rtz.employee_management.domain.model.Employee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .build();
    }

    public Employee toDomain(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Employee(dto.getId(), dto.getName(), dto.getEmail());
    }
}
