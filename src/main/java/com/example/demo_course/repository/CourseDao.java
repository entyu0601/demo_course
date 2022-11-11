package com.example.demo_course.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_course.entity.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, String> {

	List<Course> findByCourseName(String courseName);
	
	
	List<Course> findAllByCourseCodeIn(Set<String> courseCodeSet);
	
	
}
