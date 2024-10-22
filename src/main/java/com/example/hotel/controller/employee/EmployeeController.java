package com.example.hotel.controller.employee;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.entity.employee.Employee;
import com.example.hotel.repository.employee.EmployeeRepository;
import com.example.hotel.service.employee.EmployeeService;

@RestController
@CrossOrigin
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@PostMapping("/employee/save")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
		employeeService.saveEmployee(employee);
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("message", "employee data saved successfully");
		return ResponseEntity.ok(employeeMap);
	}

	@PutMapping("/employee/edit/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody Employee employee) {
		try {
			Employee existingEmployee = employeeService.findEmployeeById(employeeId);
			if (existingEmployee == null) {
				return ResponseEntity.notFound().build();
			}

			existingEmployee.setEmployeeName(employee.getEmployeeName());
			existingEmployee.setAadharNo(employee.getAadharNo());
			existingEmployee.setMobileNumber(employee.getMobileNumber());
			existingEmployee.setLocation(employee.getLocation());
			existingEmployee.setAddress(employee.getAddress());
			existingEmployee.setMaintanenceId(employee.getMaintanenceId());
			existingEmployee.setGender(employee.getGender());

			employeeService.saveEmployee(existingEmployee);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "employee data updated successfully");
			return ResponseEntity.ok(bookingMap);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/employee/view")
	public List<Map<String, Object>> employeeList(@RequestParam(required = true) String view) {
		try {
			if ("employee".equals(view)) {
				return employeeRepository.getAllEmployeeAndMaintanence();
			} else {
				throw new IllegalArgumentException("The provided employeeleave is not supported.");
			}
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@GetMapping("/employee/view/{employee_id}")
	public Map<String, Object> getAllMemberDetailsByMemberId1(@PathVariable Long employee_id) {
		return employeeRepository.AllEmployeeID(employee_id);
	}
}
