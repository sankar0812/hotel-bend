package com.example.hotel.service.floor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.floor.Floor;
import com.example.hotel.repository.floor.FloorRepository;

@Service
public class FloorService {

	@Autowired
	private FloorRepository floorRepository;

	// view
	public List<Floor> listAll() {
		return this.floorRepository.findAll();
	}

	// save
	public Floor SaveFloorDetails(Floor floor) {
		return floorRepository.save(floor);
	}

	// edit
	public Floor findById(Long floorId) {
		return floorRepository.findById(floorId).get();
	}

	// delete
	public void deleteFloorId(Long id) {
		floorRepository.deleteById(id);
	}

}
