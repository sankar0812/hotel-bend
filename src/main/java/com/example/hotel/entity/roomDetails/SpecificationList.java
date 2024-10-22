package com.example.hotel.entity.roomDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "specification_list")
public class SpecificationList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long specificationListId;
	private long specificationId;
	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getSpecificationListId() {
		return specificationListId;
	}

	public void setSpecificationListId(long specificationListId) {
		this.specificationListId = specificationListId;
	}

	public long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}

	public SpecificationList() {
		super();
	}

}
