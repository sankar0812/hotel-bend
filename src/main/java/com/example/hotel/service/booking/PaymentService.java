package com.example.hotel.service.booking;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.booking.Payment;
import com.example.hotel.repository.booking.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	public List<Payment> paymentList() {
		return this.paymentRepository.findAll();
	}

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	public Payment findByPaymentId(Long paymentId) {
		return paymentRepository.findById(paymentId).get();
	}

	public void deletePaymentById(Long id) {
		paymentRepository.deleteById(id);
	}
	
	public List<Map<String, Object>> getAllPaymentDetails(){
		return paymentRepository.getAllPaymentDetails();
	}
	
	public List<Map<String, Object>> getPaymentDetail(){
		return paymentRepository.getPaymentDetails();
	}
}
