package com.example.demo_course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_course.entity.Students;

@Repository
public interface StudentDao extends JpaRepository<Students, String> {

}
