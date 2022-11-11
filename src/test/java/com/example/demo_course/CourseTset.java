package com.example.demo_course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo_course.entity.Course;
import com.example.demo_course.service.ifs.CourseService;
import com.example.demo_course.vo.CourseReq;
import com.example.demo_course.vo.CourseResp;

@SpringBootTest
public class CourseTset {
	
	@Autowired
	private CourseService courseService;
//	
//	@Test
//	public void course() {
//		CourseResp course = courseService.createCourse("A00", "Chinese", "¥|", "09:00", "11:00", 3);
//		Assert.isTrue(course.equals("A00"), "Error");
//	}
	
	
	
	@Test
	public void getCourseByName() {
		CourseResp course = courseService.getCourseByName("history");
		Assert.isTrue(course.equals("hostory"), "Error");
//		 courseService.getCourseByName(req.getCourse_name()¡A;¡A
	}
}
