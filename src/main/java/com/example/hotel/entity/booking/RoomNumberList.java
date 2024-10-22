package com.example.hotel.entity.booking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room_number_list")
public class RoomNumberList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roomNumberId;
	private long roomId;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getRoomNumberId() {
		return roomNumberId;
	}

	public void setRoomNumberId(long roomNumberId) {
		this.roomNumberId = roomNumberId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public RoomNumberList() {
		super();
	}

}
