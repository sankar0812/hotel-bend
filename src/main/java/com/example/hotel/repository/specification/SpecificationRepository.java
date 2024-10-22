package com.example.hotel.repository.specification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.specification.Specification;

public interface SpecificationRepository extends JpaRepository<Specification, Long> {

}
