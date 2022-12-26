package com.imaginovate.csvapplication.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imaginovate.csvapplication.constants.Constants;
import com.imaginovate.csvapplication.model.Employee;
import com.imaginovate.csvapplication.model.Response;
import com.imaginovate.csvapplication.repository.EmployeeRepository;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@Service
public class CsvService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public Response parseCsvFile() {
		try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mock_data.csv")) {
			
			Map<String, String> map = new HashMap<>();
			
			map.put("id", "id");
			map.put("first_name", "firstName");
			map.put("last_name", "lastName");
			map.put("email", "email");
			map.put("gender", "gender");
			map.put("dob", "dob");
			map.put("role", "role");
			
			HeaderColumnNameTranslateMappingStrategy<Employee> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
			strategy.setType(Employee.class);
			strategy.setColumnMapping(map);
			
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
			CsvToBean<Employee> bean = new CsvToBean<>();
			bean.setCsvReader(reader);
			bean.setMappingStrategy(strategy);
			List<Employee> employeeList = bean.parse();
			
			List<Employee> saveAll = (List<Employee>) employeeRepository.saveAll(employeeList);
			
			return new Response(Constants.COMPLETED, Constants.HTTP_200, "Data Saved !", saveAll);
		} catch(Exception e) {
			e.printStackTrace();
			return new Response(Constants.ERROR, Constants.HTTP_400, "Data Save Failed !");
		}
	}

	public Response addEmployee(Employee employee) {
		try {
			employee.setId(null);
			Employee save = employeeRepository.save(employee);
			return new Response(Constants.COMPLETED, Constants.HTTP_200, "Employee Saved !", save);
		} catch (Exception e) {
			logger.error("Error :: ",e);
			return new Response(Constants.ERROR, Constants.HTTP_400, "Employee save failed !");
		}
	}

	public Response deleteEmployee(Integer id) {
		try {
			employeeRepository.deleteById(id);
			return new Response(Constants.COMPLETED, Constants.HTTP_200, "Employee Deleted !");
		} catch (Exception e) {
			logger.error("Error :: ",e);
			return new Response(Constants.ERROR, Constants.HTTP_400, "Delete failed !");
		}
	}

	public Response updateEmployeeDetails(Employee employee) {
		try {
			Optional<Employee> optional = employeeRepository.findById(employee.getId());
			if(optional.isPresent()) {
				Employee employee2 = optional.get();
				if(employee.getFirstName()!=null && !employee.getFirstName().isEmpty() && !employee.getFirstName().isBlank()) {
					employee2.setFirstName(employee.getFirstName());
				}
				Employee employee3 = employeeRepository.save(employee);
				return new Response(Constants.COMPLETED, Constants.HTTP_200, "Employee Info Updated !", employee3);
			} else {
				return new Response(Constants.FAILED, Constants.HTTP_400, "No such employee exists to update");
			}
		} catch (Exception e) {
			logger.error("Error :: ",e);
			return new Response(Constants.ERROR, Constants.HTTP_400, "Updation failed !");
		}
	}

	public Response getEmplyeeByDetails(Integer page, Integer size, String firstName, String lastName, String role) {
		try {
			Response response = new Response(Constants.COMPLETED, Constants.HTTP_200, "Data Retrieved !");
			Pageable pageable = PageRequest.of(page, size);
			Boolean notFound = false;
			Optional<List<Employee>> optional = null;
			if(!isEmptyOrNull(firstName) && !isEmptyOrNull(lastName) && !isEmptyOrNull(role)) {
				optional = employeeRepository.findByFirstNameAndLastNameAndRole(firstName, lastName, role);
			} else if(!isEmptyOrNull(firstName) && !isEmptyOrNull(lastName)) {
				optional = employeeRepository.findByFirstNameAndLastName(firstName, lastName);
			} else if(!isEmptyOrNull(firstName) && !isEmptyOrNull(role)) {
				optional = employeeRepository.findByFirstNameAndRole(firstName, role);
			} else if (!isEmptyOrNull(lastName) && !isEmptyOrNull(role)) {
				optional = employeeRepository.findByLastNameAndRole(lastName, role);
			} else if(!isEmptyOrNull(firstName)) {
				optional = employeeRepository.findByFirstName(firstName);
			} else if(!isEmptyOrNull(lastName)) {
				optional = employeeRepository.findByLastName(lastName);
			} else if(!isEmptyOrNull(role)) {
				optional = employeeRepository.findByRole(role);
			} else {
				Page<Employee> page2 = employeeRepository.findAll(pageable);
				response.setData(page2.getContent());
				return response;
			}
			
			if(optional !=null && optional.isPresent()) {
				response.setData(optional.get());
			} else {
				return new Response(Constants.FAILED, Constants.HTTP_400, "No Data To Retrieve !");
			}
			
			return response;
		}catch (Exception e) {
			logger.error("Error :: ",e);
			return new Response(Constants.ERROR, Constants.HTTP_400, "Search and retrieval failed !");
		}
	}
	
	private Boolean isEmptyOrNull(String s) {
		if(s==null || s.isEmpty() || s.isBlank()) {
			return true;
		}
		return false;
	}
	
	

}
