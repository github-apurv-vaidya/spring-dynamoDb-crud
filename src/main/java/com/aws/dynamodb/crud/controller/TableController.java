package com.aws.dynamodb.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.dynamodb.crud.model.Employee;
import com.aws.dynamodb.crud.service.TableService;

/**
 * @author Apurv
 * 
 *         create,retrieve,update,Delete
 *
 */
@RestController
@RequestMapping("api/v1")
public class TableController {

	@Autowired
	TableService service;

	@PostMapping("/create/employee")
	public ResponseEntity<String> createEmployee(@RequestBody Employee emp) {
		service.createEmp(emp);
		return new ResponseEntity<String>("Employee Created", HttpStatus.CREATED);
	}

	@GetMapping("/get/employee/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
		return new ResponseEntity<Employee>(service.getEmpDetails(id), HttpStatus.OK);
	}

	@DeleteMapping("/delete/employee/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
		service.deleteEmpById(id);
		return new ResponseEntity<String>("Employee with id " + id + " is deleted successfully", HttpStatus.OK);
	}
}
