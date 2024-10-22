package com.example.hotel.repository.detail;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.detail.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
