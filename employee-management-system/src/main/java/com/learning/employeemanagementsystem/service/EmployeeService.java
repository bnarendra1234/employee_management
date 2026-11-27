package com.learning.employeemanagementsystem.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.employeemanagementsystem.entity.Employee;
import com.learning.employeemanagementsystem.entity.EmployeePatchRequest;
import com.learning.employeemanagementsystem.exception.DuplicateEmailException;
import com.learning.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.learning.employeemanagementsystem.exception.InvalidArgumentException;
import com.learning.employeemanagementsystem.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Transactional(readOnly = true)
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Employee> getEmployeesByDepartment(String department) {
		return employeeRepository.findByDepartment(department);
	}

	@Transactional(readOnly = true)
	public List<Employee> getEmployeesWithSalaryGreaterThan(BigDecimal salary) {
		return employeeRepository.findBySalaryGreaterThan(salary);
	}

	@Transactional(readOnly = true)
	public List<Employee> searchEmployeesByName(String name) {
		return employeeRepository.findByNameContainingIgnoreCase(name);
	}

	@Transactional(readOnly = true)
	public Employee getEmployeeById(Long id) {
		validateId(id);
		return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	public Employee createEmployee(Employee employee) {
		if (employee == null) {
			throw new InvalidArgumentException("Employee details are required");
		}
		if (employeeRepository.existsByEmail(employee.getEmail())) {
			throw new DuplicateEmailException(employee.getEmail());
		}
		return employeeRepository.save(employee);
	}

	public Employee updateEmployee(Long id, Employee employee) {
		validateId(id);
		if (employee == null) {
			throw new InvalidArgumentException("Employee details are required");
		}

		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
		if (employeeRepository.existsByEmailAndIdNot(employee.getEmail(), id)) {
			throw new DuplicateEmailException(employee.getEmail());
		}

		existingEmployee.setName(employee.getName());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setDepartment(employee.getDepartment());
		existingEmployee.setSalary(employee.getSalary());
		existingEmployee.setJoiningDate(employee.getJoiningDate());
		return employeeRepository.save(existingEmployee);
	}

	public Employee patchEmployee(Long id, EmployeePatchRequest patch) {
		validateId(id);
		if (patch == null) {
			throw new InvalidArgumentException("Employee update details are required");
		}
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		if (patch.getName() != null && patch.getName().trim().isEmpty()) {
			throw new InvalidArgumentException("Employee name must not be blank");
		}
		if (patch.getEmail() != null && patch.getEmail().trim().isEmpty()) {
			throw new InvalidArgumentException("Employee email must not be blank");
		}
		if (patch.getDepartment() != null && patch.getDepartment().trim().isEmpty()) {
			throw new InvalidArgumentException("Employee department must not be blank");
		}
		if (patch.getEmail() != null && employeeRepository.existsByEmailAndIdNot(patch.getEmail(), id)) {
			throw new DuplicateEmailException(patch.getEmail());
		}
		if (patch.getName() != null) {
			employee.setName(patch.getName());
		}
		if (patch.getEmail() != null) {
			employee.setEmail(patch.getEmail());
		}
		if (patch.getDepartment() != null) {
			employee.setDepartment(patch.getDepartment());
		}
		if (patch.getSalary() != null) {
			employee.setSalary(patch.getSalary());
		}
		if (patch.getJoiningDate() != null) {
			employee.setJoiningDate(patch.getJoiningDate());
		}
		return employeeRepository.save(employee);
	}

	public void deleteEmployee(Long id) {
		validateId(id);
		if (!employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}
		employeeRepository.deleteById(id);
	}

	private void validateId(Long id) {
		if (id == null || id <= 0) {
			throw new InvalidArgumentException("Employee id must be a positive number");
		}
	}

}
