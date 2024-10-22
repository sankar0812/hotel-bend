package com.example.hotel.repository.roomDetails;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hotel.entity.roomDetails.RoomMaintenance;

public interface RoomMaintenanceRepository extends JpaRepository<RoomMaintenance, Long> {

	@Query(value="select r.room_maintenance_id as roomMaintenanceId,r.completed,r.employee_id as employeeId,r.end_date as endDate,r.end_time as endTime,s.specification_id as specificationId, r.work_start_date as workStartDate,r.workstart_time as workstartTime,"
			+ " r.no_of_days as noOfDays,r.room_id as roomId,r.room_status as roomStatus,r.start_date as startDate,r.start_time as startTime,s.specification_name as specificationName,"
			+ " e.aadhar_no as aadharNo,e.address,e.employee_name as employeeName,e.location,e.mobile_number as mobileNumber,a.amount,"
			+ " a.category_id as categoryId,a.floor_id as floorId,a.gst_percentage as gstPercentage,a.room_no as roomNo,a.size,a.total_amount as totalAmount,a.no_of_beds as noOfBeds,f.floor_name as floorName,c.category_name as categoryName"
			+ " from room_maintenance as r"
			+ " left join employee as e on e.employee_id=r.employee_id"
			+ " left join specification as s on s.specification_id=r.specification_id"
			+ " join add_rooms as a on a.room_id=r.room_id"
			+ " join floor as f on f.floor_id=a.floor_id"
			+ " join category as c on c.category_id=a.category_id", nativeQuery = true)
	List<Map<String, Object>>getRoomMaintenanceDetails(); 
}
