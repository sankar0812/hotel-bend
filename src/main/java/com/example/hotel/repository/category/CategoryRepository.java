package com.example.hotel.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
