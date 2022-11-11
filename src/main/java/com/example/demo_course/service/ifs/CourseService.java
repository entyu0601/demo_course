package com.example.demo_course.service.ifs;

import java.util.Set;

import com.example.demo_course.vo.CourseResp;

public interface CourseService {

	// 創建課程
	public CourseResp createCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit);

	// 修改課程
	public CourseResp updateCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit);

	// 刪除課程
	public CourseResp deleteCourse(String courseCode);

	// 查詢課程(課堂代碼)
	public CourseResp getCourseByCode(String courseCode);

	// 查詢課程(課堂名稱)
	public CourseResp getCourseByName(String courseName);

	// 已選上課程查詢(學生ID)
	public CourseResp getCourseByStudentId(String studentId);

	// 創建學生資料
	public CourseResp createStudents(String studentId, String studentName);

	// 修改學生資料
	public CourseResp updateStudents(String studentId, String studentName);

	// 刪除學生資料
	public CourseResp deleteStudents(String studentId);

	// 選課
	public CourseResp seletionCourses(String studentId, Set<String> courseCodeSet);

	// 加選
	public CourseResp addCourses(String studentId, Set<String> courseCodeSet);

	// 退選
	public CourseResp dropCourses(String studentId, Set<String> courseCodeSet);

}
