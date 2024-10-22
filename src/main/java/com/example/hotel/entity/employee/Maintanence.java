package com.example.hotel.entity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "maintanence")
public class Maintanence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long maintanenceId;
	private String maintanenceName;

	public long getMaintanenceId() {
		return maintanenceId;
	}

	public void setMaintanenceId(long maintanenceId) {
		this.maintanenceId = maintanenceId;
	}

	public String getMaintanenceName() {
		return maintanenceName;
	}

	public void setMaintanenceName(String maintanenceName) {
		this.maintanenceName = maintanenceName;
	}

	public Maintanence() {
		super();
	}

}
