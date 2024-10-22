package com.example.hotel.entity.detail;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="purpose_of_stay")
public class PurposeOfStay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long purposeOfStayId;
	private String purpose;
	public long getPurposeOfStayId() {
		return purposeOfStayId;
	}
	public void setPurposeOfStayId(long purposeOfStayId) {
		this.purposeOfStayId = purposeOfStayId;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public PurposeOfStay() {
		super();
	}
	
	
}
