package com.rtz.employee_management.adapter.in.controller;

import com.rtz.employee_management.adapter.in.mapper.EmployeeDTO;
import com.rtz.employee_management.adapter.in.mapper.EmployeeMapper;
import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {


    private final EmployeeUseCase employeeUseCase;
    private final EmployeeMapper mapper;

    public EmployeeController(EmployeeUseCase employeeUseCase, EmployeeMapper mapper) {
        this.employeeUseCase = employeeUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody EmployeeDTO dto) {
        try {
            log.info("Recebida solicitação para criar funcionário: {}", dto);
            Employee employee = mapper.toDomain(dto);
            Employee created = employeeUseCase.create(employee);
            log.info("Funcionário criado com sucesso: {}", created);
            return new ResponseEntity<>(mapper.toDTO(created), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Erro ao criar funcionário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar funcionário.");
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            log.info("Recebida solicitação para listar todos os funcionários");
            List<Employee> employees = employeeUseCase.findAll();
            log.info("Total de funcionários encontrados: {}", employees.size());
            List<EmployeeDTO> dtos = employees.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erro ao listar funcionários: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar funcionários.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            log.info("Recebida solicitação para buscar funcionário por ID: {}", id);
            Optional<Employee> employeeOpt = employeeUseCase.findById(id);
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                log.info("Funcionário encontrado: {}", employee);
                return ResponseEntity.ok(mapper.toDTO(employee));
            } else {
                log.warn("Funcionário com ID {} não encontrado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
            }
        } catch (Exception e) {
            log.error("Erro ao buscar funcionário por ID: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar funcionário.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        try {
            log.info("Recebida solicitação para atualizar funcionário ID {} com dados: {}", id, dto);
            Employee employee = mapper.toDomain(dto);
            Employee updated = employeeUseCase.update(id, employee);
            log.info("Funcionário atualizado com sucesso: {}", updated);
            return ResponseEntity.ok(mapper.toDTO(updated));
        } catch (RuntimeException e) {
            log.warn("Funcionário com ID {} não encontrado: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        } catch (Exception e) {
            log.error("Erro ao atualizar funcionário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar funcionário.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            log.info("Recebida solicitação para excluir funcionário com ID: {}", id);
            employeeUseCase.delete(id);
            log.info("Funcionário com ID {} excluído com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir funcionário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir funcionário.");
        }
    }
}
