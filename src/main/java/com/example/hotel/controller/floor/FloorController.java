package com.example.hotel.controller.floor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.entity.floor.Floor;
import com.example.hotel.service.floor.FloorService;

@RestController
@CrossOrigin
public class FloorController {

	@Autowired
	private FloorService floorService;

	@GetMapping("/floor/view")
	public ResponseEntity<?> getFloorDetails(@RequestParam(required = true) String floor) {
		try {
			if ("floorDetails".equals(floor)) {
				Iterable<Floor> floorDetails = floorService.listAll();
				return new ResponseEntity<>(floorDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided floor is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving floor details: " + e.getMessage());
		}
	}

	@PostMapping("/floor/save")
	public ResponseEntity<?> saveFloorDetails(@RequestBody Floor floor) {
		try {
			floorService.SaveFloorDetails(floor);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "floor data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving floor: " + e.getMessage());
		}
	}

	@PutMapping("/floor/edit/{id}")
	public ResponseEntity<?> updateFloor(@PathVariable("id") Long floorId, @RequestBody Floor floor) {
		try {
			Floor existingFloor = floorService.findById(floorId);

			if (existingFloor == null) {
				return ResponseEntity.notFound().build();
			}
			existingFloor.setFloorName(floor.getFloorName());

			floorService.SaveFloorDetails(existingFloor);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "floor data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/floor/delete/{floorId}")
	public ResponseEntity<?> deleteFloorId(@PathVariable("floorId") Long floorId) {
		floorService.deleteFloorId(floorId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "floor data deleted successfully");
		return ResponseEntity.ok(bookingMap);
	}
}
