package com.example.hotel.service.specification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hotel.entity.specification.Specification;
import com.example.hotel.repository.specification.SpecificationRepository;

@Service

public class SpecificationService {

	@Autowired
	private SpecificationRepository specificationRepository;

	// view
	public List<Specification> listAll() {
		return this.specificationRepository.findAll();
	}

	// save
	public Specification SaveSpecificationDetails(Specification specification) {
		return specificationRepository.save(specification);
	}

	// edit
	public Specification findById(Long specificationId) {
		return specificationRepository.findById(specificationId).get();
	}

	// delete
	public void deleteSpecificationId(Long id) {
		specificationRepository.deleteById(id);
	}
}
