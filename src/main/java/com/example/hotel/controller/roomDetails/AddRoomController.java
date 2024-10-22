package com.example.hotel.controller.roomDetails;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.hotel.entity.roomDetails.AddRooms;
import com.example.hotel.entity.roomDetails.RoomMaintenance;
import com.example.hotel.entity.roomDetails.RoomsList;
import com.example.hotel.entity.roomDetails.SpecificationList;
import com.example.hotel.repository.roomDetails.AddRoomsRepository;
import com.example.hotel.service.roomDetails.AddRoomsService;
import com.example.hotel.service.roomDetails.RoomMaintenanceService;

@RestController
@CrossOrigin
public class AddRoomController {

	@Autowired
	private AddRoomsService addRoomsService;

	@Autowired
	private RoomMaintenanceService roomMaintenanceService;

	@Autowired
	private AddRoomsRepository addRoomsRepository;

	private boolean isRoomNumberAlreadyExists(int roomNo, Long currentRoomId) {
		Optional<AddRooms> existingRoom = addRoomsRepository.findByRoomNo(roomNo);
		return existingRoom.isPresent() && !existingRoom.get().getRoomId().equals(currentRoomId);
	}

	@PostMapping("/add/rooms/save")
	public ResponseEntity<?> addEmployeeWithImage(@RequestBody AddRooms addRooms) {
		try {
			Integer roomNo = addRooms.getRoomNo();

			if (addRoomsService.isRoomNoExist(roomNo)) {
				Map<String, Object> errorEmail = new HashMap<>();
				errorEmail.put("message", "Room Number already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorEmail);
			}

			addRooms.setAvailable(true);
			addRooms.setCleaning(true);
			addRooms.setStatusType("available");

			List<RoomsList> roomList = addRooms.getRoomsList();
			for (RoomsList roomLoop : roomList) {
				String base64Image = roomLoop.getImageUrl();
				System.out.println(base64Image);
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);
				Blob blob = null;
				try {
					blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				roomLoop.setImage(blob);
			}

			addRoomsService.saveRooms(addRooms);
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "Rooms data saved successfully");
			return ResponseEntity.ok(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "Failed to save rooms data");
			errorMap.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
		}
	}

	@PutMapping("/addroom/edit/{id}")
	public ResponseEntity<?> updateProductd(@PathVariable("id") Long id, @RequestBody AddRooms updatedRooms) {
		try {

			if (isRoomNumberAlreadyExists(updatedRooms.getRoomNo(), id)) {
				Map<String, Object> errorEmail = new HashMap<>();
				errorEmail.put("message", "Room Number already exists");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorEmail);
			}
			Optional<AddRooms> existingRoomIsOptional = addRoomsService.findRoomOptionalById(id);

			if (existingRoomIsOptional.isPresent()) {
				AddRooms existingRooms = existingRoomIsOptional.get();

				if (updatedRooms.getRoomNo() != 0) {
					existingRooms.setRoomNo(updatedRooms.getRoomNo());
				}
				if (updatedRooms.getFloorId() != 0) {
					existingRooms.setFloorId(updatedRooms.getFloorId());
				}
				if (updatedRooms.getCategoryId() != 0) {
					existingRooms.setCategoryId(updatedRooms.getCategoryId());
				}
				if (updatedRooms.getAmount() != 0) {
					existingRooms.setAmount(updatedRooms.getAmount());
				}

				if (updatedRooms.getRoomType() != null) {
					existingRooms.setRoomType(updatedRooms.getRoomType());
				}
				if (updatedRooms.getGstPercentage() != 0) {
					existingRooms.setGstPercentage(updatedRooms.getGstPercentage());
				}
				if (updatedRooms.getTotalAmount() != 0) {
					existingRooms.setTotalAmount(updatedRooms.getTotalAmount());
				}
				if (updatedRooms.getNoOfBeds() != 0) {
					existingRooms.setNoOfBeds(updatedRooms.getNoOfBeds());
				}
				if (updatedRooms.getSize() != null) {
					existingRooms.setSize(updatedRooms.getSize());
				}

				List<RoomsList> updatedRoomList = updatedRooms.getRoomsList();
				if (updatedRoomList != null && !updatedRoomList.isEmpty()) {
					for (RoomsList updatedRoomLoop : updatedRoomList) {
						Long roomListId = updatedRoomLoop.getRoomListId();
						if (updatedRoomLoop.isDeleted()) {
							if (roomListId != null) {
								addRoomsService.deleteRoomsListById(roomListId);
							}
						}

						if (Objects.nonNull(roomListId) && roomListId != 0) {

							Optional<RoomsList> existingRoomListIsOptional = addRoomsService
									.findRoomListOptionalById(roomListId);

							if (existingRoomListIsOptional.isPresent()) {
								RoomsList existingRoomList = existingRoomListIsOptional.get();

								if (updatedRoomLoop.getImageUrl() != null) {
									existingRoomList.setImageUrl(updatedRoomLoop.getImageUrl());
								}
								if (updatedRoomLoop.isDeleted() != false) {
									existingRoomList.setDeleted(updatedRoomLoop.isDeleted());
								}

								if (updatedRoomLoop.getImage() != null) {
									existingRoomList.setImage(updatedRoomLoop.getImage());
								}

								String base64Image = updatedRoomLoop.getImageUrl();

								if (base64Image != null) {
									byte[] imageBytes = Base64.getDecoder().decode(base64Image);
									Blob blob = null;
									try {
										blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									existingRoomList.setImage(blob);
								} else {
									System.out.println("Error: getImageUrl() returned null.");
								}

							}
						} else {
							List<RoomsList> updatedRoomsList = existingRooms.getRoomsList();

							for (RoomsList roomLoop : updatedRoomsList) {
								String base64Image = roomLoop.getImageUrl();

								if (base64Image != null) {
									byte[] imageBytes = Base64.getDecoder().decode(base64Image);
									Blob blob = null;

									try {
										blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
									} catch (SQLException e) {
										e.printStackTrace();
									}

									updatedRoomLoop.setImage(blob);
								}
							}

							existingRooms.getRoomsList().add(updatedRoomLoop);
						}

					}
				}
				List<SpecificationList> updatedSpecificationList = updatedRooms.getSpecificationList();
				if (updatedSpecificationList != null && !updatedSpecificationList.isEmpty()) {
					for (SpecificationList specificationLoop : updatedSpecificationList) {
						Long specificationListId = specificationLoop.getSpecificationListId();

						if (specificationLoop.isDeleted()) {
							if (specificationListId != null) {
								addRoomsService.deleteSpecificationListByIdRoom(specificationListId);
							}
						}

						if (Objects.nonNull(specificationListId) && specificationListId != 0) {
							Optional<SpecificationList> existingSpecificationListIsOptional = addRoomsService
									.findSpecificationListOptionalById(specificationListId);

							if (existingSpecificationListIsOptional.isPresent()) {
								SpecificationList existingSpecificationList = existingSpecificationListIsOptional.get();

								if (specificationLoop.getSpecificationId() != 0) {
									existingSpecificationList
											.setSpecificationId(specificationLoop.getSpecificationId());
								}

								if (specificationLoop.isDeleted() != false) {
									existingSpecificationList.setDeleted(specificationLoop.isDeleted());
								}

							}
						} else {
							existingRooms.getSpecificationList().add(specificationLoop);
						}

					}
				}

				addRoomsService.saveRooms(existingRooms);

				Map<String, Object> bookingMap = new HashMap<>();
				bookingMap.put("message", "room data updated successfully");
				return ResponseEntity.ok(existingRooms);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + id);
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating product: " + e.getMessage());
		}
	}

	@GetMapping("/add/rooms/view")
	public ResponseEntity<?> getEmployeeReportId(@RequestParam(required = true) String rooms) {
		if ("customer".equals(rooms)) {
			List<Map<String, Object>> roomDetails = addRoomsService.getAllRoomAndRoomListAndSpecificationList();
			Map<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroupMap = roomDetails.stream()
					.collect(Collectors.groupingBy(action -> action.get("room_id").toString(), (Collectors.groupingBy(
							action -> action.get("room_list_id").toString(),
							(Collectors.groupingBy(action -> action.get("specification_list_id").toString()))))));
			List<Map<String, Object>> roomList = new ArrayList<>();
			for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : roomGroupMap
					.entrySet()) {
				Map<String, Object> roomMap = new HashMap<>();
				roomMap.put("roomId", roomGroup.getKey());

				List<Map<String, Object>> roomSubList = new ArrayList<>();
				for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue()
						.entrySet()) {
					Map<String, Object> roomListMap = new HashMap<>();
					String roomListId = roomSubLoop.getKey();
					long roomListIdLong = Long.parseLong(roomListId);
					roomListMap.put("roomListId", roomListIdLong);

					List<Map<String, Object>> specificationList = new ArrayList<>();
					for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue()
							.entrySet()) {
						Map<String, Object> specificationMap = new HashMap<>();

						String specificationListId = specificationLoop.getKey();
						long specificationListIdAsLong = Long.parseLong(specificationListId);
						specificationMap.put("specificationListId", specificationListIdAsLong);

						specificationMap.put("specificationName",
								specificationLoop.getValue().get(0).get("specification_name"));
						specificationMap.put("specificationId",
								specificationLoop.getValue().get(0).get("specification_id"));

						RoomsList roomsList = convertToVerificationObject(roomListMap);
						int randomNumber = generateRandomNumber();
						String fileExtension = getFileExtensionForImage(roomsList);
						String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "." + fileExtension;
						roomListMap.put("imageUrl", imageUrl);
						roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

						roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
						roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
						roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
						roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
						roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
						roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
						roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
						roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
						roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
						roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
						roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
						roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
						roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
						roomMap.put("uncleaned", specificationLoop.getValue().get(0).get("uncleaned"));
						roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
						roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
						roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
						roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));

						specificationList.add(specificationMap);

					}

					roomMap.put("specificationList", specificationList);
					roomSubList.add(roomListMap);
				}

				roomMap.put("roomsList", roomSubList);
				roomList.add(roomMap);

			}

			return ResponseEntity.ok(roomList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@GetMapping("/add/rooms/available/view")
	public ResponseEntity<?> getAllRoomAndRoomListAndSpecificationListWithAvailableTrue(
			@RequestParam(required = true) String rooms) {
		if ("customer".equals(rooms)) {
			List<Map<String, Object>> roomDetails = addRoomsService
					.getAllRoomAndRoomListAndSpecificationListWithAvailableTrue();
			Map<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroupMap = roomDetails.stream()
					.collect(Collectors.groupingBy(action -> action.get("room_id").toString(), (Collectors.groupingBy(
							action -> action.get("room_list_id").toString(),
							(Collectors.groupingBy(action -> action.get("specification_list_id").toString()))))));
			List<Map<String, Object>> roomList = new ArrayList<>();
			for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : roomGroupMap
					.entrySet()) {
				Map<String, Object> roomMap = new HashMap<>();
				roomMap.put("roomId", roomGroup.getKey());

				List<Map<String, Object>> roomSubList = new ArrayList<>();
				for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue()
						.entrySet()) {
					Map<String, Object> roomListMap = new HashMap<>();
					String roomListId = roomSubLoop.getKey();
					long roomListIdLong = Long.parseLong(roomListId);
					roomListMap.put("roomListId", roomListIdLong);

					List<Map<String, Object>> specificationList = new ArrayList<>();
					for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue()
							.entrySet()) {
						Map<String, Object> specificationMap = new HashMap<>();

						String specificationListId = specificationLoop.getKey();
						long specificationListIdAsLong = Long.parseLong(specificationListId);
						specificationMap.put("specificationListId", specificationListIdAsLong);

						specificationMap.put("specificationName",
								specificationLoop.getValue().get(0).get("specification_name"));
						specificationMap.put("specificationId",
								specificationLoop.getValue().get(0).get("specification_id"));

						RoomsList roomsList = convertToVerificationObject(roomListMap);
						int randomNumber = generateRandomNumber();
						String fileExtension = getFileExtensionForImage(roomsList);
						String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "." + fileExtension;
						roomListMap.put("imageUrl", imageUrl);
						roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

						roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
						roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
						roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
						roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
						roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
						roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
						roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
						roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
						roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
						roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
						roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
						roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
						roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
						roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
						roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
						roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
						roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));
						roomMap.put("uncleaned", specificationLoop.getValue().get(0).get("uncleaned"));


						specificationList.add(specificationMap);

					}

					roomMap.put("specificationList", specificationList);
					roomSubList.add(roomListMap);
				}

				roomMap.put("roomsList", roomSubList);
				roomList.add(roomMap);

			}

			return ResponseEntity.ok(roomList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@GetMapping("/add/rooms/floor/view")
	public ResponseEntity<?> getFloor(@RequestParam(required = true) String rooms) {
		if ("floor".equals(rooms)) {
			List<Map<String, Object>> roomDetails = addRoomsService.getAllRoomAndRoomListAndSpecificationList();
			Map<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> roomGroupMap = roomDetails
					.stream()
					.collect(Collectors.groupingBy(action -> action.get("floor_id").toString(), (Collectors.groupingBy(
							action -> action.get("room_id").toString(),
							(Collectors.groupingBy(action -> action.get("room_list_id").toString(), (Collectors
									.groupingBy(action -> action.get("specification_list_id").toString()))))))));
			List<Map<String, Object>> floorList = new ArrayList<>();
			for (Entry<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> floorGroup : roomGroupMap
					.entrySet()) {
				Map<String, Object> floorMap = new HashMap<>();
				floorMap.put("floorId", floorGroup.getKey());
				List<Map<String, Object>> roomList = new ArrayList<>();
				for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : floorGroup
						.getValue().entrySet()) {
					Map<String, Object> roomMap = new HashMap<>();
					roomMap.put("roomId", roomGroup.getKey());

					List<Map<String, Object>> roomSubList = new ArrayList<>();
					for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue()
							.entrySet()) {
						Map<String, Object> roomListMap = new HashMap<>();
						String roomListId = roomSubLoop.getKey();
						long roomListIdLong = Long.parseLong(roomListId);
						roomListMap.put("roomListId", roomListIdLong);

						List<Map<String, Object>> specificationList = new ArrayList<>();
						for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue()
								.entrySet()) {
							Map<String, Object> specificationMap = new HashMap<>();

							String specificationListId = specificationLoop.getKey();
							long specificationListIdAsLong = Long.parseLong(specificationListId);
							specificationMap.put("specificationListId", specificationListIdAsLong);
							specificationMap.put("specificationName",
									specificationLoop.getValue().get(0).get("specification_name"));
							specificationMap.put("specificationId",
									specificationLoop.getValue().get(0).get("specification_id"));

							RoomsList roomsList = convertToVerificationObject(roomListMap);
							int randomNumber = generateRandomNumber();
							String fileExtension = getFileExtensionForImage(roomsList);
							String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "."
									+ fileExtension;
							roomListMap.put("imageUrl", imageUrl);
							roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

							floorMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));

							roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
							roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
							roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
							roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
							roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
							roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
							roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
							roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
							roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
							roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
							roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
							roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
							roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
							roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
							roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
							roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
							roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));
							roomMap.put("uncleaned", specificationLoop.getValue().get(0).get("uncleaned"));


							specificationList.add(specificationMap);

						}

						roomMap.put("specificationList", specificationList);
						roomSubList.add(roomListMap);
					}

					roomMap.put("roomsList", roomSubList);
					roomList.add(roomMap);

				}
				floorMap.put("floorDetails", roomList);
				floorList.add(floorMap);
			}

			return ResponseEntity.ok(floorList);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@GetMapping("/add/rooms/floor/view/{id}")
	public ResponseEntity<?> getFloorWithId(@PathVariable("id") Long floorId) {
		List<Map<String, Object>> roomDetails = addRoomsService
				.getAllRoomAndRoomListAndSpecificationListWithFloorId(floorId);
		Map<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> roomGroupMap = roomDetails
				.stream()
				.collect(Collectors.groupingBy(action -> action.get("floor_id").toString(),
						(Collectors.groupingBy(action -> action.get("room_id").toString(), (Collectors.groupingBy(
								action -> action.get("room_list_id").toString(),
								(Collectors.groupingBy(action -> action.get("specification_list_id").toString()))))))));
		Map<String, Object> floorList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, Map<String, List<Map<String, Object>>>>>> floorGroup : roomGroupMap
				.entrySet()) {
			Map<String, Object> floorMap = new HashMap<>();
			floorMap.put("floorId", floorGroup.getKey());
			List<Map<String, Object>> roomList = new ArrayList<>();
			for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : floorGroup.getValue()
					.entrySet()) {
				Map<String, Object> roomMap = new HashMap<>();
				roomMap.put("roomId", roomGroup.getKey());

				List<Map<String, Object>> roomSubList = new ArrayList<>();
				for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue()
						.entrySet()) {
					Map<String, Object> roomListMap = new HashMap<>();
					String roomListId = roomSubLoop.getKey();
					long roomListIdLong = Long.parseLong(roomListId);
					roomListMap.put("roomListId", roomListIdLong);

					List<Map<String, Object>> specificationList = new ArrayList<>();
					for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue()
							.entrySet()) {
						Map<String, Object> specificationMap = new HashMap<>();

						String specificationListId = specificationLoop.getKey();
						long specificationListIdAsLong = Long.parseLong(specificationListId);
						specificationMap.put("specificationListId", specificationListIdAsLong);
						specificationMap.put("specificationName",
								specificationLoop.getValue().get(0).get("specification_name"));
						specificationMap.put("specificationId",
								specificationLoop.getValue().get(0).get("specification_id"));

						RoomsList roomsList = convertToVerificationObject(roomListMap);
						int randomNumber = generateRandomNumber();
						String fileExtension = getFileExtensionForImage(roomsList);
						String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "." + fileExtension;
						roomListMap.put("imageUrl", imageUrl);
						roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

						floorMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));

						roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
						roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
						roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
						roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
						roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
						roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
						roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
						roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
						roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
						roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
						roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
						roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
						roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
						roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
						roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
						roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
						roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));
						roomMap.put("uncleaned", specificationLoop.getValue().get(0).get("uncleaned"));


						specificationList.add(specificationMap);

					}

					roomMap.put("specificationList", specificationList);
					roomSubList.add(roomListMap);
				}

				roomMap.put("roomsList", roomSubList);
				roomList.add(roomMap);

			}
			floorMap.put("floorDetails", roomList);
			floorList.putAll(floorMap);
		}

		return ResponseEntity.ok(floorList);
	}

	@GetMapping("/add/rooms/view/{id}")
	public ResponseEntity<?> getAllRoomAndRoomListAndSpecificationListWithId(@PathVariable("id") Long roleId) {
		List<Map<String, Object>> roomDetails = addRoomsService.getAllRoomAndRoomListAndSpecificationListWithId(roleId);
		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroupMap = roomDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("room_id").toString(),
						(Collectors.groupingBy(action -> action.get("room_list_id").toString(),
								(Collectors.groupingBy(action -> action.get("specification_list_id").toString()))))));
		Map<String, Object> roomList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : roomGroupMap.entrySet()) {
			Map<String, Object> roomMap = new HashMap<>();
			roomMap.put("roomId", roomGroup.getKey());

			List<Map<String, Object>> roomSubList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue().entrySet()) {
				Map<String, Object> roomListMap = new HashMap<>();
				String roomListId = roomSubLoop.getKey();
				long roomListIdLong = Long.parseLong(roomListId);
				roomListMap.put("roomListId", roomListIdLong);

				List<Map<String, Object>> specificationList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue().entrySet()) {
					Map<String, Object> specificationMap = new HashMap<>();
					String specificationListId = specificationLoop.getKey();
					long specificationListIdAsLong = Long.parseLong(specificationListId);
					specificationMap.put("specificationListId", specificationListIdAsLong);
					specificationMap.put("specificationName",
							specificationLoop.getValue().get(0).get("specification_name"));
					specificationMap.put("specificationId",
							specificationLoop.getValue().get(0).get("specification_id"));

					RoomsList roomsList = convertToVerificationObject(roomListMap);
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(roomsList);
					String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "." + fileExtension;
					roomListMap.put("imageUrl", imageUrl);
					roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

					roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
					roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
					roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
					roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
					roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
					roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
					roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
					roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
					roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
					roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
					roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
					roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
					roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
					roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
					roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
					roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
					roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));

					specificationList.add(specificationMap);

				}

				roomMap.put("specificationList", specificationList);
				roomSubList.add(roomListMap);
			}

			roomMap.put("roomsList", roomSubList);
			roomList.putAll(roomMap);

		}

		return ResponseEntity.ok(roomList);

	}

	@GetMapping("/add/rooms/available/view/{id}")
	public ResponseEntity<?> getAllRoomAndRoomListAndSpecificationListWithAvailableTrueWithId(
			@PathVariable("id") Long roleId) {
		List<Map<String, Object>> roomDetails = addRoomsService
				.getAllRoomAndRoomListAndSpecificationListWithAvailableTrueWithId(roleId);
		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroupMap = roomDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("room_id").toString(),
						(Collectors.groupingBy(action -> action.get("room_list_id").toString(),
								(Collectors.groupingBy(action -> action.get("specification_list_id").toString()))))));
		Map<String, Object> roomList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> roomGroup : roomGroupMap.entrySet()) {
			Map<String, Object> roomMap = new HashMap<>();
			roomMap.put("roomId", roomGroup.getKey());

			List<Map<String, Object>> roomSubList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> roomSubLoop : roomGroup.getValue().entrySet()) {
				Map<String, Object> roomListMap = new HashMap<>();
				String roomListId = roomSubLoop.getKey();
				long roomListIdLong = Long.parseLong(roomListId);
				roomListMap.put("roomListId", roomListIdLong);

				List<Map<String, Object>> specificationList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> specificationLoop : roomSubLoop.getValue().entrySet()) {
					Map<String, Object> specificationMap = new HashMap<>();
					String specificationListId = specificationLoop.getKey();
					long specificationListIdAsLong = Long.parseLong(specificationListId);
					specificationMap.put("specificationListId", specificationListIdAsLong);
					specificationMap.put("specificationName",
							specificationLoop.getValue().get(0).get("specification_name"));
					specificationMap.put("specificationId",
							specificationLoop.getValue().get(0).get("specification_id"));

					RoomsList roomsList = convertToVerificationObject(roomListMap);
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(roomsList);
					String imageUrl = "rooms/" + randomNumber + "/" + roomSubLoop.getKey() + "." + fileExtension;
					roomListMap.put("imageUrl", imageUrl);
					roomListMap.put("imageType", specificationLoop.getValue().get(0).get("image_type"));

					roomMap.put("roomType", specificationLoop.getValue().get(0).get("room_type"));
					roomMap.put("roomNo", specificationLoop.getValue().get(0).get("room_no"));
					roomMap.put("categoryId", specificationLoop.getValue().get(0).get("category_id"));
					roomMap.put("categoryName", specificationLoop.getValue().get(0).get("category_name"));
					roomMap.put("floorId", specificationLoop.getValue().get(0).get("floor_id"));
					roomMap.put("floorName", specificationLoop.getValue().get(0).get("floor_name"));
					roomMap.put("amount", specificationLoop.getValue().get(0).get("amount"));
					roomMap.put("gstPercentage", specificationLoop.getValue().get(0).get("gst_percentage"));
					roomMap.put("totalAmount", specificationLoop.getValue().get(0).get("total_amount"));
					roomMap.put("noOfBeds", specificationLoop.getValue().get(0).get("no_of_beds"));
					roomMap.put("size", specificationLoop.getValue().get(0).get("size"));
					roomMap.put("available", specificationLoop.getValue().get(0).get("available"));
					roomMap.put("cleaning", specificationLoop.getValue().get(0).get("cleaning"));
					roomMap.put("maintanence", specificationLoop.getValue().get(0).get("maintanence"));
					roomMap.put("vacate", specificationLoop.getValue().get(0).get("vacate"));
					roomMap.put("booking", specificationLoop.getValue().get(0).get("booking"));
					roomMap.put("statusType", specificationLoop.getValue().get(0).get("status_type"));
					roomMap.put("uncleaned", specificationLoop.getValue().get(0).get("uncleaned"));


					specificationList.add(specificationMap);

				}

				roomMap.put("specificationList", specificationList);
				roomSubList.add(roomListMap);
			}

			roomMap.put("roomsList", roomSubList);
			roomList.putAll(roomMap);

		}

		return ResponseEntity.ok(roomList);

	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

//	@GetMapping("rooms/{randomNumber}/{id:.+}")
//	public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
//			@PathVariable("id") String id) {
//		String[] parts = id.split("\\.");
//		if (parts.length != 2) {
//			return ResponseEntity.badRequest().build();
//		}
//		String fileExtension = parts[1];
//
//		Long imageId;
//		try {
//			imageId = Long.parseLong(parts[0]);
//		} catch (NumberFormatException e) {
//			return ResponseEntity.badRequest().build();
//		}
//
//		Optional<RoomsList> image = addRoomsService.findRoomListOptionalById(imageId);
//		if (image == null) {
//			return ResponseEntity.notFound().build();
//		}
//
//		byte[] imageBytes;
//		try {
//			imageBytes = image.get().getImage().getBytes(1, (int) image.get().getImage().length());
//		} catch (SQLException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		ByteArrayResource resource = new ByteArrayResource(imageBytes);
//		HttpHeaders headers = new HttpHeaders();
//
//		if ("jpg".equalsIgnoreCase(fileExtension)) {
//			headers.setContentType(MediaType.IMAGE_JPEG);
//		} else if ("png".equalsIgnoreCase(fileExtension)) {
//			headers.setContentType(MediaType.IMAGE_PNG);
//		} else {
//
//			headers.setContentType(MediaType.IMAGE_JPEG);
//		}
//
//		return ResponseEntity.ok().headers(headers).body(resource);
//	}

	@GetMapping("rooms/{randomNumber}/{id:.+}")
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

		Optional<RoomsList> imageOptional = addRoomsService.findRoomListOptionalById(imageId);
		if (imageOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		RoomsList image = imageOptional.get();
		Blob imageBlob = image.getImage();

		if (imageBlob == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
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

	private String getFileExtensionForImage(RoomsList image) {
		if (image == null || image.getImageUrl() == null || image.getImageUrl().isEmpty()) {
			return "jpg";
		}
		String url = image.getImageUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	private RoomsList convertToVerificationObject(Map<String, Object> verificationMap) {
		RoomsList roomsList = new RoomsList();
		roomsList.setImageUrl((String) verificationMap.get("imageUrl"));
		return roomsList;
	}

	@PutMapping("/room/cleaning/status/{id}")
	public ResponseEntity<?> updateCleaningStatus(@PathVariable("id") Long bookingId, @RequestBody AddRooms addrooms) {
		try {
			AddRooms existingRooms = addRoomsService.findById(bookingId);
			if (existingRooms == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingRooms.isCleaning()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "cleaning status already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}

			existingRooms.setStatusType(addrooms.getStatusType());

			if (addrooms.getStatusType().equals("cleaned")) {

				existingRooms.setAvailable(false);
				existingRooms.setBooking(false);
				existingRooms.setCleaning(true);
				existingRooms.setMaintanence(false);
				existingRooms.setVacate(false);
			}

			addRoomsService.saveRooms(existingRooms);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "cleaning status updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@PutMapping("/room/unCleaned/status/{id}")
	public ResponseEntity<?> updateUnCleaningStatus(@PathVariable("id") Long bookingId, @RequestBody AddRooms addrooms) {
		try {
			AddRooms existingRooms = addRoomsService.findById(bookingId);
			if (existingRooms == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingRooms.isCleaning()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "unCleaned status already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}

			existingRooms.setStatusType(addrooms.getStatusType());

			if (addrooms.getStatusType().equals("uncleaned")) {

				existingRooms.setAvailable(false);
				existingRooms.setBooking(false);
				existingRooms.setCleaning(false);
				existingRooms.setMaintanence(false);
				existingRooms.setVacate(false);
				existingRooms.setUncleaned(true);
			}

			addRoomsService.saveRooms(existingRooms);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "unCleaned status updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@PutMapping("/room/available/status/{id}")
	public ResponseEntity<?> updateAvailableStatus(@PathVariable("id") Long roomId, @RequestBody AddRooms addrooms) {
		try {
			AddRooms existingRooms = addRoomsService.findById(roomId);
			if (existingRooms == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingRooms.isAvailable()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "available status already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}

			existingRooms.setStatusType(addrooms.getStatusType());

			if (addrooms.getStatusType().equals("available")) {

				existingRooms.setAvailable(true);
				existingRooms.setBooking(false);
				existingRooms.setCleaning(true);
				existingRooms.setMaintanence(false);
				existingRooms.setVacate(false);
				existingRooms.setUncleaned(false);
			}

			addRoomsService.saveRooms(existingRooms);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "available status updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/room/maintanence/status/{id}")
	public ResponseEntity<?> updateMaintanencetatus(@PathVariable("id") Long roomId, @RequestBody AddRooms addrooms) {
		try {
			AddRooms existingRooms = addRoomsService.findById(roomId);
			if (existingRooms == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingRooms.isMaintanence()) {
				Map<String, Object> errorMessage = new HashMap<>();
				errorMessage.put("message", "maintanence status already exists");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
			}

			existingRooms.setStatusType(addrooms.getStatusType());

			if (addrooms.getStatusType().equals("maintanence")) {

				existingRooms.setAvailable(false);
				existingRooms.setBooking(false);
				existingRooms.setCleaning(false);
				existingRooms.setMaintanence(true);
				existingRooms.setVacate(false);
				existingRooms.setUncleaned(false);

			}

			addRoomsService.saveRooms(existingRooms);

			if (existingRooms.getStatusType().equals("maintanence")) {

				long id = existingRooms.getRoomId();

				RoomMaintenance roomMintanence = new RoomMaintenance();

				roomMintanence.setRoomId(id);
				roomMintanence.setRoomStatus("maintanence");
				roomMintanence.setStartDate(new Date(System.currentTimeMillis()));
				roomMintanence.setStartTime(new Time(System.currentTimeMillis()));
				roomMaintenanceService.SaveRoomMaintenanceDetails(roomMintanence);
			}

			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "maintanence status updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/dashboard/detail/view")
	public ResponseEntity<?> getDashboardPageDetails(@RequestParam(required = true) String dashboard) {
		try {
			if ("dashboardDetails".equals(dashboard)) {
				Map<String, Object> dashboardDetails = addRoomsRepository.getdashboardPageDetails();

				return new ResponseEntity<>(dashboardDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided dashboard is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving dashboard details: " + e.getMessage());
		}
	}
	
	
	@GetMapping("/dashboard/percentage/view")
	public ResponseEntity<?> getAllPercentageDetails(@RequestParam(required = true) String dashboard) {
		try {
			if ("dashboardDetails".equals(dashboard)) {
				Map<String, Object> dashboardDetails = addRoomsRepository.getAllPercentageDetails();

				return new ResponseEntity<>(dashboardDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided dashboard is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving dashboard details: " + e.getMessage());
		}
	}

}
