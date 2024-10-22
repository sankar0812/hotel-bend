package com.example.hotel.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.customer.Verification;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

}
