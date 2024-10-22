package com.example.hotel.repository.roomDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.hotel.entity.roomDetails.AddRooms;

public interface AddRoomsRepository extends JpaRepository<AddRooms, Long> {

	@Query(value = "select ad.*,rl.room_list_id,sl.specification_id,s.specification_name,rl.image_type,rl.deleted as roomListDeleted, sl.deleted as specificationListDeleted,f.floor_name,c.category_name,sl.specification_list_id "
			+ " from add_rooms as ad" + " join rooms_list as rl on rl.room_id = ad.room_id"
			+ " join specification_list as sl on sl.room_id = ad.room_id"
			+ " join specification as s on sl.specification_id = s.specification_id"
			+ " join category as c on c.category_id = ad.category_id" + " join floor as f on f.floor_id = ad.floor_id"
			+ " where rl.deleted = false and sl.deleted = false", nativeQuery = true)
	List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationList();

	@Query(value = "select ad.*,rl.room_list_id,sl.specification_id,s.specification_name,rl.image_type,rl.deleted as roomListDeleted, sl.deleted as specificationListDeleted,f.floor_name,c.category_name,sl.specification_list_id "
			+ " from add_rooms as ad" + " join rooms_list as rl on rl.room_id = ad.room_id"
			+ " join specification_list as sl on sl.room_id = ad.room_id"
			+ " join specification as s on sl.specification_id = s.specification_id"
			+ " join category as c on c.category_id = ad.category_id" + " join floor as f on f.floor_id = ad.floor_id"
			+ " where rl.deleted = false and sl.deleted = false and ad.available = true", nativeQuery = true)
	List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithAvailableTrue();

	@Query(value = "select ad.*,rl.room_list_id,sl.specification_id,rl.image_type,s.specification_name,rl.deleted as roomListDeleted, sl.deleted as specificationListDeleted,f.floor_name,c.category_name,sl.specification_list_id "
			+ " from add_rooms as ad" + " join rooms_list as rl on rl.room_id = ad.room_id"
			+ " join specification_list as sl on sl.room_id = ad.room_id"
			+ " join specification as s on sl.specification_id = s.specification_id"
			+ " join category as c on c.category_id = ad.category_id" + " join floor as f on f.floor_id = ad.floor_id"
			+ " where rl.deleted = false and sl.deleted = false and ad.room_id = :room_id and ad.available = true", nativeQuery = true)
	List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithAvailableTrueWithId(Long room_id);

	@Query(value = "select ad.*,rl.room_list_id,sl.specification_id,rl.image_type,s.specification_name,rl.deleted as roomListDeleted, sl.deleted as specificationListDeleted,f.floor_name,c.category_name,sl.specification_list_id "
			+ " from add_rooms as ad" + " join rooms_list as rl on rl.room_id = ad.room_id"
			+ " join specification_list as sl on sl.room_id = ad.room_id"
			+ " join specification as s on sl.specification_id = s.specification_id"
			+ " join category as c on c.category_id = ad.category_id" + " join floor as f on f.floor_id = ad.floor_id"
			+ " where rl.deleted = false and sl.deleted = false and ad.room_id = :room_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithId(Long room_id);

	@Query(value = "select ad.*,rl.room_list_id,sl.specification_id,rl.image_type,s.specification_name,rl.deleted as roomListDeleted, sl.deleted as specificationListDeleted,f.floor_name,c.category_name,sl.specification_list_id "
			+ " from add_rooms as ad" + " join rooms_list as rl on rl.room_id = ad.room_id"
			+ " join specification_list as sl on sl.room_id = ad.room_id"
			+ " join specification as s on sl.specification_id = s.specification_id"
			+ " join category as c on c.category_id = ad.category_id" + " join floor as f on f.floor_id = ad.floor_id"
			+ " where rl.deleted = false and sl.deleted = false and ad.floor_id = :floor_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithFloorId(Long floor_id);

	AddRooms findByRoomNo(Integer roomNo);

	Optional<AddRooms> findByRoomNo(int roomNo);

	@Query(value = " SELECT"
			+ " (SELECT COUNT(customer_id) FROM customer_registration) AS totalCustomers,"
			+ " (SELECT COUNT(employee_id) FROM employee) AS totalEmployees,"
			+ " round(((SELECT COUNT(*) FROM customer_registration WHERE gender = 'Male') * 100.0 / (SELECT COUNT(*) FROM customer_registration))) AS customerMalePercentage,"
			+ " round(((SELECT COUNT(*) FROM customer_registration WHERE gender = 'Female') * 100.0 / (SELECT COUNT(*) FROM customer_registration))) AS customerFemalePercentage,"
			+ " (SELECT COUNT(room_id) FROM add_rooms) AS totalRooms,"
			+ " (SELECT COUNT(*) FROM add_rooms WHERE available = true) AS availableRooms,"
			+ " (SELECT COUNT(*) FROM add_rooms WHERE booking = true) AS bookedRooms,"
			+ " (SELECT COUNT(*) FROM add_rooms WHERE maintanence = true) AS maintenanceRooms", nativeQuery = true)
	Map<String, Object> getdashboardPageDetails();
	
	@Query(value="SELECT"
			+ " round(((SELECT COUNT(*) FROM employee WHERE gender = 'Male') * 100.0 / (SELECT COUNT(*) FROM employee))) AS malePercentage,"
			+ " round(((SELECT COUNT(*) FROM employee WHERE gender = 'Female') * 100.0 / (SELECT COUNT(*) FROM employee))) AS femalePercentage,"
			+ " round(((SELECT COUNT(*) FROM employee WHERE gender = 'Others') * 100.0 / (SELECT COUNT(*) FROM employee))) AS otherPercentage", nativeQuery = true)
	Map<String, Object> getAllPercentageDetails();

}
