package com.example.hotel.service.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.detail.City;
import com.example.hotel.repository.detail.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	// view
	public List<City> listAll() {
		return this.cityRepository.findAll();
	}

	// save
	public City SaveCityDetails(City city) {
		return cityRepository.save(city);
	}

	// edit
	public City findById(Long cityId) {
		return cityRepository.findById(cityId).get();
	}

	// delete
	public void deleteCityId(Long id) {
		cityRepository.deleteById(id);
	}
}
