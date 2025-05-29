package com.rtz.employee_management.adapter.in.mapper;

import com.rtz.employee_management.domain.model.Employee;
import com.rtz.employee_management.domain.port.in.EmployeeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeUseCase employeeUseCase;
    private final EmployeeMapper mapper;

    public EmployeeController(EmployeeUseCase employeeUseCase, EmployeeMapper mapper) {
        this.employeeUseCase = employeeUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        Employee employee = mapper.toDomain(dto);
        Employee created = employeeUseCase.create(employee);
        return new ResponseEntity<>(mapper.toDTO(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        List<Employee> employees = employeeUseCase.findAll();
        List<EmployeeDTO> dtos = employees.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        return employeeUseCase.findById(id)
                .map(employee -> ResponseEntity.ok(mapper.toDTO(employee)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        Employee employee = mapper.toDomain(dto);
        Employee updated = employeeUseCase.update(id, employee);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
