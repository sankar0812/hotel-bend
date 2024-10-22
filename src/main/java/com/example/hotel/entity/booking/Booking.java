package com.example.hotel.entity.booking;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookingId;
	private double givenAmount;
	private double extraBedAmount;
	private Date bookingDate;
	private Time bookingTime;
	private Date vacateDate;
	private Time vacateTime;
	private Date releivingDate;
	private Time releivingTime;
	private long customerId;
	private int extraBebs;
	private int child;
	private int men;
	private int women;
	private int noOfDays;
	private int noOfPerson;
	private long purposeOfStayId;
	private double balance;
	private String paymentType;
	private double totalAmount;
	private boolean vacate;
	private String statusType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "bookingId", referencedColumnName = "bookingId")
	private List<RoomNumberList> roomNumberDetails;

	public Date getReleivingDate() {
		return releivingDate;
	}

	public void setReleivingDate(Date releivingDate) {
		this.releivingDate = releivingDate;
	}

	public Time getReleivingTime() {
		return releivingTime;
	}

	public void setReleivingTime(Time releivingTime) {
		this.releivingTime = releivingTime;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Time getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(Time bookingTime) {
		this.bookingTime = bookingTime;
	}

	public Date getVacateDate() {
		return vacateDate;
	}

	public void setVacateDate(Date vacateDate) {
		this.vacateDate = vacateDate;
	}

	public Time getVacateTime() {
		return vacateTime;
	}

	public void setVacateTime(Time vacateTime) {
		this.vacateTime = vacateTime;
	}

	public boolean isVacate() {
		return vacate;
	}

	public void setVacate(boolean vacate) {
		this.vacate = vacate;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public double getGivenAmount() {
		return givenAmount;
	}

	public void setGivenAmount(double givenAmount) {
		this.givenAmount = givenAmount;
	}

	public double getExtraBedAmount() {
		return extraBedAmount;
	}

	public void setExtraBedAmount(double extraBedAmount) {
		this.extraBedAmount = extraBedAmount;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getExtraBebs() {
		return extraBebs;
	}

	public void setExtraBebs(int extraBebs) {
		this.extraBebs = extraBebs;
	}

	public int getChild() {
		return child;
	}

	public void setChild(int child) {
		this.child = child;
	}

	public int getMen() {
		return men;
	}

	public void setMen(int men) {
		this.men = men;
	}

	public int getWomen() {
		return women;
	}

	public void setWomen(int women) {
		this.women = women;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public int getNoOfPerson() {
		return noOfPerson;
	}

	public void setNoOfPerson(int noOfPerson) {
		this.noOfPerson = noOfPerson;
	}

	public long getPurposeOfStayId() {
		return purposeOfStayId;
	}

	public void setPurposeOfStayId(long purposeOfStayId) {
		this.purposeOfStayId = purposeOfStayId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<RoomNumberList> getRoomNumberDetails() {
		return roomNumberDetails;
	}

	public void setRoomNumberDetails(List<RoomNumberList> roomNumberDetails) {
		this.roomNumberDetails = roomNumberDetails;
	}

	public Booking() {
		super();
	}

}
