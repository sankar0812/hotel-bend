package com.example.hotel.service.business;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hotel.entity.business.BusinessProfile;
import com.example.hotel.repository.business.BusinessProfileRepository;

@Service
public class BusinessProfileService {

	@Autowired
	private BusinessProfileRepository businessProfileRepository;

	public List<BusinessProfile> listAll() {
		return this.businessProfileRepository.findAll();
	}

	public BusinessProfile SaveProfileDetails(BusinessProfile businessProfile) {
		return businessProfileRepository.save(businessProfile);
	}

	public BusinessProfile findById(Long profileId) {
		return businessProfileRepository.findById(profileId).get();
	}

	public void deleteProfileId(Long id) {
		businessProfileRepository.deleteById(id);
	}
	
}