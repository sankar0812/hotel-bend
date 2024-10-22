package com.example.hotel.controller.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.hotel.entity.customer.CustomerRegistration;
import com.example.hotel.entity.login.LoginRequest;
import com.example.hotel.repository.customer.CustomerRegistrationRepository;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@PostMapping("/login")
	public ResponseEntity<?> processLogin(@RequestBody LoginRequest loginRequest) {

		String email = loginRequest.getEmail();
		String mobileNumber = loginRequest.getMobileNumber();

		CustomerRegistration customerRegistration = customerRegistrationRepository.findByEmail(email);

		if (customerRegistration != null && customerRegistration.getEmail().equals(email)
				&& customerRegistration.getMobileNumber().equals(mobileNumber)) {
			Map<String, Object> userDetails = getCustomerDetails(customerRegistration);
			return ResponseEntity.ok(userDetails);
		} else {
			Map<String, Object> loginFailedResponse = createLoginFailedResponse();
			return ResponseEntity.badRequest().body(loginFailedResponse);
		}
	}

	private Map<String, Object> createLoginFailedResponse() {
		Map<String, Object> adminMap = new HashMap<>();
		adminMap.put("result", "Login Failed");
		return adminMap;
	}

	private Map<String, Object> getCustomerDetails(CustomerRegistration customerRegistration) {
		List<Map<String, Object>> userDetails = customerRegistrationRepository
				.getLoginDetailsResponse(customerRegistration.getCustomerId());
		Map<String, List<Map<String, Object>>> customerGroupMap = userDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("customer_id").toString()));
		Map<String, Object> customerDetailsMap = new HashMap<>();

		for (Entry<String, List<Map<String, Object>>> customerLoop : customerGroupMap.entrySet()) {
			Map<String, Object> customerMap = new HashMap<>();
			customerMap.put("customerId", customerLoop.getKey());
			customerMap.put("name", customerLoop.getValue().get(0).get("name"));
			customerMap.put("mobileNumber", customerLoop.getValue().get(0).get("mobile_number"));
			customerMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			customerDetailsMap.putAll(customerMap);
		}

		return customerDetailsMap;
	}
}
