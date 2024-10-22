package com.example.hotel.repository.business;



import org.springframework.data.jpa.repository.JpaRepository;


import com.example.hotel.entity.business.BusinessProfile;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {

	
}