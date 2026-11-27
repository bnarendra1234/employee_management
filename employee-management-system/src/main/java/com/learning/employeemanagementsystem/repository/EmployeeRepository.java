package com.learning.employeemanagementsystem.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.employeemanagementsystem.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);

	List<Employee> findByDepartment(String department);

	List<Employee> findBySalaryGreaterThan(BigDecimal salary);

	List<Employee> findByNameContainingIgnoreCase(String name);
}
