package com.example.hotel.entity.customer;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "verification")
public class Verification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long verificationId;
	private String number;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String imageUrl;
	private String verificationType;
	@JsonIgnore
	private Blob image;

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public long getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(long verificationId) {
		this.verificationId = verificationId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

	public Verification() {
		super();
	}
}
