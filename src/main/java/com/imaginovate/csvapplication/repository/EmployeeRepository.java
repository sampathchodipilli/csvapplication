package com.imaginovate.csvapplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imaginovate.csvapplication.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	Optional<List<Employee>> findByFirstName(String firstName);
	
	Optional<List<Employee>> findByLastName(String lastName);
	
	Optional<List<Employee>> findByRole(String role);
	
	Optional<List<Employee>> findByFirstNameAndLastName(String firstName, String lastName);
	
	Optional<List<Employee>> findByFirstNameAndRole(String firstName, String role);
	
	Optional<List<Employee>> findByLastNameAndRole(String lastName, String role);
	
	Optional<List<Employee>> findByFirstNameAndLastNameAndRole(String firstName, String lastName, String role);
}
