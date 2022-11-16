package com.example.demo_course.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_course.entity.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, String> {

	//查詢課程-->用課堂名稱
	List<Course> findByCourseName(String courseName);
	
	//查詢課程-->用Set所包起來的課堂名稱去查找
	List<Course> findAllByCourseCodeIn(Set<String> courseCodeSet);
	
	
}
