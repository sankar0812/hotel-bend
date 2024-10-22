package com.example.hotel.service.roomDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.roomDetails.AddRooms;
import com.example.hotel.entity.roomDetails.RoomsList;
import com.example.hotel.entity.roomDetails.SpecificationList;
import com.example.hotel.repository.roomDetails.AddRoomsRepository;
import com.example.hotel.repository.roomDetails.RoomsListRepository;
import com.example.hotel.repository.roomDetails.SpecificationListRepository;

@Service
public class AddRoomsService {

	@Autowired
	private AddRoomsRepository addRoomRepository;

	@Autowired
	private RoomsListRepository roomListRepository;

	@Autowired
	private SpecificationListRepository specificationListRepository;

	public List<AddRooms> listRooms() {
		return this.addRoomRepository.findAll();
	}

	public AddRooms saveRooms(AddRooms addRooms) {
		return addRoomRepository.save(addRooms);
	}

	public AddRooms findById(Long roomId) {
		return addRoomRepository.findById(roomId).get();
	}

	public void deleteRoomsById(Long id) {
		addRoomRepository.deleteById(id);
	}

	public void deleteRoomsListById(Long id) {
		roomListRepository.deleteById(id);
	}

	public List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithId(Long roomId) {
		return addRoomRepository.getAllRoomAndRoomListAndSpecificationListWithId(roomId);
	}

	public List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithFloorId(Long floorId) {
		return addRoomRepository.getAllRoomAndRoomListAndSpecificationListWithFloorId(floorId);
	}

	public List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationList() {
		return addRoomRepository.getAllRoomAndRoomListAndSpecificationList();
	}

	public void deleteSpecificationListById(Long id) {
		specificationListRepository.deleteBySpecificationListIdAndDeletedTrue(id);
	}

	public void deleteSpecificationListByIdRoom(Long id) {
		specificationListRepository.deleteById(id);
	}

	public RoomsList findRoomListById(Long id) {
		return roomListRepository.findById(id).get();
	}

	public SpecificationList findSpecificationById(Long id) {
		return specificationListRepository.findById(id).get();
	}

	public Optional<AddRooms> findRoomOptionalById(Long roomId) {
		return addRoomRepository.findById(roomId);
	}

	public Optional<RoomsList> findRoomListOptionalById(Long roomId) {
		return roomListRepository.findById(roomId);
	}

	public Optional<SpecificationList> findSpecificationListOptionalById(Long roomId) {
		return specificationListRepository.findById(roomId);
	}

	public boolean isRoomNoExist(Integer roomNo) {
		AddRooms existingRooms = addRoomRepository.findByRoomNo(roomNo);
		return existingRooms != null;
	}

	public List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithAvailableTrue() {
		return addRoomRepository.getAllRoomAndRoomListAndSpecificationListWithAvailableTrue();
	}

	public List<Map<String, Object>> getAllRoomAndRoomListAndSpecificationListWithAvailableTrueWithId(Long roleId) {
		return addRoomRepository.getAllRoomAndRoomListAndSpecificationListWithAvailableTrueWithId(roleId);
	}
}
