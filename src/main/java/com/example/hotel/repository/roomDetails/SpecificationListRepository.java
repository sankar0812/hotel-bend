package com.example.hotel.repository.roomDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.roomDetails.SpecificationList;

public interface SpecificationListRepository extends JpaRepository<SpecificationList, Long> {

	void deleteBySpecificationListIdAndDeletedTrue(Long id);

}
