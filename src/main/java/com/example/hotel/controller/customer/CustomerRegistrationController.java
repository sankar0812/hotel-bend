package com.example.hotel.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.example.hotel.entity.customer.CustomerRegistration;
import com.example.hotel.entity.customer.Verification;
import com.example.hotel.repository.customer.CustomerRegistrationRepository;
import com.example.hotel.repository.customer.VerificationRepository;
import com.example.hotel.service.customer.CustomerRegistrationService;

@RestController
@CrossOrigin
public class CustomerRegistrationController {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;
	@Autowired
	private VerificationRepository verificationRepository;

	@PostMapping("/customer/registration/save")
	public ResponseEntity<?> addEmployeeWithImage(@RequestBody CustomerRegistration customerRegistration)
			throws SQLException, IOException {
		String email = customerRegistration.getEmail();

		if (customerRegistrationService.isEmailExists(email)) {
			Map<String, Object> errorEmail = new HashMap<>();
			errorEmail.put("message", "E-Mail already exists");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorEmail);
		}

		customerRegistrationService.saveCustomer(customerRegistration);
		Map<String, Object> customerMap = new HashMap<>();
		customerMap.put("message", "customer data saved successfully");
		return ResponseEntity.ok(customerMap);
	}


	@GetMapping("customer/registration/{randomNumber}/{id:.+}")
	public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id) {
		String[] parts = id.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];

		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build();
		}

		Optional<Verification> image = verificationRepository.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.get().getImage().getBytes(1, (int) image.get().getImage().length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {

			headers.setContentType(MediaType.IMAGE_JPEG);
		}

		return ResponseEntity.ok().headers(headers).body(resource);
	}


	@PutMapping("/customer/registration/edit/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable("id") Long customerId,
			@RequestBody CustomerRegistration customerRegistration) {
		try {
			CustomerRegistration existingCustomerRegistration = customerRegistrationService
					.findCustomerById(customerId);
			if (existingCustomerRegistration == null) {
				return ResponseEntity.notFound().build();
			}

			existingCustomerRegistration.setAddress(customerRegistration.getAddress());
			existingCustomerRegistration.setName(customerRegistration.getName());
			existingCustomerRegistration.setCountryId(customerRegistration.getCountryId());
			existingCustomerRegistration.setGender(customerRegistration.getGender());
			existingCustomerRegistration.setMobileNumber(customerRegistration.getMobileNumber());
			existingCustomerRegistration.setStateId(customerRegistration.getStateId());
			existingCustomerRegistration.setVerificationType(customerRegistration.getVerificationType());
			existingCustomerRegistration.setNumber(customerRegistration.getNumber());
			existingCustomerRegistration.setCityId(customerRegistration.getCityId());
			existingCustomerRegistration.setVerificationType(customerRegistration.getVerificationType());


			customerRegistrationService.saveCustomer(existingCustomerRegistration);

			Map<String, Object> customerMap = new HashMap<>();
			customerMap.put("message", "customer updated data saved successfully");
			return ResponseEntity.ok(customerMap);

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/customer/registration/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long customerId) {
		customerRegistrationService.deleteCustomerById(customerId);
		Map<String, Object> customerMap = new HashMap<>();
		customerMap.put("message", "customer data deleted successfully");
		return ResponseEntity.ok(customerMap);
	}


	@GetMapping("/customer/registration/view")
	public ResponseEntity<?> getCustomerDetails(@RequestParam(required = true) String registration) {
		if ("customer".equals(registration)) {
			List<CustomerRegistration> customerList = customerRegistrationService.customerList();
	        return ResponseEntity.ok(customerList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}



	/////////////////////updated details///////////
	@GetMapping("/customer/view")
	public ResponseEntity<?> getAllCustomerDetails(@RequestParam(required = true) String customer) {
	    try {
	        if ("customerDetails".equals(customer)) {
	            Iterable<Map<String, Object>>customerDetails = customerRegistrationRepository.getCustomerDetails();
	            return new ResponseEntity<>(customerDetails, HttpStatus.OK);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided customer is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving customer details: " + e.getMessage());
	    }
	}
	
	
}
