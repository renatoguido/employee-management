package com.rtz.employee_management.domain.service;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.out.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "Renato", "renato@example.com");
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.create(employee);

        assertEquals(employee, result);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testFindById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee updated = employeeService.update(1L, employee);

        assertEquals(employee, updated);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.delete(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void testCreateEmployee_WhenRepositoryThrowsException_ShouldThrowRuntimeException() {
        when(employeeRepository.save(employee)).thenThrow(new RuntimeException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.create(employee);
        });

        assertEquals("Error saving employee", exception.getMessage());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeNotFound_ShouldThrowRuntimeException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.update(1L, employee);
        });

        assertEquals("Employee not found with id 1", exception.getMessage());
    }

    @Test
    void testUpdateEmployee_WhenRepositoryThrowsExceptionOnSave_ShouldThrowRuntimeException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenThrow(new RuntimeException("DB error on update"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.update(1L, employee);
        });

        assertEquals("Error updating employee", exception.getMessage());
    }

    @Test
    void testDeleteEmployee_WhenRepositoryThrowsException_ShouldThrowRuntimeException() {
        doThrow(new RuntimeException("DB error on delete")).when(employeeRepository).deleteById(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.delete(1L);
        });

        assertEquals("Error deleting employee", exception.getMessage());
    }
}
