package com.example.hotel.entity.roomDetails;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "add_rooms")
public class AddRooms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;
	private int roomNo;
	private long categoryId;
	private String roomType;
	private long floorId;
	private double amount;
	private double gstPercentage;
	private double totalAmount;
	private int noOfBeds;
	private String size;
	private boolean available;
	private boolean booking;
	private boolean maintanence;
	private boolean vacate;
	private boolean cleaning;
	private boolean uncleaned;
	private String statusType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "roomId", referencedColumnName = "roomId")
	private List<RoomsList> roomsList;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "roomId", referencedColumnName = "roomId")
	private List<SpecificationList> specificationList;

	public boolean isUncleaned() {
		return uncleaned;
	}

	public void setUncleaned(boolean uncleaned) {
		this.uncleaned = uncleaned;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isBooking() {
		return booking;
	}

	public void setBooking(boolean booking) {
		this.booking = booking;
	}

	public boolean isMaintanence() {
		return maintanence;
	}

	public void setMaintanence(boolean maintanence) {
		this.maintanence = maintanence;
	}

	public boolean isVacate() {
		return vacate;
	}

	public void setVacate(boolean vacate) {
		this.vacate = vacate;
	}

	public boolean isCleaning() {
		return cleaning;
	}

	public void setCleaning(boolean cleaning) {
		this.cleaning = cleaning;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public int getNoOfBeds() {
		return noOfBeds;
	}

	public void setNoOfBeds(int noOfBeds) {
		this.noOfBeds = noOfBeds;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public int getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(int roomNo) {
		this.roomNo = roomNo;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getFloorId() {
		return floorId;
	}

	public void setFloorId(long floorId) {
		this.floorId = floorId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(double gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<RoomsList> getRoomsList() {
		return roomsList;
	}

	public void setRoomsList(List<RoomsList> roomsList) {
		this.roomsList = roomsList;
	}

	public List<SpecificationList> getSpecificationList() {
		return specificationList;
	}

	public void setSpecificationList(List<SpecificationList> specificationList) {
		this.specificationList = specificationList;
	}

	public AddRooms() {
		super();
	}

}
