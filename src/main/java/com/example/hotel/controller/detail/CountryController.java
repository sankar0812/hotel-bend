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

import com.example.hotel.entity.detail.Country;
import com.example.hotel.service.detail.CountryService;

@RestController
@CrossOrigin
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	@GetMapping("/country/view")
	public ResponseEntity<?> getCountryDetails(@RequestParam(required = true) String country) {
	    try {
	        if ("countryDetails".equals(country)) {
	            Iterable<Country> countryDetails = countryService.listAll();
	            return new ResponseEntity<>(countryDetails, HttpStatus.OK);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided country is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving country details: " + e.getMessage());
	    }
	}
	
	@PostMapping("/country/save")
	public ResponseEntity<?> saveCountryDetails(@RequestBody Country country) {
		try {
			countryService.SaveCountryDetails(country);
			 Map<String, Object> bookingMap = new HashMap<>();
			    bookingMap.put("message", "country data saved successfully");
			    return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving country: " + e.getMessage());
		}
	}
	
	@PutMapping("/country/edit/{id}")
	public ResponseEntity<?> updateCountry(@PathVariable("id") Long countryId, @RequestBody Country country) {
		try {
			Country existingCountry= countryService.findById(countryId);

			if (existingCountry == null) {
				return ResponseEntity.notFound().build();
			}
			existingCountry.setCountryName(country.getCountryName());
		
			countryService.SaveCountryDetails(existingCountry);
			 Map<String, Object> bookingMap = new HashMap<>();
			    bookingMap.put("message", "country data updated successfully");
			    return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/country/delete/{countryId}")
	public ResponseEntity<?> deleteCountryId(@PathVariable("countryId") Long countryId) {
		countryService.deleteCountryId(countryId);
		 Map<String, Object> bookingMap = new HashMap<>();
		    bookingMap.put("message", "country data deleted successfully");
		    return ResponseEntity.ok(bookingMap);
	}
}
