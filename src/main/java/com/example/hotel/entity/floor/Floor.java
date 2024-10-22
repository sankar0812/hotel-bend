package com.example.hotel.entity.floor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="floor")
public class Floor {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long floorId;
	private String floorName;
	public long getFloorId() {
		return floorId;
	}
	public void setFloorId(long floorId) {
		this.floorId = floorId;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public Floor() {
		super();
	}
	
	
}
