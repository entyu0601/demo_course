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

	/* �Ыؽҵ{ */
	@PostMapping(value = "/api/createCourse")
	public CourseResp course(@RequestBody CourseReq req) {
		return courseService.createCourse(req.getCourseCode(), req.getCourseName(), req.getDays(),
				req.getCourseStrtime(), req.getCourseEndtime(), req.getCredit());
	}

	/* �ק�ҵ{ */
	@PostMapping(value = "/api/updateCourse")
	public CourseResp updateCourse(@RequestBody CourseReq req) {
		return courseService.updateCourse(req.getCourseCode(), req.getCourseName(), req.getDays(),
				req.getCourseStrtime(), req.getCourseEndtime(), req.getCredit());
	}

	/* �R���ҵ{ */
	@PostMapping(value = "/api/deleteCourse")
	public CourseResp deleteCourse(@RequestBody CourseReq req) {
		return courseService.deleteCourse(req.getCourseCode());
	}

	/* �d�߽ҵ{(�Ұ�N�X) */
	@PostMapping(value = "/api/getCourseByCode")
	public CourseResp getCourseByCode(@RequestBody CourseReq req) {
		return courseService.getCourseByCode(req.getCourseCode());
	}

	/* �d�߽ҵ{(�Ұ�W��) */
	@PostMapping(value = "/api/getCourseByName")
	public CourseResp getCourseByName(@RequestBody CourseReq req) {
		return courseService.getCourseByName(req.getCourseName());
	}
	
	/* �d�߽ҵ{(�ǥ�ID) */
	@PostMapping(value = "/api/getCourseByStudentId")
	public CourseResp getCourseByStudentId(@RequestBody CourseReq req) {
		return courseService.getCourseByStudentId(req.getStudentId());
	}

	/* �Ыؾǥ͸�� */
	@PostMapping(value = "/api/createStudents")
	public CourseResp createStudents(@RequestBody CourseReq req) {
		return courseService.createStudents(req.getStudentId(), req.getStudentName());
	}

	/* �ק�ǥ͸�� */
	@PostMapping(value = "/api/updateStudents")
	public CourseResp updateStudents(@RequestBody CourseReq req) {
		return courseService.updateStudents(req.getStudentId(), req.getStudentName());
	}

	/* �R���ǥ͸�� */
	@PostMapping(value = "/api/deleteStudents")
	public CourseResp deleteStudents(@RequestBody CourseReq req) {
		return courseService.deleteStudents(req.getStudentId());
	}

	/* ��� */
	@PostMapping(value = "/api/seletionCourses")
	public CourseResp seletionCourses(@RequestBody CourseReq req) {
		return courseService.seletionCourses(req.getStudentId(), req.getCourseCodeSet());
	}
	
	/* �[�� */
	@PostMapping(value = "/api/addCourses")
	public CourseResp addCourses(@RequestBody CourseReq req) {
		return courseService.addCourses(req.getStudentId(), req.getCourseCodeSet());
	}
	
	/* �h�� */
	@PostMapping(value = "/api/dropCourses")
	public CourseResp dropCourses(@RequestBody CourseReq req) {
		return courseService.dropCourses(req.getStudentId(), req.getCourseCodeSet());
	}

}
