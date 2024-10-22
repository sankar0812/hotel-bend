package com.example.hotel.controller.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.entity.login.LoginRequest;


@CrossOrigin
@RestController
public class AdminLoginController {

	@Value("${app.email}")
	private String email;

	@Value("${app.password}")
	private String password;

	@PostMapping("/admin/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		System.out.println("email from properties file: " + email);
		System.out.println("password from properties file: " + password);
		System.out.println("email from login request: " + loginRequest.getEmail());
		System.out.println("password from login request: " + loginRequest.getPassword());

		if (loginRequest != null && loginRequest.getEmail() != null && loginRequest.getEmail().equals(email)
				&& loginRequest.getPassword().equals(password)) {
			Map<String, Object> responseData = new HashMap<>();
			responseData.put("token",
					"2wCEAAkGBwgHBgkIBwgKCZCSAcgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			responseData.put("email", email);
			responseData.put("login_status", "success");
			return ResponseEntity.ok(responseData);
		} else {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Email and password are incorrect");
			errorResponse.put("login_status", "Failed");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}

}
