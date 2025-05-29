package com.rtz.employee_management.domain.service;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import com.rtz.employee_management.domain.port.out.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee create(Employee employee) {
        try {
            return repository.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar funcionário: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar funcionários: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar funcionário: " + e.getMessage(), e);
        }
    }

    @Override
    public Employee update(Long id, Employee employee) {
        try {
            Optional<Employee> existing = repository.findById(id);
            if (existing.isEmpty()) {
                throw new IllegalStateException("Funcionário não encontrado para atualização");
            }

            Employee toUpdate = existing.get();
            toUpdate.setName(employee.getName());
            toUpdate.setEmail(employee.getEmail());

            return repository.save(toUpdate);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar funcionário: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Optional<Employee> existing = repository.findById(id);
            if (existing.isEmpty()) {
                throw new IllegalStateException("Funcionário não encontrado para exclusão");
            }
            repository.deleteById(id);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir funcionário: " + e.getMessage(), e);
        }
    }
}
