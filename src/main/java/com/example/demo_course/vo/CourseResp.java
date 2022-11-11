package com.example.demo_course.vo;

import java.util.List;
import java.util.Set;

import com.example.demo_course.entity.Course;
import com.example.demo_course.entity.Students;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResp {

	@JsonProperty("course_info")
	private Course course;

	@JsonProperty("student_info")
	private Students students;

	private String message;

	@JsonProperty("Course")
	private String allCourse;

	@JsonProperty("courseCode_set")
	private Set<String> courseCodeSet;

	@JsonProperty("courseName_list")
	private List<Course> courseNameList;

	public CourseResp() {

	}
	
	public CourseResp(Set<String> courseCodeSet,String message) {
		this.courseCodeSet=courseCodeSet;
		this.message=message;
	}

	public CourseResp(Students students, List<Course> courseNameList, String message) {
		this.students = students;
		this.courseNameList = courseNameList;
		this.message = message;
	}

	public CourseResp(String message) {
		this.message = message;
	}

	public CourseResp(String allCourse, String message) {
		this.allCourse = allCourse;
		this.message = message;
	}

	public CourseResp(List<Course> courseNameList, String message) {
		this.courseNameList = courseNameList;
		this.message = message;
	}

	public CourseResp(Course course, String message) {
		this.course = course;
		this.message = message;
	}

	public CourseResp(Students students, String message) {
		this.students = students;
		this.message = message;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Students getStudents() {
		return students;
	}

	public void setStudents(Students students) {
		this.students = students;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<String> getCourseCodeSet() {
		return courseCodeSet;
	}

	public void setCourseCodeSet(Set<String> courseCodeSet) {
		this.courseCodeSet = courseCodeSet;
	}

	public List<Course> getCourseNameList() {
		return courseNameList;
	}

	public void setCourseNameList(List<Course> courseNameList) {
		this.courseNameList = courseNameList;
	}

	public String getAllCourse() {
		return allCourse;
	}

	public void setAllCourse(String allCourse) {
		this.allCourse = allCourse;
	}

}
