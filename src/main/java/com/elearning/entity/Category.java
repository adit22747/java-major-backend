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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
@Entity
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;
    private String categoryName;
    @Column(length = 1000)
    private String categoryDesc;
    private String categoryLogo;
    //@OneToMany(targetEntity = Course.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JoinColumn(name="categoryId", referencedColumnName = "categoryId")
    @OneToMany(targetEntity = Course.class,fetch = FetchType.LAZY,cascade = { CascadeType.ALL })
    @JoinColumn(name="categoryId", referencedColumnName = "categoryId")
//     @JsonIgnore
     List<Course> courses;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public Category(int categoryId, List<Course> courses) {
		super();
		this.categoryId = categoryId;
		this.courses = courses;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getCategoryLogo() {
		return categoryLogo;
	}
	public void setCategoryLogo(String categoryLogo) {
		this.categoryLogo = categoryLogo;
	}
	@JsonIgnore
	@JsonProperty(value = "courses")
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public Category() {
		super();
	}
	public Category(String categoryName, String categoryDesc, String categoryLogo, List<Course> courses) {
		super();
		this.categoryName = categoryName;
		this.categoryDesc = categoryDesc;
		this.categoryLogo = categoryLogo;
		this.courses = courses;
	}
	public Category(int categoryId, String categoryName, String categoryDesc, String categoryLogo,
			List<Course> courses) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDesc = categoryDesc;
		this.categoryLogo = categoryLogo;
		this.courses = courses;
	}
	
	
	
	
}