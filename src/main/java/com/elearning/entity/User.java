package com.elearning.entity;

import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name ="usertype")
@DiscriminatorValue("regular")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	private String username;
	private String email;
	private String password;
	private boolean locked;
	private boolean activated;
	private int failedattempts; 
	private String role;
	
	
	
	@OneToOne(fetch = FetchType.LAZY ,
	    	
	    		cascade = CascadeType.ALL,
	    		mappedBy = "user"
	    		)
	private Profile profile;
	
	
	
	
	
	@OneToMany(targetEntity = Certificate.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    List<Certificate> certies;
	
	@OneToMany(targetEntity = Like.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    List<Like> likes;
	
	@OneToMany(targetEntity = Comment.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    List<Comment> comments;
	
	@OneToMany(targetEntity = EnrolledCourses.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    List<EnrolledCourses> enrolledcourses;
	
	
	
	@OneToMany(targetEntity = Feedback.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    List<Feedback> feedbacks;

//	
//	 @OneToOne(fetch = FetchType.LAZY,
//	    		optional = false)
//	 @JoinColumn(name="resultID",nullable = false,referencedColumnName = "resultID")
//	 private Result result;
//	
//	@OneToMany(targetEntity = Feedback.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name="userId", referencedColumnName = "userId")
//    List<Feedback> feedbacks;


	public String getUsername() {
		return username;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@JsonIgnore
	@JsonProperty(value = "password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public User() {
		super();
	}
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	public User(String username, String email, String password, boolean locked, boolean activated,String role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.locked = false;
		this.activated = false;
		this.role="user";
	}
	public User(int userId,String username, String email, String password, boolean locked, boolean activated,String role) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.locked = locked;
		this.activated = activated;
		this.role=role;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public User(int userId, List<Comment> comments) {
		super();
		this.userId = userId;
		this.comments = comments;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@JsonIgnore
	@JsonProperty(value = "certies")
	public List<Certificate> getCerties() {
		return certies;
	}
	public void setCerties(List<Certificate> certies) {
		this.certies = certies;
	}
	@JsonIgnore
	@JsonProperty(value = "comments")
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	@JsonIgnore
	@JsonProperty(value = "failedattempts")
	public int getFailedattempts() {
		return failedattempts;
	}
	public void setFailedattempts(int failedattempts) {
		this.failedattempts = failedattempts;
	}
	@JsonIgnore
	@JsonProperty(value = "enrolledcourses")
	public List<EnrolledCourses> getEnrolledcourses() {
		return enrolledcourses;
	}
	public void setEnrolledcourses(List<EnrolledCourses> enrolledcourses) {
		this.enrolledcourses = enrolledcourses;
	}
	@JsonIgnore
	@JsonProperty(value = "feedbacks")
	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	
	
}
