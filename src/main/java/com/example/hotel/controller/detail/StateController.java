package com.example.hotel.controller.detail;

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

import com.example.hotel.entity.detail.State;
import com.example.hotel.service.detail.StateService;

@RestController
@CrossOrigin
public class StateController {

	@Autowired
	private StateService stateService;

	@GetMapping("/state/view")
	public ResponseEntity<?> getStateDetails(@RequestParam(required = true) String state) {
		try {
			if ("stateDetails".equals(state)) {
				Iterable<State> stateDetails = stateService.listAll();
				return new ResponseEntity<>(stateDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided state is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving state details: " + e.getMessage());
		}
	}

	@PostMapping("/state/save")
	public ResponseEntity<?> saveStateDetails(@RequestBody State state) {
		try {
			stateService.SaveStateDetails(state);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "state data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving state: " + e.getMessage());
		}
	}

	@PutMapping("/state/edit/{id}")
	public ResponseEntity<?> updateState(@PathVariable("id") Long stateId, @RequestBody State state) {
		try {
			State existingState = stateService.findById(stateId);

			if (existingState == null) {
				return ResponseEntity.notFound().build();
			}
			existingState.setStateName(state.getStateName());

			stateService.SaveStateDetails(existingState);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "state data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/state/delete/{stateId}")
	public ResponseEntity<?> deleteFloorId(@PathVariable("stateId") Long stateId) {
		stateService.deleteStateId(stateId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "state data deleted successfully");
		return ResponseEntity.ok(bookingMap);
	}
}
