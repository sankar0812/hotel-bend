package com.example.hotel.controller.roomDetails;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import com.example.hotel.entity.roomDetails.AddRooms;
import com.example.hotel.entity.roomDetails.RoomMaintenance;
import com.example.hotel.service.roomDetails.AddRoomsService;
import com.example.hotel.service.roomDetails.RoomMaintenanceService;

@RestController
@CrossOrigin
public class RoomMaintenanceController {

	@Autowired
	private RoomMaintenanceService roomMaintenanceService;

	@Autowired
	private AddRoomsService addRoomsService;

	@GetMapping("/roomMaintenance/view")
	public ResponseEntity<?> getRoomMaintenanceDetails(@RequestParam(required = true) String roomMaintenance) {
		try {
			if ("roomMaintenanceDetails".equals(roomMaintenance)) {
				Iterable<RoomMaintenance> roomMaintenanceDetails = roomMaintenanceService.listAll();
				return new ResponseEntity<>(roomMaintenanceDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided roomMaintenance is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving roomMaintenance details: " + e.getMessage());
		}
	}

	@PostMapping("/roomMaintenance/save")
	public ResponseEntity<?> saveRoomMaintenanceDetail(@RequestBody RoomMaintenance roomMaintenance) {
		try {
			roomMaintenance.setCompleted(false);

			roomMaintenanceService.SaveRoomMaintenanceDetails(roomMaintenance);

			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "room maintanence data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving roomMaintenance: " + e.getMessage());
		}
	}

	@PutMapping("/roomMaintenance/available/status/{id}")
	public ResponseEntity<?> saveRoomMaintenanceDetails(@PathVariable("id") Long roomMaintenanceId,
			@RequestBody RoomMaintenance roomMaintenance) {

		try {
			RoomMaintenance existingMaintenance = roomMaintenanceService.findById(roomMaintenanceId);

			if (existingMaintenance == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingMaintenance.isCompleted()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "room maintanence already completed");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}
			existingMaintenance.setNoOfDays(roomMaintenance.getNoOfDays());
			existingMaintenance.setRoomStatus(roomMaintenance.getRoomStatus());

			if (roomMaintenance.getRoomStatus().equals("completed")) {
				existingMaintenance.setCompleted(true);
				existingMaintenance.setEndDate(new Date(System.currentTimeMillis()));
				existingMaintenance.setEndTime(new Time(System.currentTimeMillis()));

			}

			roomMaintenanceService.SaveRoomMaintenanceDetails(existingMaintenance);

			if (existingMaintenance.getRoomStatus().equals("completed")) {
				long roomId = existingMaintenance.getRoomId();

				Optional<AddRooms> addRoomsList = addRoomsService.findRoomOptionalById(roomId);
				if (addRoomsList.isPresent()) {
					AddRooms addRoom = addRoomsList.get();
					addRoom.setAvailable(true);
					addRoom.setBooking(false);
					addRoom.setCleaning(true);
					addRoom.setMaintanence(false);
					addRoom.setVacate(false);
					addRoom.setStatusType("available");

					addRoomsService.saveRooms(addRoom);
				}

			}

			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "available status updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/roomMaintenance/edit/{id}")
	public ResponseEntity<?> updateRoomMaintenance(@PathVariable("id") Long roomMaintenanceId,
			@RequestBody RoomMaintenance roomMaintenance) {
		try {
			RoomMaintenance existingRoomMaintenance = roomMaintenanceService.findById(roomMaintenanceId);

			if (existingRoomMaintenance == null) {
				return ResponseEntity.notFound().build();
			}
			existingRoomMaintenance.setEmployeeId(roomMaintenance.getEmployeeId());
			existingRoomMaintenance.setSpecificationId(roomMaintenance.getSpecificationId());
			existingRoomMaintenance.setWorkStartDate(roomMaintenance.getWorkStartDate());
			existingRoomMaintenance.setWorkstartTime(roomMaintenance.getWorkstartTime());
			existingRoomMaintenance.setRoomStatus(roomMaintenance.getRoomStatus());

			roomMaintenanceService.SaveRoomMaintenanceDetails(existingRoomMaintenance);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "room maintanence updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@GetMapping("/roomMaintenance/detail/view")
	public ResponseEntity<?> getAllRoomMaintenanceDetails(@RequestParam(required = true) String roomMaintenance) {
		try {
			if ("roomMaintenanceDetails".equals(roomMaintenance)) {
				Iterable<Map<String, Object>> roomMaintenanceDetails = roomMaintenanceService
						.getRoomMaintenanceDetails();
				return new ResponseEntity<>(roomMaintenanceDetails, HttpStatus.OK);

			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided roomMaintenance is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving roomMaintenance details: " + e.getMessage());
		}
	}


}
