package com.example.hotel.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.employee.Maintanence;
import com.example.hotel.repository.employee.MaintanenceRepository;

@Service
public class MaintanenceService {

	@Autowired
	private MaintanenceRepository maintanenceRepository;

	public List<Maintanence> maintanenceList() {
		return maintanenceRepository.findAll();
	}

	public void saveMaintanence(Maintanence maintanence) {
		this.maintanenceRepository.save(maintanence);
	}

	public Maintanence findMaintanenceById(Long id) {
		return maintanenceRepository.findById(id).get();
	}

	public void deleteMaintanenceById(Long maintanenceId) {
		maintanenceRepository.deleteById(maintanenceId);
	}

}
