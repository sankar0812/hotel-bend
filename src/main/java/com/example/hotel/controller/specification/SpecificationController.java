package com.example.hotel.controller.specification;

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

import com.example.hotel.entity.specification.Specification;
import com.example.hotel.service.specification.SpecificationService;

@RestController
@CrossOrigin
public class SpecificationController {

	@Autowired
	private SpecificationService specificationService;

	@GetMapping("/specification/view")
	public ResponseEntity<?> getSpecificationDetails(@RequestParam(required = true) String specification) {
		try {
			if ("specificationDetails".equals(specification)) {
				Iterable<Specification> specificationDetails = specificationService.listAll();
				return new ResponseEntity<>(specificationDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided specification is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving specification details: " + e.getMessage());
		}
	}

	@PostMapping("/specification/save")
	public ResponseEntity<?> saveSpecificationDetails(@RequestBody Specification specification) {
		try {
			specificationService.SaveSpecificationDetails(specification);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "specification data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving specification: " + e.getMessage());
		}
	}

	@PutMapping("/specification/edit/{id}")
	public ResponseEntity<?> updateSpecification(@PathVariable("id") Long specificationId,
			@RequestBody Specification specification) {
		try {
			Specification existingSpecification = specificationService.findById(specificationId);

			if (existingSpecification == null) {
				return ResponseEntity.notFound().build();
			}
			existingSpecification.setSpecificationName(specification.getSpecificationName());

			specificationService.SaveSpecificationDetails(existingSpecification);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "specification data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/specification/delete/{specificationId}")
	public ResponseEntity<?> deleteSpecificationId(@PathVariable("specificationId") Long specificationId) {
		specificationService.deleteSpecificationId(specificationId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "specification data deleted successfully");
		return ResponseEntity.ok(bookingMap);
	}
}
