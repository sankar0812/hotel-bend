package com.example.hotel.service.customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hotel.entity.customer.CustomerRegistration;
import com.example.hotel.entity.customer.Verification;
import com.example.hotel.repository.customer.CustomerRegistrationRepository;
import com.example.hotel.repository.customer.VerificationRepository;

@Service
public class CustomerRegistrationService {

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Autowired
	private VerificationRepository verificationRepository;

	public List<CustomerRegistration> customerList() {
		return customerRegistrationRepository.findAllByOrderByCustomerIdDesc();
	}

	public void saveCustomer(CustomerRegistration user) {
		this.customerRegistrationRepository.save(user);
	}

//	public CustomerRegistration findCustomerById(Long id) {
//		return customerRegistrationRepository.findById(id).get();
//	}

	public CustomerRegistration findCustomerById(Long customerId) {
		Optional<CustomerRegistration> optionalCustomer = customerRegistrationRepository.findById(customerId);
		return optionalCustomer.orElse(null);
	}

	public void deleteCustomerById(Long customerId) {
		customerRegistrationRepository.deleteById(customerId);
	}

	public boolean isEmailExists(String email) {
		CustomerRegistration existingCustomer = customerRegistrationRepository.findByEmail(email);
		return existingCustomer != null;
	}

	public List<Map<String, Object>> getAllCustomerDetails() {
		return customerRegistrationRepository.getAllCustomerDetails();
	}

	public List<Map<String, Object>> getAllCustomerDetailsWithId(Long customerId) {
		return customerRegistrationRepository.getAllCustomerDetailsWithId(customerId);
	}

	public Verification findVerificationById(Long id) {
		return verificationRepository.findById(id).get();
	}

	public void saveVerification(Verification verification) {
		this.verificationRepository.save(verification);
	}
}
