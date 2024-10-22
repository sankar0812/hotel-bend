package com.example.hotel.service.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.detail.Country;
import com.example.hotel.repository.detail.CountryRepository;

@Service

public class CountryService {

	@Autowired
	private CountryRepository countryRepository;
	
	// view
		public List<Country> listAll() {
			return this.countryRepository.findAll();
		}

		// save
		public Country SaveCountryDetails(Country country) {
			return countryRepository.save(country);
		}

		// edit
		public Country findById(Long countryId) {
			return countryRepository.findById(countryId).get();
		}

		// delete
		public void deleteCountryId(Long id) {
			countryRepository.deleteById(id);
		}
}
