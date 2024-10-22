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

import com.example.hotel.entity.detail.City;
import com.example.hotel.service.detail.CityService;

@RestController
@CrossOrigin
public class CityController {

	@Autowired
	private CityService cityService;

	@GetMapping("/city/view")
	public ResponseEntity<?> getCityDetails(@RequestParam(required = true) String city) {
		try {
			if ("cityDetails".equals(city)) {
				Iterable<City> cityDetails = cityService.listAll();
				return new ResponseEntity<>(cityDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided city is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving city details: " + e.getMessage());
		}
	}

	@PostMapping("/city/save")
	public ResponseEntity<?> saveCityDetails(@RequestBody City city) {
		try {
			cityService.SaveCityDetails(city);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "City data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving city: " + e.getMessage());
		}
	}

	@PutMapping("/city/edit/{id}")
	public ResponseEntity<?> updateCity(@PathVariable("id") Long cityId, @RequestBody City city) {
		try {
			City existingCity = cityService.findById(cityId);

			if (existingCity == null) {
				return ResponseEntity.notFound().build();
			}
			existingCity.setCityName(city.getCityName());

			cityService.SaveCityDetails(existingCity);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "city data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/city/delete/{cityId}")
	public ResponseEntity<?> deleteCityId(@PathVariable("cityId") Long cityId) {
		cityService.deleteCityId(cityId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "city data deleted successfully");
		return ResponseEntity.ok(bookingMap);

	}
}
