package com.elearning.entity;
import java.sql.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int courseId;
	private String courseName;
	@Column(length = 1000)
	private String courseDesc;
	private String courseLogo;
	private int coursePrice;
	public Date getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}
	private Date lastupdated;
	
	@JsonInclude
	@Transient
	private int enrollments;

	@JsonInclude
	@Transient
	private float progress;
	private int likes;
	
	@JsonInclude
	@Transient
	private int videosize;
	
	@JsonInclude
	@Transient
	private int totalcomment;

	@JsonInclude
	@Transient
	private int rating;
	
	@ManyToOne(targetEntity = Category.class ,fetch = FetchType.LAZY)
	@JoinColumn(name="categoryId")
	private Category category;
	
	@OneToMany(targetEntity = Video.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId", referencedColumnName = "courseId")
	List<Video> video;
	
	@OneToMany(targetEntity = Like.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="courseId", referencedColumnName = "courseId")
    List<Like> likess;
	
	@OneToMany(targetEntity = Feedback.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="courseId", referencedColumnName = "courseId")
	List<Feedback> feedbacks;
	
	@OneToMany(targetEntity = Certificate.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="courseId", referencedColumnName = "courseId")
	List<Certificate> certificates;
	
	
	@OneToMany(targetEntity = Comment.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="courseId", referencedColumnName = "courseId")
    List<Comment> comments;
	
	@OneToMany(targetEntity = EnrolledCourses.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="courseId", referencedColumnName = "courseId")
	List<EnrolledCourses> ecourse;
	
	public Course(String courseName, String courseDesc, String courseLogo, int coursePrice) {
		super();
		this.courseName = courseName;
		this.courseDesc = courseDesc;
		this.courseLogo = courseLogo;
		this.coursePrice = coursePrice;
	}
	public Course() {
		super();
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDesc() {
		return courseDesc;
	}
	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}
	public String getCourseLogo() {
		return courseLogo;
	}
	public void setCourseLogo(String courseLogo) {
		this.courseLogo = courseLogo;
	}
	public int getCoursePrice() {
		return coursePrice;
	}
	public void setCoursePrice(int coursePrice) {
		this.coursePrice = coursePrice;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	@JsonIgnore
	@JsonProperty(value = "video")
	public List<Video> getVideo() {
		return video;
	}
	
	
	
	
	public void setVideo(List<Video> video) {
		this.video = video;
	}
	public Course(String courseName, String courseDesc, String courseLogo, int coursePrice, int likes) {
		super();
		this.courseName = courseName;
		this.courseDesc = courseDesc;
		this.courseLogo = courseLogo;
		this.coursePrice = coursePrice;
		this.likes = 0;
	}
	@JsonIgnore
	@JsonProperty(value = "feedbacks")
	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	public float getProgress() {
		return progress;
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	@JsonIgnore
	@JsonProperty(value = "ecourse")
	public List<EnrolledCourses> getEcourse() {
		return ecourse;
	}
	public void setEcourse(List<EnrolledCourses> ecourse) {
		this.ecourse = ecourse;
	}
	public String getCategory() {
		return category.getCategoryName();
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	@JsonIgnore
	@JsonProperty(value = "comments")
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public int getEnrollments() {
		return enrollments;
	}
	public void setEnrollments(int enrollments) {
		this.enrollments = enrollments;
	}
	
	public int getTotalcomment() {
		return totalcomment;
	}
	public void setTotalcomment(int totalcomment) {
		this.totalcomment = totalcomment;
	}
	public int getVideosize() {
		return video.size();
	}
	public void setVideosize(int videosize) {
		this.videosize = videosize;
	}
	@JsonIgnore
	@JsonProperty(value = "likess")
	public List<Like> getLikess() {
		return likess;
	}
	public void setLikess(List<Like> likess) {
		this.likess = likess;
	}
	
	
}