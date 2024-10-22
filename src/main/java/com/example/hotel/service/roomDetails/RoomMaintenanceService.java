package com.example.hotel.service.roomDetails;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.roomDetails.RoomMaintenance;
import com.example.hotel.repository.roomDetails.RoomMaintenanceRepository;

@Service
public class RoomMaintenanceService {

	@Autowired
	private RoomMaintenanceRepository roomMaintenanceRepository;

	// view
	public List<RoomMaintenance> listAll() {
		return this.roomMaintenanceRepository.findAll();
	}

	// save
	public RoomMaintenance SaveRoomMaintenanceDetails(RoomMaintenance roomMaintenance) {
		return roomMaintenanceRepository.save(roomMaintenance);
	}

	// edit
	public RoomMaintenance findById(Long RoomMaintenanceId) {
		return roomMaintenanceRepository.findById(RoomMaintenanceId).get();
	}

	// delete
	public void deleteRoomMaintenanceId(Long id) {
		roomMaintenanceRepository.deleteById(id);
	}

	public List<Map<String, Object>> getRoomMaintenanceDetails() {
		return roomMaintenanceRepository.getRoomMaintenanceDetails();
	}
}
