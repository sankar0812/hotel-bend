package com.example.hotel.controller.employee;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

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
import com.example.hotel.entity.employee.Maintanence;
import com.example.hotel.service.employee.MaintanenceService;

@RestController
@CrossOrigin
public class MaintanenceController {

	@Autowired
	private MaintanenceService maintanenceService;

	@PostMapping("/maintanence/save")
	public ResponseEntity<?> addMaintanence(@RequestBody Maintanence maintanence) {
		maintanenceService.saveMaintanence(maintanence);
		Map<String, Object> maintanenceMap = new HashMap<>();
		maintanenceMap.put("message", "Maintanence data saved successfully");
		return ResponseEntity.ok(maintanenceMap);
	}

	@GetMapping("/maintanence/view")
	public ResponseEntity<?> viewMaintanence(@RequestParam(required = true) String maintanence) {
		if ("employee".equals(maintanence)) {
			return ResponseEntity.ok(maintanenceService.maintanenceList());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PutMapping("/mainatnence/edit/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable("id") Long maintanenceId,
			@RequestBody Maintanence maintanence) {
		try {
			Maintanence existingMaintanence = maintanenceService.findMaintanenceById(maintanenceId);
			if (existingMaintanence == null) {
				return ResponseEntity.notFound().build();
			}

			existingMaintanence.setMaintanenceName(maintanence.getMaintanenceName());

			maintanenceService.saveMaintanence(existingMaintanence);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "maintanence data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/maintanence/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long maintanence) {
		maintanenceService.deleteMaintanenceById(maintanence);
		Map<String, Object> maintanenceMap = new HashMap<>();
		maintanenceMap.put("message", "maintanence data deleted successfully");
		return ResponseEntity.ok(maintanenceMap);
	}

}
