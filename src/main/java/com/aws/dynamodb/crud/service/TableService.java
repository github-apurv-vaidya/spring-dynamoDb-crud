package com.aws.dynamodb.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.aws.dynamodb.crud.model.Employee;

@Component
public class TableService {

	@Autowired
	DynamoDBMapper dbMapper;

	public void createEmp(Employee emp) {
		dbMapper.save(emp);
	}

	public Employee getEmpDetails(String id) {
		return dbMapper.load(Employee.class, id);
	}

	public void deleteEmpById(String id) {
		Employee emp = getEmpDetails(id);
		dbMapper.delete(emp);
	}

}
