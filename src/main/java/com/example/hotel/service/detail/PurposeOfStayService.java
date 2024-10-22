package com.example.hotel.service.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.detail.PurposeOfStay;
import com.example.hotel.repository.detail.PurposeOfStayRepository;

@Service
public class PurposeOfStayService {

	@Autowired
	private PurposeOfStayRepository purposeOfStayRepository;
	
	// view
		public List<PurposeOfStay> listAll() {
			return this.purposeOfStayRepository.findAll();
		}

		// save
		public PurposeOfStay SaveStayDetails(PurposeOfStay stay) {
			return purposeOfStayRepository.save(stay);
		}

		// edit
		public PurposeOfStay findById(Long purposeOfStayId) {
			return purposeOfStayRepository.findById(purposeOfStayId).get();
		}

		// delete
		public void deletePurposeOfStayId(Long id) {
			purposeOfStayRepository.deleteById(id);
		}

}
