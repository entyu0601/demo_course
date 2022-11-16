package com.example.demo_course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_course.service.ifs.CourseService;
import com.example.demo_course.vo.CourseReq;
import com.example.demo_course.vo.CourseResp;

@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	/* 創建課程 */
	@PostMapping(value = "/api/createCourse")
	public CourseResp course(@RequestBody CourseReq req) {
		return courseService.createCourse(req.getCourseCode(), req.getCourseName(), req.getDays(),
				req.getCourseStrtime(), req.getCourseEndtime(), req.getCredit());
	}

	/* 修改課程 */
	@PostMapping(value = "/api/updateCourse")
	public CourseResp updateCourse(@RequestBody CourseReq req) {
		return courseService.updateCourse(req.getCourseCode(), req.getCourseName(), req.getDays(),
				req.getCourseStrtime(), req.getCourseEndtime(), req.getCredit());
	}

	/* 刪除課程 */
	@PostMapping(value = "/api/deleteCourse")
	public CourseResp deleteCourse(@RequestBody CourseReq req) {
		return courseService.deleteCourse(req.getCourseCode());
	}

	/* 查詢課程(課堂代碼) */
	@PostMapping(value = "/api/getCourseByCode")
	public CourseResp getCourseByCode(@RequestBody CourseReq req) {
		return courseService.getCourseByCode(req.getCourseCode());
	}

	/* 查詢課程(課堂名稱) */
	@PostMapping(value = "/api/getCourseByName")
	public CourseResp getCourseByName(@RequestBody CourseReq req) {
		return courseService.getCourseByName(req.getCourseName());
	}
	
	/* 查詢課程(學生ID) */
	@PostMapping(value = "/api/getCourseByStudentId")
	public CourseResp getCourseByStudentId(@RequestBody CourseReq req) {
		return courseService.getCourseByStudentId(req.getStudentId());
	}

	/* 創建學生資料 */
	@PostMapping(value = "/api/createStudents")
	public CourseResp createStudents(@RequestBody CourseReq req) {
		return courseService.createStudents(req.getStudentId(), req.getStudentName());
	}

	/* 修改學生資料 */
	@PostMapping(value = "/api/updateStudents")
	public CourseResp updateStudents(@RequestBody CourseReq req) {
		return courseService.updateStudents(req.getStudentId(), req.getStudentName());
	}

	/* 刪除學生資料 */
	@PostMapping(value = "/api/deleteStudents")
	public CourseResp deleteStudents(@RequestBody CourseReq req) {
		return courseService.deleteStudents(req.getStudentId());
	}

	/* 選課 */
	@PostMapping(value = "/api/seletionCourses")
	public CourseResp seletionCourses(@RequestBody CourseReq req) {
		return courseService.seletionCourses(req.getStudentId(), req.getCourseCodeSet());
	}
	
	/* 加選 */
	@PostMapping(value = "/api/addCourses")
	public CourseResp addCourses(@RequestBody CourseReq req) {
		return courseService.addCourses(req.getStudentId(), req.getCourseCodeSet());
	}
	
	/* 退選 */
	@PostMapping(value = "/api/dropCourses")
	public CourseResp dropCourses(@RequestBody CourseReq req) {
		return courseService.dropCourses(req.getStudentId(), req.getCourseCodeSet());
	}

}
