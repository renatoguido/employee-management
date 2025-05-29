package com.rtz.employee_management.adapter.in.controller;

import com.rtz.employee_management.adapter.in.mapper.EmployeeDTO;
import com.rtz.employee_management.adapter.in.mapper.EmployeeMapper;
import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
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
    public void testCreate_Success() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.create(employee)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<?> response = controller.create(employeeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());

        verify(employeeUseCase).create(employee);
    }

    @Test
    public void testCreate_Exception() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.create(employee)).thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = controller.create(employeeDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao criar funcionário.", response.getBody());
    }

    @Test
    public void testFindAll_Success() {
        List<Employee> employees = Arrays.asList(employee);
        List<EmployeeDTO> dtos = employees.stream().map(emp -> employeeDTO).collect(Collectors.toList());

        when(employeeUseCase.findAll()).thenReturn(employees);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<?> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos.size(), ((List<?>) response.getBody()).size());
    }

    @Test
    public void testFindAll_Exception() {
        when(employeeUseCase.findAll()).thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = controller.findAll();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao listar funcionários.", response.getBody());
    }

    @Test
    public void testFindById_Found() {
        when(employeeUseCase.findById(1L)).thenReturn(Optional.of(employee));
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<?> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        when(employeeUseCase.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.findById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Funcionário não encontrado.", response.getBody());
    }

    @Test
    public void testFindById_Exception() {
        when(employeeUseCase.findById(1L)).thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = controller.findById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao buscar funcionário.", response.getBody());
    }

    @Test
    public void testUpdate_Success() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.update(1L, employee)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        ResponseEntity<?> response = controller.update(1L, employeeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDTO, response.getBody());

        verify(employeeUseCase).update(1L, employee);
    }

    @Test
    public void testUpdate_NotFound() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.update(1L, employee)).thenThrow(new RuntimeException("Não encontrado"));

        ResponseEntity<?> response = controller.update(1L, employeeDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Funcionário não encontrado.", response.getBody());
    }

    @Test
    public void testUpdate_Exception() {
        when(mapper.toDomain(employeeDTO)).thenReturn(employee);
        when(employeeUseCase.update(1L, employee)).thenThrow(new IllegalStateException("Erro simulado"));

        ResponseEntity<?> response = controller.update(1L, employeeDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Funcionário não encontrado.", response.getBody());
    }

    @Test
    public void testDelete_Success() {
        doNothing().when(employeeUseCase).delete(1L);

        ResponseEntity<?> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(employeeUseCase).delete(1L);
    }

    @Test
    public void testDelete_Exception() {
        doThrow(new RuntimeException("Erro simulado")).when(employeeUseCase).delete(1L);

        ResponseEntity<?> response = controller.delete(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao excluir funcionário.", response.getBody());
    }

}