package com.rtz.employee_management.adapter.in.exception;

public class EmployeeNotFoundException extends RuntimeException {
  public EmployeeNotFoundException(Long id) {
    super("Funcionário com ID " + id + " não encontrado.");
  }

  public EmployeeNotFoundException(String message) {
    super(message);
  }
}
