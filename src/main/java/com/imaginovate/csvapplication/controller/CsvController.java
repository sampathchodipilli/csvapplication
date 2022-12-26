package com.imaginovate.csvapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imaginovate.csvapplication.model.Employee;
import com.imaginovate.csvapplication.model.Response;
import com.imaginovate.csvapplication.service.CsvService;
/**
 * 
 * @author sampath
 * 
 * */
@RestController
@RequestMapping("/csvapp")
public class CsvController {
	
	@Autowired
	private CsvService service;
	
	/**
	 * This endpoint is used to load data from csv file to in memeroy database, the file is present in resources folder
	 * 
	 * @return Response - response object with status, code , message and data
	 * */
	@GetMapping("/loadData")
	public ResponseEntity<Response> loadCsvData() {
		Response response = service.parseCsvFile();
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * This endpoint is used to add another new employee to the databse
	 * 
	 * @param employee - employee object in json structure
	 * @return Response - response object with status, code , message and data
	 * 
	 * */
	@PostMapping("/addEmployee")
	public ResponseEntity<Response> adduser(@RequestBody Employee employee) {
		Response response = service.addEmployee(employee);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * This endpoint is used to delete an existing employee based on id
	 * 
	 * @param id - id of the employee to be deleted
	 * @return Response - response object with status, code , message and data
	 * 
	 * */
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable("id") Integer id) {
		Response response = service.deleteEmployee(id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * This endpoint is used to update the details of an already existing employee based on id
	 * 
	 * @param employee - details of employee to be updated along with the id of the employee
	 * @return Response - response object with status, code , message and data
	 * */
	@PutMapping("/updateEmployeeDetails")
	public ResponseEntity<Response> updateUser(@RequestBody Employee employee) {
		Response response = service.updateEmployeeDetails(employee);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * This endpoint is used to search and retrieve data based on firstname, lastname and role using pagination
	 * that is to display only size results per page
	 * with this endpoint you can search by firstname alone, lastname alone or role alone or you can search by the combinations
	 * of firstname, lastname and role
	 * 
	 * @param pageno - number of the page its a 0 based index
	 * @param size - number of results needed per page
	 * @param firstname - firstname of the employee to be searched
	 * @param lastname - lastname of the employee to be searched
	 * @param role - role of the employee to be searched
	 * 
	 * @return Response - response object with status, code , message and data
	 * 
	 * */
	@GetMapping("/getEmployeeByDetails")
	public ResponseEntity<Response> getUsersByDetails(@RequestParam("page") Integer page, @RequestParam("size") Integer size,@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName, @RequestParam("role") String role) {
		Response response = service.getEmplyeeByDetails(page-1,size,firstName,lastName,role);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
