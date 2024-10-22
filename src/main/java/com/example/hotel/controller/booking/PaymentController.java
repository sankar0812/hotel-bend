package com.example.hotel.controller.booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hotel.entity.booking.Booking;
import com.example.hotel.entity.booking.Payment;
import com.example.hotel.repository.booking.PaymentRepository;
import com.example.hotel.service.booking.BookingService;
import com.example.hotel.service.booking.PaymentService;

@RestController
@CrossOrigin
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PaymentRepository paymentRepository;
	

	@PostMapping("/payment/save")
	public ResponseEntity<?> savePayment(@RequestBody Payment payment) {
		try {

			double givenAmount = payment.getGivenAmount();
			double balance = payment.getBalance();
			payment.setGivenAmount(givenAmount);
			double updatedBalance = (balance - givenAmount);
			payment.setBalance(updatedBalance);
			paymentService.savePayment(payment);

			Booking booking = bookingService.findBookingById(payment.getBookingId());
			double newBalance = booking.getBalance() - givenAmount;
			double newGivenAmount = booking.getGivenAmount() + givenAmount;
			booking.setBalance(newBalance);
			booking.setGivenAmount(newGivenAmount);
			bookingService.saveBooking(booking);

			if (newBalance == 0) {
				Map<String, Object> bookingMap = new HashMap<>();
				bookingMap.put("message", "payment data saved successfully -- Now Balance is 0");
				return ResponseEntity.ok(bookingMap);
			} else {
				Map<String, Object> bookingMap = new HashMap<>();
				bookingMap.put("message", "payment data saved successfully: " + newBalance);
				return ResponseEntity.ok(bookingMap);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving payment: " + e.getMessage());
		}
		
	}

	@PutMapping("/payment/edit/{id}")
	public ResponseEntity<?> updatePayment(@PathVariable("id") Long paymentId, @RequestBody Payment payment) {
		try {
			Payment existingPayment = paymentService.findByPaymentId(paymentId);

			if (existingPayment == null) {
				return ResponseEntity.notFound().build();
			}

			double existingGivenAmount = existingPayment.getGivenAmount();
			double updatedGivenAmount = payment.getGivenAmount();
			double balance = existingPayment.getBalance();

			if (updatedGivenAmount < existingGivenAmount) {
				balance += (existingGivenAmount - updatedGivenAmount);
			} else if (updatedGivenAmount > existingGivenAmount) {
				balance -= (updatedGivenAmount - existingGivenAmount);
			} else {
				return ResponseEntity.ok().body("No changes made. The given amount is the same as the existing value.");
			}

			existingPayment.setGivenAmount(updatedGivenAmount);
			existingPayment.setBalance(balance);
			paymentService.savePayment(existingPayment);

			Booking booking = bookingService.findBookingById(existingPayment.getBookingId());
			double bookingBalance = booking.getBalance();

			if (updatedGivenAmount < existingGivenAmount) {
				bookingBalance += (existingGivenAmount - updatedGivenAmount);
			} else if (updatedGivenAmount > existingGivenAmount) {
				bookingBalance -= (updatedGivenAmount - existingGivenAmount);
			}

			booking.setBalance(bookingBalance);
			booking.setGivenAmount(booking.getGivenAmount() - existingGivenAmount + updatedGivenAmount);
			bookingService.saveBooking(booking);

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "Payment data updated successfully");
			return ResponseEntity.ok(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/payment/view/booking")
	public ResponseEntity<Object> getAllPaymentDetails(@RequestParam(required = true) String payment) {
		if ("booking".equals(payment)) {
			List<Map<String, Object>> paymentList = new ArrayList<>();
			List<Map<String, Object>> paymentRole = paymentService.getAllPaymentDetails();
			Map<String, List<Map<String, Object>>> paymentGroupMap = paymentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("customer_id").toString()));

			for (Entry<String, List<Map<String, Object>>> paymentLoop : paymentGroupMap.entrySet()) {
				Map<String, Object> paymentMap = new HashMap<>();
				paymentMap.put("customerId", Long.parseLong(paymentLoop.getKey()));
				paymentMap.put("name", paymentLoop.getValue().get(0).get("name"));
				paymentMap.put("email", paymentLoop.getValue().get(0).get("email"));
				List<Map<String, Object>> paymentSubList = new ArrayList<>();
				for (Map<String, Object> paymentSubLoop : paymentLoop.getValue()) {

					Map<String, Object> paymentSubMap = new HashMap<>();
					paymentSubMap.put("bookingId", paymentSubLoop.get("booking_id"));
					paymentSubMap.put("givenAmount", paymentSubLoop.get("given_amount"));
					paymentSubMap.put("totalAmount", paymentSubLoop.get("total_amount"));
					paymentSubMap.put("paymentId", paymentSubLoop.get("payment_id"));
					paymentSubMap.put("balance", paymentSubLoop.get("balance"));
					paymentSubMap.put("customerId", paymentSubLoop.get("customer_id"));
					paymentSubList.add(paymentSubMap);
				}
				paymentMap.put("bookingDetails", paymentSubList);
				paymentList.add(paymentMap);
			}

			return ResponseEntity.ok(paymentList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@GetMapping("/payment/view/customer/booking")
	public ResponseEntity<Object> getAllPaymentCustomerDetails(@RequestParam(required = true) String payment) {
		if ("booking".equals(payment)) {
			List<Map<String, Object>> paymentList = new ArrayList<>();
			List<Map<String, Object>> paymentRole = paymentService.getAllPaymentDetails();
			Map<String, Map<String, List<Map<String, Object>>>> paymentGroupMap = paymentRole.stream()
				    .collect(
				        Collectors.groupingBy(action -> action.get("customer_id").toString(),
				            Collectors.groupingBy(action -> action.get("booking_id").toString())
				        )
				    );

		
			for (Entry<String,Map<String, List<Map<String, Object>>>> paymentLoop : paymentGroupMap.entrySet()) {
				Map<String, Object> paymentMap = new HashMap<>();
				paymentMap.put("customerId", Long.parseLong(paymentLoop.getKey()));
				
				
				List<Map<String,Object>> bookingList = new ArrayList<>();
				for(Entry<String, List<Map<String, Object>>> bookingLoop : paymentLoop.getValue().entrySet()) {
					Map<String, Object> bookingMap = new HashMap<>();
					bookingMap.put("bookingId", bookingLoop.getKey());
					
					
					List<Map<String, Object>> paymentSubList = new ArrayList<>();
					for (Map<String, Object> paymentSubLoop : bookingLoop.getValue()) {

						Map<String, Object> paymentSubMap = new HashMap<>();
						paymentSubMap.put("bookingId", paymentSubLoop.get("booking_id"));
						paymentSubMap.put("givenAmount", paymentSubLoop.get("given_amount"));
						paymentSubMap.put("totalAmount", paymentSubLoop.get("total_amount"));
						paymentSubMap.put("paymentId", paymentSubLoop.get("payment_id"));
						paymentSubMap.put("balance", paymentSubLoop.get("balance"));
						paymentSubMap.put("customerId", paymentSubLoop.get("customer_id"));
						
						bookingMap.put("bookingDate", paymentSubLoop.get("booking_date"));
						
						paymentMap.put("name", paymentSubLoop.get("name"));
						paymentMap.put("email", paymentSubLoop.get("email"));
						paymentSubList.add(paymentSubMap);
					}
					
					bookingMap.put("paymentDetails", paymentSubList);
					bookingList.add(bookingMap);
				}
				
				paymentMap.put("bookingDetails", bookingList);
				paymentList.add(paymentMap);
			}

			return ResponseEntity.ok(paymentList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	
	
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000000);
    }
    
    

    @GetMapping("/payment/view")
    public ResponseEntity<Object> getAllPaymentDetails1(@RequestParam(required = true) String payment) {
        try {
            if ("paymentDetails".equals(payment)) {
                List<Map<String, Object>> paymentList = new ArrayList<>();
                List<Map<String, Object>> paymentRole = paymentRepository.getPaymentDetails();

                Map<String, List<Map<String, Object>>> paymentGroupMap = paymentRole.stream()
                        .collect(Collectors.groupingBy(action -> action.get("profile_id").toString()));

                for (Entry<String, List<Map<String, Object>>> paymentLoop : paymentGroupMap.entrySet()) {
                    Map<String, Object> paymentMap = new HashMap<>();
                    paymentMap.put("profileId", Long.parseLong(paymentLoop.getKey()));
                    paymentMap.put("address", paymentLoop.getValue().get(0).get("address"));
                    paymentMap.put("country", paymentLoop.getValue().get(0).get("country"));
                    paymentMap.put("email", paymentLoop.getValue().get(0).get("email"));
                    paymentMap.put("location", paymentLoop.getValue().get(0).get("location"));
                    paymentMap.put("phoneNumber", paymentLoop.getValue().get(0).get("phone_number"));
                    paymentMap.put("state", paymentLoop.getValue().get(0).get("state"));
                    paymentMap.put("pincode", paymentLoop.getValue().get(0).get("pincode"));
                    paymentMap.put("name", paymentLoop.getValue().get(0).get("businessName"));
                    List<Map<String, Object>> paymentSubList = new ArrayList<>();

                    for (Map<String, Object> paymentSubLoop : paymentLoop.getValue()) {
                        Map<String, Object> paymentSubMap = new HashMap<>();

                        paymentSubMap.put("paymentType", paymentSubLoop.get("payment_type"));
                        paymentSubMap.put("profileId", paymentSubLoop.get("profile_id"));
                        paymentSubMap.put("bookingId", paymentSubLoop.get("booking_id"));
                        paymentSubMap.put("givenAmount", paymentSubLoop.get("given_amount"));
                        paymentSubMap.put("totalAmount", paymentSubLoop.get("total_amount"));
                        paymentSubMap.put("paymentId", paymentSubLoop.get("payment_id"));
                        paymentSubMap.put("balance", paymentSubLoop.get("balance"));
                        paymentSubMap.put("customerId", paymentSubLoop.get("customer_id"));
                        paymentSubMap.put("name", paymentSubLoop.get("customerName"));
                        paymentSubMap.put("mobileNumber", paymentSubLoop.get("mobile_number"));
                        paymentSubMap.put("bookingDate", paymentSubLoop.get("booking_date"));
                      

                        int randomNumber = generateRandomNumber();
                        String fileExtension = getFileExtensionForImage(paymentSubLoop);
                        String profileUrl = "profile/" + randomNumber + "/" + paymentSubLoop.get("profile_id") + "." + fileExtension;
                        paymentSubMap.put("profileUrl", profileUrl);

                       
                        String fileExtension1 = getFileExtensionForImage1(paymentSubLoop);
                        String logoUrl = "logo/" + randomNumber + "/" + paymentSubLoop.get("profile_id") + "." + fileExtension1;
                        paymentSubMap.put("logoUrl", logoUrl);

                        paymentSubList.add(paymentSubMap);
                    }

                    paymentMap.put("paymentDetails", paymentSubList);
                    paymentList.add(paymentMap);
                }

                return ResponseEntity.ok(paymentList);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameter value");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving payment details: " + e.getMessage());
        }
    }
    private String getFileExtensionForImage(Map<String, Object> paymentSubLoop) {
        if (paymentSubLoop == null || paymentSubLoop.get("profileUrl") == null || paymentSubLoop.get("profileUrl").toString().isEmpty()) {
            return "jpg";
        }

        String url = paymentSubLoop.get("profileUrl").toString();
        if (url.endsWith(".png")) {
            return "png";
        } else if (url.endsWith(".jpg")) {
            return "jpg";
        } else {
            return "jpg";
        }
    }

    private String getFileExtensionForImage1(Map<String, Object> paymentSubLoop) {
        if (paymentSubLoop == null || paymentSubLoop.get("logoUrl") == null || paymentSubLoop.get("logoUrl").toString().isEmpty()) {
            return "jpg";
        }

        String url = paymentSubLoop.get("logoUrl").toString();
        if (url.endsWith(".png")) {
            return "png";
        } else if (url.endsWith(".jpg")) {
            return "jpg";
        } else {
            return "jpg";
        }
    }

}
