package com.example.hotel.controller.category;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.entity.category.Category;
import com.example.hotel.service.category.CategoryService;

@RestController
@CrossOrigin

public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/category/view")
	public ResponseEntity<?> getCategoryDetails(@RequestParam(required = true) String category) {
		try {
			if ("categoryDetails".equals(category)) {
				Iterable<Category> categoryDetails = categoryService.listAll();
				return new ResponseEntity<>(categoryDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided category is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving category details: " + e.getMessage());
		}
	}

	@PostMapping("/category/save")
	public ResponseEntity<?> saveCategoryDetails(@RequestBody Category category) {
		try {
			categoryService.SavecategoryDetails(category);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "category data saved successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving category: " + e.getMessage());
		}
	}

	@PutMapping("/category/edit/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable("id") Long categoryId, @RequestBody Category category) {
		try {
			Category existingCategory = categoryService.findById(categoryId);

			if (existingCategory == null) {
				return ResponseEntity.notFound().build();
			}
			existingCategory.setCategoryName(category.getCategoryName());

			categoryService.SavecategoryDetails(existingCategory);
			Map<String, Object> bookingMap = new HashMap<>();
			bookingMap.put("message", "category data updated successfully");
			return ResponseEntity.ok(bookingMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/category/delete/{categoryId}")
	public ResponseEntity<?> deleteCategoryId(@PathVariable("categoryId") Long categoryId) {
		categoryService.deleteCategoryId(categoryId);
		Map<String, Object> bookingMap = new HashMap<>();
		bookingMap.put("message", "category data deleted successfully");
		return ResponseEntity.ok(bookingMap);
	}
}
