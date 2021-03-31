package com.elearning.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.sql.rowset.serial.SerialBlob;


@Entity
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int profileId;
	private String fullName;
	private Date birthdate;
	private String gender;
	private long contact;
	private String searchOccupation;
	@OneToOne(fetch = FetchType.LAZY,
	    		optional = false)
	 @JoinColumn(name="userId",nullable = false,referencedColumnName = "userId")
	 private User user;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Profile() {
	}
	public Profile(String fullName,  Date birthdate, String gender) {
		super();
		this.fullName = fullName;
		
		this.birthdate = birthdate;
		this.gender = gender;
	}
	public Profile(int profileId, String fullName, Date birthdate, String gender, long contact,
			String searchOccupation, User user) {
		super();
		this.profileId = profileId;
		this.fullName = fullName;
		
		this.birthdate = birthdate;
		this.gender = gender;
		this.contact = contact;
		this.searchOccupation = searchOccupation;
		this.user = user;
	}
	public String getUser() {
		return user.getUsername();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	 public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	public String getSearchOccupation() {
		return searchOccupation;
	}
	public void setSearchOccupation(String searchOccupation) {
		this.searchOccupation = searchOccupation;
	}
}
