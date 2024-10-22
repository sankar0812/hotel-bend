package com.example.hotel.entity.roomDetails;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room_maintenance")
public class RoomMaintenance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roomMaintenanceId;
	private long roomId;
	private long employeeId;
	private long specificationId;;
	private String roomStatus;
	private boolean completed;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private int noOfDays;
	private Date workStartDate;
	private Time workstartTime;
	
	

	public Date getWorkStartDate() {
		return workStartDate;
	}

	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}

	public Time getWorkstartTime() {
		return workstartTime;
	}

	public void setWorkstartTime(Time workstartTime) {
		this.workstartTime = workstartTime;
	}

	public long getRoomMaintenanceId() {
		return roomMaintenanceId;
	}

	public void setRoomMaintenanceId(long roomMaintenanceId) {
		this.roomMaintenanceId = roomMaintenanceId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public RoomMaintenance() {
		super();
	}

}
