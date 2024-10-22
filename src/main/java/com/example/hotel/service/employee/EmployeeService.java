package com.example.hotel.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.employee.Employee;
import com.example.hotel.repository.employee.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public void saveEmployee(Employee employee) {
		this.employeeRepository.save(employee);
	}

	public Employee findEmployeeById(Long id) {
		return employeeRepository.findById(id).get();
	}

	public void deleteEmployeeById(Long employeeId) {
		employeeRepository.deleteById(employeeId);
	}
}
