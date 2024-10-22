package com.example.hotel.entity.specification;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="specification")
public class Specification {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long specificationId;
	private String specificationName;
	public long getSpecificationId() {
		return specificationId;
	}
	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}
	public String getSpecificationName() {
		return specificationName;
	}
	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}
	public Specification() {
		super();
	}
	
	
}
