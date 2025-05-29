package com.rtz.employee_management.adapter.in.mapper;

import com.rtz.employee_management.domain.model.Employee;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeMapper {

    EmployeeDTO toDTO(Employee employee);

    Employee toDomain(EmployeeDTO dto);
}
