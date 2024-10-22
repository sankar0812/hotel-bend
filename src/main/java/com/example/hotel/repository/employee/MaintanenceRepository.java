package com.example.hotel.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.employee.Maintanence;

public interface MaintanenceRepository extends JpaRepository<Maintanence, Long> {

}
