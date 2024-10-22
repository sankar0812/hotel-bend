package com.example.hotel.service.booking;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.booking.Booking;
import com.example.hotel.repository.booking.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	public List<Booking> bookingList() {
		return bookingRepository.findAll();
	}

	public void saveBooking(Booking booking) {
		this.bookingRepository.save(booking);
	}

	public Booking findBookingById(Long id) {
		return bookingRepository.findById(id).get();
	}

	public Booking findOptionalBookingById(Long bookingId) {
		Optional<Booking> optionalCustomer = bookingRepository.findById(bookingId);
		return optionalCustomer.orElse(null);
	}

	public void deleteBookingById(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	public List<Map<String, Object>> getAllBookingDetailsByVacateFalse() {
		return bookingRepository.getAllBookingDetailsByVacateFalse();
	}
	
	public List<Map<String, Object>> getAllBookingDetails() {
		return bookingRepository.getAllBookingDetails();
	}

}
