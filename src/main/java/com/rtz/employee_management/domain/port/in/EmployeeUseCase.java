package com.rtz.employee_management.domain.port.in;

import com.rtz.employee_management.domain.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeUseCase {
    Employee create(Employee employee);

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee update(Long id, Employee employee);

    void delete(Long id);
}
