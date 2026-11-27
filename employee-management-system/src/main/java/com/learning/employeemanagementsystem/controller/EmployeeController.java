package com.learning.employeemanagementsystem.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learning.employeemanagementsystem.entity.Employee;
import com.learning.employeemanagementsystem.entity.EmployeePatchRequest;
import com.learning.employeemanagementsystem.response.ApiResponse;
import com.learning.employeemanagementsystem.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public ApiResponse<List<Employee>> getAllEmployees() {
		return response("Employees fetched successfully", employeeService.getAllEmployees(), 200);
	}

	@GetMapping("/{id}")
	public ApiResponse<Employee> getEmployeeById(@PathVariable Long id) {
		return response("Employee fetched successfully", employeeService.getEmployeeById(id), 200);
	}

	@GetMapping("/department/{dept}")
	public ApiResponse<List<Employee>> getEmployeesByDepartment(@PathVariable("dept") String department) {
		return response("Employees fetched successfully",
				employeeService.getEmployeesByDepartment(department), 200);
	}

	@GetMapping("/salary/{amount}")
	public ApiResponse<List<Employee>> getEmployeesWithSalaryGreaterThan(@PathVariable BigDecimal amount) {
		return response("Employees fetched successfully",
				employeeService.getEmployeesWithSalaryGreaterThan(amount), 200);
	}

	@GetMapping("/search")
	public ApiResponse<List<Employee>> searchEmployees(@RequestParam String name) {
		return response("Employees fetched successfully", employeeService.searchEmployeesByName(name), 200);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
		Employee createdEmployee = employeeService.createEmployee(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdEmployee.getId())
				.toUri();
		return ResponseEntity.created(location)
				.body(response("Employee created successfully", createdEmployee, 201));
	}

	@PutMapping("/{id}")
	public ApiResponse<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
		return response("Employee updated successfully", employeeService.updateEmployee(id, employee), 200);
	}

	@PatchMapping("/{id}")
	public ApiResponse<Employee> patchEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeePatchRequest patch) {
		return response("Employee updated successfully", employeeService.patchEmployee(id, patch), 200);
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return response("Employee deleted successfully", null, 200);
	}

	private <T> ApiResponse<T> response(String message, T data, int status) {
		return new ApiResponse<T>(message, data, status);
	}
}
