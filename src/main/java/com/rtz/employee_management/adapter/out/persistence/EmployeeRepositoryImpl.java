package com.rtz.employee_management.adapter.out.persistence;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.out.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository jpaRepository;

    public EmployeeRepositoryImpl(EmployeeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private Employee toDomain(EmployeeEntity entity) {
        return new Employee(entity.getId(), entity.getName(), entity.getEmail());
    }

    private EmployeeEntity toEntity(Employee domain) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        return entity;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeEntity entity = toEntity(employee);
        EmployeeEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Employee> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
