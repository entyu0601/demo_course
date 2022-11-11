package com.example.demo_course.vo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseReq {

	private String courseCode;

	private String courseName;

	private String days;

	private String courseStrtime;

	private String courseEndtime;

	private int credit;

	private String studentId;

	private String studentName;

	@JsonProperty("courseCode_set")
	private Set<String> courseCodeSet;

	public CourseReq() {

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

	public String getCourseStrtime() {
		return courseStrtime;
	}

	public void setCourseStrtime(String courseStrtime) {
		this.courseStrtime = courseStrtime;
	}

	public String getCourseEndtime() {
		return courseEndtime;
	}

	public void setCourseEndtime(String courseEndtime) {
		this.courseEndtime = courseEndtime;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Set<String> getCourseCodeSet() {
		return courseCodeSet;
	}

	public void setCourseCodeSet(Set<String> courseCodeSet) {
		this.courseCodeSet = courseCodeSet;
	}

}
