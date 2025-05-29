package com.rtz.employee_management.adapter.in.mapper;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;



public class EmployeeControllerTest {

    @Mock
    private EmployeeUseCase employeeUseCase;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeController controller;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Renato");
        employee.setEmail("renato@example.com");

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("Renato");
        employeeDTO.setEmail("renato@example.com");
    }

    @Test
    public void testCreate() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.create(employee)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<EmployeeDTO> response = controller.create(employeeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());

        verify(employeeUseCase).create(employee);
    }

    @Test
    public void testFindAll() {
        List<Employee> employees = Arrays.asList(employee);
        List<EmployeeDTO> dtos = employees.stream().map(emp -> employeeDTO).collect(Collectors.toList());

        when(employeeUseCase.findAll()).thenReturn(employees);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<List<EmployeeDTO>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos.size(), response.getBody().size());
        assertEquals(dtos.get(0), response.getBody().get(0));

        verify(employeeUseCase).findAll();
    }

    @Test
    public void testFindById_Found() {
        when(employeeUseCase.findById(1L)).thenReturn(Optional.of(employee));
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<EmployeeDTO> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());

        verify(employeeUseCase).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(employeeUseCase.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDTO> response = controller.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(employeeUseCase).findById(1L);
    }

    @Test
    public void testUpdate() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.update(1L, employee)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<EmployeeDTO> response = controller.update(1L, employeeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());

        verify(employeeUseCase).update(1L, employee);
    }

    @Test
    public void testDelete() {
        doNothing().when(employeeUseCase).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(employeeUseCase).delete(1L);
    }
}