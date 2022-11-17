package com.example.demo_course.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Students {

	@Id
	@Column(name = "studentId")
	private String studentId;

	@Column(name = "studentName")
	private String studentName;

	@Column(name = "courseData")
	private String courseData;

	public Students() {

	}

	public Students(String studentId, String studentName) {
		this.studentId = studentId;
		this.studentName = studentName;
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

	public String getCourseData() {
		return courseData;
	}

	public void setCourseData(String courseData) {
		this.courseData = courseData;
	}

}
