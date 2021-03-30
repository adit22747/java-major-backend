package com.elearning.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int videoId;
	private int srNo;

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public Video() {
		super();
	}
	
	@JsonIgnore
	@ManyToOne(targetEntity = Course.class ,fetch = FetchType.LAZY)
	@JoinColumn(name="courseId")
	private Course course;

	@OneToMany(targetEntity = EnrolledCourseVideo.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="videoId", referencedColumnName = "videoId")
    List<EnrolledCourseVideo> ecvideos;
	
	public String getCourse() {
		return course.getCourseName();
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	private String videoName;
	@Column(length = 1000)
	private String videoDesc;
	private String videoPath;

	@JsonInclude
	@Transient
	private String courseName;
	
	

	public Video(String videoName, String videoDesc, String videoPath) {
		super();
		this.videoName = videoName;
		this.videoDesc = videoDesc;
		this.videoPath = videoPath;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoDesc() {
		return videoDesc;
	}

	public void setVideoDesc(String videoDesc) {
		this.videoDesc = videoDesc;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getCourseName() {
		return course.getCourseName();
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	

	

	
	
	
}
