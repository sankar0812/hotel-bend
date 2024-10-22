package com.example.hotel.repository.employee;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel.entity.employee.Employee;

public interface EmployeeRepository  extends JpaRepository<Employee, Long>{
	
	
	 @Query(value=
		     "  select e.employee_id as employeeId,e.aadhar_no as aadharNo,e.address,e.employee_name as employeeName,e.gender as gender,"
		     + "e.maintanence_id as maintanenceId,m.maintanence_name as maintanenceName,e.location,e.mobile_number as mobileNumber"
		     + " from employee as e"
		     + " join maintanence as m on m.maintanence_id=e.maintanence_id",nativeQuery = true)
	List<Map<String, Object>> getAllEmployeeAndMaintanence();
	 
	 @Query(value=
		     "  select e.employee_id as employeeId,e.aadhar_no as aadharNo,e.address,e.employee_name as employeeName,e.gender as gender"
		     + "e.maintanence_id as maintanenceId,m.maintanence_name as maintanenceName,e.location,e.mobile_number as mobileNumber"
		     + " from employee as e"
		     + " join maintanence as m on m.maintanence_id=e.maintanence_id"
		     + " where e.employee_id =:employeeId ",nativeQuery = true)
	Map<String, Object> AllEmployeeID(@Param("employeeId") Long employeeId);

}
