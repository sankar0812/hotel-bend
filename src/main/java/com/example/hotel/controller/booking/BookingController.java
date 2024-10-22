package com.example.hotel.controller.booking;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
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
import com.example.hotel.entity.booking.RoomNumberList;
import com.example.hotel.entity.roomDetails.AddRooms;
import com.example.hotel.entity.roomDetails.RoomMaintenance;
import com.example.hotel.repository.roomDetails.AddRoomsRepository;
import com.example.hotel.repository.roomDetails.RoomMaintenanceRepository;
import com.example.hotel.service.booking.BookingService;
import com.example.hotel.repository.booking.BookingRepository;
import com.example.hotel.repository.booking.PaymentRepository;
import com.example.hotel.repository.booking.RoomNumberListRepository;

@RestController
@CrossOrigin
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private AddRoomsRepository addRoomsRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomNumberListRepository roomNumberListRepository;

	@Autowired
	private RoomMaintenanceRepository roomMaintenanceRepository;

	private Date convertUtilDateToSqlDate(Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}

	private Date calculateVacateDate(Date bookingDate, int noOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bookingDate);
		calendar.add(Calendar.DAY_OF_MONTH, noOfDays);
		return new Date(calendar.getTimeInMillis());
	}

	@PostMapping("/booking/save")
	public ResponseEntity<?> addEmployeeWithImage(@RequestBody Booking booking) throws SQLException, IOException {

		booking.setBookingDate(new Date(System.currentTimeMillis()));
		booking.setBookingTime(new Time(System.currentTimeMillis()));

		int noOfDays = booking.getNoOfDays();
		Date bookingDateUtil = booking.getBookingDate();
		Date bookingDateSql = convertUtilDateToSqlDate(bookingDateUtil);
		Date vacateDate = calculateVacateDate(bookingDateSql, noOfDays);
		booking.setVacateDate(vacateDate);

		booking.setStatusType("onStayPeriod");

		Time bookingTime = booking.getBookingTime();

		Calendar bookingCalendar = Calendar.getInstance();
		bookingCalendar.setTimeInMillis(bookingTime.getTime());
		bookingCalendar.add(Calendar.DAY_OF_MONTH, noOfDays);
		Time vacateTime = new Time(bookingCalendar.getTimeInMillis());
		booking.setVacateTime(vacateTime);

		List<RoomNumberList> roomNumberList = booking.getRoomNumberDetails();

		for (RoomNumberList roomNumberLoop : roomNumberList) {
			Long roomId = roomNumberLoop.getRoomId();
			Optional<AddRooms> addRoomsList = addRoomsRepository.findById(roomId);

			if (addRoomsList.isPresent()) {
				AddRooms addRoom = addRoomsList.get();
				addRoom.setAvailable(false);
				addRoom.setBooking(true);
				addRoom.setCleaning(false);
				addRoom.setMaintanence(false);
				addRoom.setVacate(false);
				addRoom.setStatusType("booking");
			} else {
				System.err.println("Room with ID " + roomId + " not found.");
			}
		}
		bookingService.saveBooking(booking);
		long bookingId = booking.getBookingId();
		long customerId = booking.getCustomerId();
		double givenAmount = booking.getGivenAmount();
		double balance = booking.getBalance();
		double totalAmount = booking.getTotalAmount();
		String paymentType = booking.getPaymentType();

		Payment payment = new Payment();

		payment.setBalance(balance);
		payment.setCustomerId(customerId);
		payment.setGivenAmount(givenAmount);
		payment.setTotalAmount(totalAmount);
		payment.setBookingId(bookingId);
		payment.setPaymentType(paymentType);

		paymentRepository.save(payment);

		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "Booking data saved successfully");
		return ResponseEntity.ok(bookingMap);
	}

	@GetMapping("/booking/view")
	public ResponseEntity<Object> getAllBooking(@RequestParam(required = true) String booking) {
		if ("view".equals(booking)) {
			List<Map<String, Object>> bookingList = new ArrayList<>();
			List<Map<String, Object>> bookingRole = bookingService.getAllBookingDetails();
			Map<String, Map<String, List<Map<String, Object>>>> bookingGroupMap = bookingRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("booking_id").toString(),
							(Collectors.groupingBy(action -> action.get("room_number_id").toString()))));

			for (Entry<String, Map<String, List<Map<String, Object>>>> bookingLoop : bookingGroupMap.entrySet()) {
				Map<String, Object> bookingMap = new HashMap<>();
				bookingMap.put("bookingId", Long.parseLong(bookingLoop.getKey()));
				List<Map<String, Object>> roomNumberList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> roomListLoop : bookingLoop.getValue().entrySet()) {

					Map<String, Object> roomNumberMap = new HashMap<>();
					roomNumberMap.put("roomNumberId", roomListLoop.getKey());
					roomNumberMap.put("roomNo", roomListLoop.getValue().get(0).get("room_no"));

					bookingMap.put("balance", roomListLoop.getValue().get(0).get("balance"));
					bookingMap.put("bookingDate", roomListLoop.getValue().get(0).get("booking_date"));
					bookingMap.put("children", roomListLoop.getValue().get(0).get("children"));
					bookingMap.put("customerId", roomListLoop.getValue().get(0).get("customer_id"));
					bookingMap.put("date", roomListLoop.getValue().get(0).get("date"));
					bookingMap.put("extraBeds", roomListLoop.getValue().get(0).get("extra_bebs"));
					bookingMap.put("givenAmount", roomListLoop.getValue().get(0).get("given_amount"));
					bookingMap.put("men", roomListLoop.getValue().get(0).get("men"));
					bookingMap.put("extraBedAmount", roomListLoop.getValue().get(0).get("extra_bed_amount"));
					bookingMap.put("noOfDays", roomListLoop.getValue().get(0).get("no_of_days"));
					bookingMap.put("noOfPerson", roomListLoop.getValue().get(0).get("no_of_person"));
					bookingMap.put("paymentType", roomListLoop.getValue().get(0).get("payment_type"));
					bookingMap.put("purposeOfStayId", roomListLoop.getValue().get(0).get("purpose_of_stay_id"));
					bookingMap.put("totalAmount", roomListLoop.getValue().get(0).get("total_amount"));
					bookingMap.put("women", roomListLoop.getValue().get(0).get("women"));
					bookingMap.put("purpose", roomListLoop.getValue().get(0).get("purpose"));
					bookingMap.put("address", roomListLoop.getValue().get(0).get("address"));
					bookingMap.put("email", roomListLoop.getValue().get(0).get("email"));
					bookingMap.put("name", roomListLoop.getValue().get(0).get("name"));
					bookingMap.put("number", roomListLoop.getValue().get(0).get("number"));
					bookingMap.put("vacate", roomListLoop.getValue().get(0).get("vacate"));
					bookingMap.put("statusType", roomListLoop.getValue().get(0).get("status_type"));

					roomNumberList.add(roomNumberMap);
				}
				bookingMap.put("roomNumberDetails", roomNumberList);
				bookingList.add(bookingMap);
			}

			return ResponseEntity.ok(bookingList);
		} else {
			String errorMessage = "Invalid value for 'accessories'. Expected 'brand'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/booking/vacate/view")
	public ResponseEntity<Object> getAllBookingDetailsByVacateFalse(@RequestParam(required = true) String booking) {
		if ("view".equals(booking)) {
			List<Map<String, Object>> bookingList = new ArrayList<>();
			List<Map<String, Object>> bookDetails = bookingRepository.getAllBookingDetailsByVacateFalse();
			Map<String, Map<String, List<Map<String, Object>>>> bookingGroupMap = bookDetails.stream()
					.collect(Collectors.groupingBy(action -> action.get("booking_id").toString(),
							(Collectors.groupingBy(action -> action.get("room_number_id").toString()))));

			for (Entry<String, Map<String, List<Map<String, Object>>>> bookingLoop : bookingGroupMap.entrySet()) {
				Map<String, Object> bookingMap = new HashMap<>();
				bookingMap.put("bookingId", Long.parseLong(bookingLoop.getKey()));
				List<Map<String, Object>> roomNumberList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> roomListLoop : bookingLoop.getValue().entrySet()) {

					Map<String, Object> roomNumberMap = new HashMap<>();
					roomNumberMap.put("roomNumberId", roomListLoop.getKey());
					roomNumberMap.put("roomNo", roomListLoop.getValue().get(0).get("room_no"));
					roomNumberMap.put("roomId", roomListLoop.getValue().get(0).get("room_id"));

					bookingMap.put("balance", roomListLoop.getValue().get(0).get("balance"));
					bookingMap.put("bookingDate", roomListLoop.getValue().get(0).get("booking_date"));
					bookingMap.put("child", roomListLoop.getValue().get(0).get("child"));
					bookingMap.put("customerId", roomListLoop.getValue().get(0).get("customer_id"));
					bookingMap.put("date", roomListLoop.getValue().get(0).get("date"));
					bookingMap.put("extraBeds", roomListLoop.getValue().get(0).get("extra_bebs"));
					bookingMap.put("givenAmount", roomListLoop.getValue().get(0).get("given_amount"));
					bookingMap.put("men", roomListLoop.getValue().get(0).get("men"));
					bookingMap.put("extraBedAmount", roomListLoop.getValue().get(0).get("extra_bed_amount"));
					bookingMap.put("noOfDays", roomListLoop.getValue().get(0).get("no_of_days"));
					bookingMap.put("noOfPerson", roomListLoop.getValue().get(0).get("no_of_person"));
					bookingMap.put("paymentType", roomListLoop.getValue().get(0).get("payment_type"));
					bookingMap.put("purposeOfStayId", roomListLoop.getValue().get(0).get("purpose_of_stay_id"));
					bookingMap.put("totalAmount", roomListLoop.getValue().get(0).get("total_amount"));
					bookingMap.put("women", roomListLoop.getValue().get(0).get("women"));
					bookingMap.put("purpose", roomListLoop.getValue().get(0).get("purpose"));
					bookingMap.put("address", roomListLoop.getValue().get(0).get("address"));
					bookingMap.put("email", roomListLoop.getValue().get(0).get("email"));
					bookingMap.put("name", roomListLoop.getValue().get(0).get("name"));
					bookingMap.put("number", roomListLoop.getValue().get(0).get("number"));
					bookingMap.put("vacate", roomListLoop.getValue().get(0).get("vacate"));
					bookingMap.put("statusType", roomListLoop.getValue().get(0).get("status_type"));

					roomNumberList.add(roomNumberMap);
				}
				bookingMap.put("roomNumberDetails", roomNumberList);
				bookingList.add(bookingMap);
			}

			return ResponseEntity.ok(bookingList);
		} else {
			String errorMessage = "Invalid value for 'booking'. Expected 'view'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PutMapping("/booking/vacate/status/{id}")
	public ResponseEntity<?> updateVacateStatus(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
		try {
			Booking existingBooking = bookingService.findBookingById(bookingId);
			if (existingBooking == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingBooking.isVacate()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "vacate status already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}

			if (booking.getStatusType() != null) {
				existingBooking.setStatusType(booking.getStatusType());
			}

			if (booking.getStatusType().equals("vacate")) {
				existingBooking.setVacate(true);
				existingBooking.setReleivingDate(new Date(System.currentTimeMillis()));
				existingBooking.setReleivingTime(new Time(System.currentTimeMillis()));

				List<RoomNumberList> roomNumberList = existingBooking.getRoomNumberDetails();
				if (roomNumberList != null) {
					for (RoomNumberList roomNumberLoop : roomNumberList) {
						Long roomId = roomNumberLoop.getRoomId();
						Optional<AddRooms> addRoomsList = addRoomsRepository.findById(roomId);
						if (addRoomsList.isPresent()) {
							AddRooms addRoom = addRoomsList.get();
							addRoom.setAvailable(false);
							addRoom.setBooking(false);
							addRoom.setCleaning(false);
							addRoom.setMaintanence(false);
							addRoom.setVacate(true);
							addRoom.setStatusType("vacate");

							addRoomsRepository.save(addRoom);
						} else {
							System.err.println("Room with ID " + roomId + " not found.");
						}
					}
				}
			}

			bookingService.saveBooking(existingBooking);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "Booking data updated successfully");
			return ResponseEntity.ok(bookingMap);

		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/booking/edit/{id}")
	public ResponseEntity<?> updateBooking(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
		try {
			Booking existingBooking = bookingService.findBookingById(bookingId);

			if (existingBooking == null) {
				return ResponseEntity.notFound().build();
			}
			existingBooking.setBalance(booking.getBalance());
			existingBooking.setBookingDate(booking.getBookingDate());
			existingBooking.setChild(booking.getChild());
			existingBooking.setCustomerId(booking.getCustomerId());
			existingBooking.setExtraBebs(booking.getExtraBebs());
			existingBooking.setExtraBedAmount(booking.getExtraBedAmount());
			existingBooking.setGivenAmount(booking.getGivenAmount());
			existingBooking.setMen(booking.getMen());
			existingBooking.setNoOfDays(booking.getNoOfDays());
			existingBooking.setNoOfPerson(booking.getNoOfPerson());
			existingBooking.setPaymentType(booking.getPaymentType());
			existingBooking.setPurposeOfStayId(booking.getPurposeOfStayId());
			existingBooking.setTotalAmount(booking.getTotalAmount());
			existingBooking.setWomen(booking.getWomen());

			bookingService.saveBooking(existingBooking);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "Booking data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/income/detail/view")
	public ResponseEntity<?> getIncomeDetails(@RequestParam(required = true) String income) {
		try {
			if ("incomeDetails".equals(income)) {
				Iterable<Map<String, Object>> incomeDetails = bookingRepository.getIncomeDetails();
				return new ResponseEntity<>(incomeDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided income is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving income details: " + e.getMessage());

		}

	}

	
	@GetMapping("/customerBalance/detail/view")
	public ResponseEntity<?> getBalanceDetails(@RequestParam(required = true) String customer) {
		try {
			if ("balanceDetails".equals(customer)) {

				Iterable<Map<String, Object>> balanceDetails = bookingRepository.getCustomerBalanceDetail();
				return new ResponseEntity<>(balanceDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided balance is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving balance details: " + e.getMessage());

		}
	}

	@GetMapping("/customerVacateDate/detail")
	public ResponseEntity<?> getCustomerVacateDate(@RequestParam(required = true) String customer) {
		try {
			if ("vacateDate".equals(customer)) {
				Iterable<Map<String, Object>> balanceDetails = bookingRepository.getCustomerVacateDate();
				return new ResponseEntity<>(balanceDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided vacateDate is not supported.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving vacateDate details: " + e.getMessage());

		}

	}

	@PutMapping("/booking/swap/{id}")
	public ResponseEntity<?> updateBookingSwap(@PathVariable("id") Long bookingId, @RequestBody Booking booking) {
		try {
			Booking existingBooking = bookingService.findBookingById(bookingId);

			if (existingBooking == null) {
				return ResponseEntity.notFound().build();
			}

			List<RoomNumberList> updatedRoomNumberList = booking.getRoomNumberDetails();
			if (updatedRoomNumberList != null && !updatedRoomNumberList.isEmpty()) {
				for (RoomNumberList roomNumberLoop : updatedRoomNumberList) {
					Long roomNumberId = roomNumberLoop.getRoomNumberId();
					if (Objects.nonNull(roomNumberId) && roomNumberId != 0) {

						Optional<RoomNumberList> existingSpecificationListIsOptional = roomNumberListRepository
								.findById(roomNumberId);

						long roomId = existingSpecificationListIsOptional.get().getRoomId();

						String type = roomNumberLoop.getType();

						if (roomId != 0 && ("maintanence".equals(type) || "uncleaned".equals(type))) {
							Optional<AddRooms> existingAddRoomsIsOptional = addRoomsRepository.findById(roomId);

							if (existingAddRoomsIsOptional.isPresent()) {
								AddRooms existAddRooms = existingAddRoomsIsOptional.get();

								if ("maintenance".equals(type)) {
									existAddRooms.setMaintanence(true);
									existAddRooms.setStatusType("maintanence");
								} else if ("uncleaned".equals(type)) {
									existAddRooms.setUncleaned(true);
									existAddRooms.setStatusType("uncleaned");
								}

								addRoomsRepository.save(existAddRooms);
							}

							RoomMaintenance maintenance = new RoomMaintenance();
							maintenance.setRoomId(roomId);
							maintenance.setRoomStatus(type);
							maintenance.setStartDate(new Date(System.currentTimeMillis()));
							maintenance.setStartTime(new Time(System.currentTimeMillis()));
							roomMaintenanceRepository.save(maintenance);
						}
						if (existingSpecificationListIsOptional.isPresent()) {
							RoomNumberList existingSpecificationList = existingSpecificationListIsOptional.get();

							if (roomNumberLoop.getRoomId() != 0) {
								existingSpecificationList.setRoomId(roomNumberLoop.getRoomId());

							}

						}
					} else {
						existingBooking.getRoomNumberDetails().add(roomNumberLoop);
					}
				}
			}

			bookingService.saveBooking(existingBooking);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "Booking data updated successfully");
			return ResponseEntity.ok(existingBooking);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
