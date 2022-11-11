package com.example.demo_course.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

	@Id
	@Column(name = "courseCode")
	private String courseCode;

	@Column(name = "courseName")
	private String courseName;

	@Column(name = "days")
	private String days;

	@Column(name = "courseStrtime")
	private LocalTime courseStrtime;

	@Column(name = "courseEndtime")
	private LocalTime courseEndtime;

	@Column(name = "credit")
	private int credit;

	public Course() {

	}

	public Course(String courseCode, String courseName, String days, LocalTime courseStrtime, LocalTime courseEndtime,
			int credit) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.days = days;
		this.courseStrtime = courseStrtime;
		this.courseEndtime = courseEndtime;
		this.credit = credit;
	}

	public void updateCourse(String courseName, String days, LocalTime courseStrtime, LocalTime courseEndtime,
			int credit) {
		this.courseName = courseName;
		this.days = days;
		this.courseStrtime = courseStrtime;
		this.courseEndtime = courseEndtime;
		this.credit = credit;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public LocalTime getCourseStrtime() {
		return courseStrtime;
	}

	public void setCourseStrtime(LocalTime courseStrtime) {
		this.courseStrtime = courseStrtime;
	}

	public LocalTime getCourseEndtime() {
		return courseEndtime;
	}

	public void setCourseEndtime(LocalTime courseEndtime) {
		this.courseEndtime = courseEndtime;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

}
