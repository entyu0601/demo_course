package com.example.demo_course.service.ifs;

import java.util.Set;

import com.example.demo_course.vo.CourseResp;

public interface CourseService {

	// �Ыؽҵ{
	public CourseResp createCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit);

	// �ק�ҵ{
	public CourseResp updateCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit);

	// �R���ҵ{
	public CourseResp deleteCourse(String courseCode);

	// �d�߽ҵ{(�Ұ�N�X)
	public CourseResp getCourseByCode(String courseCode);

	// �d�߽ҵ{(�Ұ�W��)
	public CourseResp getCourseByName(String courseName);

	// �w��W�ҵ{�d��(�ǥ�ID)
	public CourseResp getCourseByStudentId(String studentId);

	// �Ыؾǥ͸��
	public CourseResp createStudents(String studentId, String studentName);

	// �ק�ǥ͸��
	public CourseResp updateStudents(String studentId, String studentName);

	// �R���ǥ͸��
	public CourseResp deleteStudents(String studentId);

	// ���
	public CourseResp seletionCourses(String studentId, Set<String> courseCodeSet);

	// �[��
	public CourseResp addCourses(String studentId, Set<String> courseCodeSet);

	// �h��
	public CourseResp dropCourses(String studentId, Set<String> courseCodeSet);

}
