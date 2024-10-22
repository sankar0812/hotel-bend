package com.example.hotel.repository.booking;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hotel.entity.booking.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query(value = "select p.*,c.name,c.email,b.booking_date from payment as p"
			+ " join customer_registration as c on c.customer_id = p.customer_id"
			+ " join booking as b on b.booking_id = p.booking_id", nativeQuery = true)
	List<Map<String, Object>> getAllPaymentDetails();
	
	
	@Query(value = " SELECT p.payment_id, p.balance, c.customer_id, p.given_amount, p.total_amount, b.booking_id, "
	        + " p.payment_type, bp.profile_id, c.name AS customerName, c.mobile_number, b.booking_date, "
	        + " bp.address, bp.country, bp.email, bp.location,bp.name as businessName, "
	        + " bp.phone_number,bp.pincode,bp.state"
	        + " FROM payment AS p "
	        + " JOIN booking AS b ON b.booking_id = p.booking_id "
	        + " JOIN customer_registration AS c ON c.customer_id = b.customer_id "
	        + " JOIN business_profile AS bp ON bp.profile_id = p.profile_id", nativeQuery = true)
	List<Map<String, Object>> getPaymentDetails();

	
}
