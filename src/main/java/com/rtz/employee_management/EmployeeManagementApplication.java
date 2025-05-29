package com.rtz.employee_management;

import com.rtz.employee_management.domain.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public static void main(String[] args) {
		logger.info("Iniciando a aplicação Employee Management...");
		SpringApplication.run(EmployeeManagementApplication.class, args);
		logger.info("Aplicação Employee Management iniciada com sucesso!");
	}

}
