package com.example.hotel.entity.roomDetails;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rooms_list")
public class RoomsList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roomListId;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String imageUrl;
	@JsonIgnore
	private Blob image;
	private boolean deleted;
	private String imageType;

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getRoomListId() {
		return roomListId;
	}

	public void setRoomListId(long roomListId) {
		this.roomListId = roomListId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public RoomsList() {
		super();
	}

}
