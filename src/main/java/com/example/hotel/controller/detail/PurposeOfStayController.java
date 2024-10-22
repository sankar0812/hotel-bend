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

import com.example.hotel.entity.detail.PurposeOfStay;
import com.example.hotel.service.detail.PurposeOfStayService;

@RestController
@CrossOrigin
public class PurposeOfStayController {

	@Autowired
	private PurposeOfStayService purposeOfStayService;

	@GetMapping("/purposeOfStay/view")
	public ResponseEntity<?> getPurposeOfStayDetails(@RequestParam(required = true) String purposeOfStay) {
		try {
			if ("purposeOfStayDetails".equals(purposeOfStay)) {
				Iterable<PurposeOfStay> purposeOfStayDetails = purposeOfStayService.listAll();
				return new ResponseEntity<>(purposeOfStayDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided purposeOfStay is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving purposeOfStay details: " + e.getMessage());
		}
	}

	@PostMapping("/purposeOfStay/save")
	public ResponseEntity<?> savePurposeOfStayDetails(@RequestBody PurposeOfStay purposeOfStay) {
		try {
			purposeOfStayService.SaveStayDetails(purposeOfStay);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "purpose data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving purposeOfStay: " + e.getMessage());
		}
	}

	@PutMapping("/purposeOfStay/edit/{id}")
	public ResponseEntity<?> updatePurposeOfStay(@PathVariable("id") Long purposeOfStayId,
			@RequestBody PurposeOfStay purposeOfStay) {
		try {
			PurposeOfStay existingPurPose = purposeOfStayService.findById(purposeOfStayId);

			if (existingPurPose == null) {
				return ResponseEntity.notFound().build();
			}
			existingPurPose.setPurpose(purposeOfStay.getPurpose());

			purposeOfStayService.SaveStayDetails(existingPurPose);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "purpose data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/purpose/delete/{purposeOfStayId}")
	public ResponseEntity<?> deleteFloorId(@PathVariable("purposeOfStayId") Long purposeOfStayId) {
		purposeOfStayService.deletePurposeOfStayId(purposeOfStayId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "purpose data deleted successfully");
		return ResponseEntity.ok(bookingMap);
	}
}
