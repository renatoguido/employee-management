package com.rtz.employee_management.adapter.in.mapper;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        log.info("Recebida solicitação para criar funcionário: {}", dto);
        Employee employee = mapper.toDomain(dto);
        Employee created = employeeUseCase.create(employee);
        log.info("Funcionário criado com sucesso: {}", created);
        return new ResponseEntity<>(mapper.toDTO(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        log.info("Recebida solicitação para listar todos os funcionários");
        List<Employee> employees = employeeUseCase.findAll();
        log.info("Total de funcionários encontrados: {}", employees.size());
        List<EmployeeDTO> dtos = employees.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        log.info("Recebida solicitação para buscar funcionário por ID: {}", id);
        return employeeUseCase.findById(id)
                .map(employee -> {
                    log.info("Funcionário encontrado: {}", employee);
                    return ResponseEntity.ok(mapper.toDTO(employee));
                })
                .orElseGet(() -> {
                    log.warn("Funcionário com ID {} não encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        log.info("Recebida solicitação para atualizar funcionário ID {} com dados: {}", id, dto);
        Employee employee = mapper.toDomain(dto);
        Employee updated = employeeUseCase.update(id, employee);
        log.info("Funcionário atualizado com sucesso: {}", updated);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Recebida solicitação para excluir funcionário com ID: {}", id);
        employeeUseCase.delete(id);
        log.info("Funcionário com ID {} excluído com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
