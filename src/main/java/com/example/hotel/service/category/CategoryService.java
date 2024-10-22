package com.example.hotel.service.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.category.Category;
import com.example.hotel.repository.category.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	// view
	public List<Category> listAll() {
		return this.categoryRepository.findAll();
	}

	// save
	public Category SavecategoryDetails(Category category) {
		return categoryRepository.save(category);
	}

	// edit
	public Category findById(Long categoryId) {
		return categoryRepository.findById(categoryId).get();
	}

	// delete
	public void deleteCategoryId(Long id) {
		categoryRepository.deleteById(id);
	}

}
