package com.example.hotel.repository.booking;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hotel.entity.booking.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query(value = "select b.*,rl.room_id,rl.room_number_id,ar.room_no,ps.purpose,cr.address,cr.email,cr.name,cr.number"
			+ " from  booking as b" + " join room_number_list as rl on rl.booking_id = b.booking_id"
			+ " join add_rooms as ar on ar.room_id = rl.room_id"
			+ " join customer_registration as cr on cr.customer_id=b.customer_id"
			+ " join purpose_of_stay as ps on ps.purpose_of_stay_id=b.purpose_of_stay_id", nativeQuery = true)
	List<Map<String, Object>> getAllBookingDetails();

	@Query(value = "select b.*,rl.room_number_id,rl.room_id,ar.room_no,ps.purpose,cr.address,cr.email,cr.name,cr.number"
			+ " from  booking as b"
			+ " join room_number_list as rl on rl.booking_id = b.booking_id"
			+ " join add_rooms as ar on ar.room_id = rl.room_id"
			+ " join customer_registration as cr on cr.customer_id=b.customer_id"
			+ " join purpose_of_stay as ps on ps.purpose_of_stay_id=b.purpose_of_stay_id"
			+ " where b.vacate = false", nativeQuery = true)
	List<Map<String, Object>> getAllBookingDetailsByVacateFalse();

	@Query(value = " select year(booking_date) as year,month(booking_date) as month,"
			+ " sum(total_amount) as monthlyBookingIncome,monthname(booking_date) as monthName"
			+ " from booking"
			+ " where year(booking_date) = year(current_date())"
			+ " group by year(booking_date),month(booking_date),booking_date"
			+ " order by year(booking_date),month(booking_date),booking_date,monthName", nativeQuery = true)
	List<Map<String, Object>> getIncomeDetails();

	@Query(value = " select c.customer_id as customerId,c.name,p.total_amount as totalAmount,p.given_amount as givenAmount,p.balance from payment as p"
			+ "  join booking as b on b.booking_id=p.booking_id "
			+ " join customer_registration as c on c.customer_id=b.customer_id"
			+ " where b.vacate=false and p.balance >= 0", nativeQuery = true)
	List<Map<String, Object>> getCustomerBalanceDetail();

	@Query(value = "select c.customer_id AS customerId, c.name,b.vacate_date AS vacateDate,"
			+ " monthname(b.vacate_date) as month from booking as b"
			+ " join customer_registration as c on c.customer_id = b.customer_id"
			+ " where b.vacate = true and month(b.vacate_date) = month(current_date())", nativeQuery = true)
	List<Map<String, Object>> getCustomerVacateDate();
	
}
