package com.example.demo_course.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.demo_course.constants.CourseRtnCode;
import com.example.demo_course.entity.Course;
import com.example.demo_course.entity.Students;
import com.example.demo_course.repository.CourseDao;
import com.example.demo_course.repository.StudentDao;
import com.example.demo_course.service.ifs.CourseService;
import com.example.demo_course.vo.CourseResp;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private StudentDao studentDao;

	/* �Ыؽҵ{ */
	@Override
	public CourseResp createCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit) {

		// �P�_ ��J���e���榡
		LocalTime strLocalTime = checkTimeFormat(courseStrtime);
		LocalTime endLocalTime = checkTimeFormat(courseEndtime);
		if (checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit) != null) {
			return checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
		}

		// �P�_�b �ҵ{DB�� �O�_�w�s�b�o�ӽҵ{�N�X, �Y�S���N�Ыطs�ҵ{
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			return new CourseResp(CourseRtnCode.COURSE_EXISTED.getMessage());
		} else {
			Course course = new Course(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
			courseDao.save(course);
			return new CourseResp(course, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		}
	}

	/* �ק�ҵ{ */
	@Override
	public CourseResp updateCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit) {

		// �P�_��J���e���榡
		LocalTime strLocalTime = checkTimeFormat(courseStrtime);
		LocalTime endLocalTime = checkTimeFormat(courseEndtime);
		if (checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit) != null) {
			return checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
		}

		// �P�_�b �ҵ{DB�� �O�_�w�s�b�o�ӽҵ{�N�X�A�Y���N�ק���
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			// �ק��ơA�s�л\��
			Course course = courseOp.get();
			course.updateCourse(courseName, days, strLocalTime, endLocalTime, credit);
			courseDao.save(course);
			return new CourseResp(course, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* �R���ҵ{ */
	@Override
	public CourseResp deleteCourse(String courseCode) {

		// �P�_���e���o����
		if (!StringUtils.hasText(courseCode)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ҵ{DB�� �O�_�w�s�b�o�ӽҵ{�N�X�A�Y���N�R�����
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			courseDao.delete(course);
			return new CourseResp(course, CourseRtnCode.DELETE_SUCCESSFUL.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* �d�߽ҵ{(�Ұ�N�X) */
	@Override
	public CourseResp getCourseByCode(String courseCode) {

		// �P�_���e���o����
		if (!StringUtils.hasText(courseCode)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ҵ{DB�� �O�_�w�s�b�o�ӽҵ{�N�X�A�Y�����N�h���ҵ{��T�X��
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			return new CourseResp(course, CourseRtnCode.DATA_IS_FOUND.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* �d�߽ҵ{(�Ұ�W��) */
	@Override
	public CourseResp getCourseByName(String courseName) {

		// �P�_���e���o����
		if (!StringUtils.hasText(courseName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ҵ{DB�� �O�_�w�s�b�o�ӽҵ{�W�١A�Y�����N�h���ҵ{��T�X��
		List<Course> courseNameList = courseDao.findByCourseName(courseName);
		if (CollectionUtils.isEmpty(courseNameList)) {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
		return new CourseResp(courseNameList, CourseRtnCode.DATA_IS_FOUND.getMessage());
	}

	/* �d�ߤw��W���ҵ{(�ǥ�ID)-->��ܾǸ��B�m�W�B�ҵ{�N�X..�� */
	@Override
	public CourseResp getCourseByStudentId(String studentId) {

		// �P�_���e���o����
		if (!StringUtils.hasText(studentId)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ǥ�DB�� �O�_�s�b�o�Ӿǥ�ID
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();

			// �P�_ �ǥͿ�Ҥ��e�O�_�����
			if (!StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student, CourseRtnCode.COURSE_IS_EMPTY.getMessage());
			}

			// �Q�Τ�k-->getCourseCodeSet�A�N�ǥ�DB�̭� String���A���Ұ��Ƥ� �ҵ{�N�X��X�ӡA�åB�s�i�hSet��
			Set<String> oldCourseCodeSet = getCourseCodeSet(student);

			// ��key��(courseCode)��Set�A�h�Ұ�DB�����ŦX���Ұ�A�æ^��List
			List<Course> courseList = courseDao.findAllByCourseCodeIn(oldCourseCodeSet);
			return new CourseResp(student, courseList, CourseRtnCode.DATA_IS_FOUND.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* �Ыؾǥ͸�� */
	@Override
	public CourseResp createStudents(String studentId, String studentName) {

		// �P�_���e���o����
		if (!StringUtils.hasText(studentId) || !StringUtils.hasText(studentName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// �P�_�b �ǥ�DB�� �O�_�w�s�b���, �Y�S���N�Ыطs���
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			return new CourseResp(CourseRtnCode.STUDENT_EXISTED.getMessage());
		} else {
			Students student = new Students(studentId, studentName);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		}
	}

	/* �ק�ǥ͸�� */
	@Override
	public CourseResp updateStudents(String studentId, String studentName) {

		// �P�_���e���o����
		if (!StringUtils.hasText(studentId) || !StringUtils.hasText(studentName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID�A�Y���N�ק���
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			// �ק��ơA�s�л\��
			Students student = studentOp.get();
			student.setStudentName(studentName);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* �R���ǥ͸�� */
	@Override
	public CourseResp deleteStudents(String studentId) {

		// �P�_���e���o����
		if (!StringUtils.hasText(studentId)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID�A�Y���N�R�����
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();
			studentDao.delete(student);
			return new CourseResp(student, CourseRtnCode.DELETE_SUCCESSFUL.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* ��� */
	@Override
	public CourseResp seletionCourses(String studentId, Set<String> courseCodeSet) {

		// �P�_��J���e�O�_����
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();

			// �P�_ �o�ӾǥͬO�_�w���Ұ��ƤF�A�Y�w�t��ƪ��ܴN��^"�w�g��L�ҡA�n�ܰʽЦܥ[�h��"���T��
			if (StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student.getCourseData(), CourseRtnCode.ALREADY_CHOOSE_COURSE.getMessage());
			}

			// �P�_ �ҿ�ҵ{�O�_�� �ҵ{DB�̦��Ыت��ҵ{
			CourseResp checkResult = checkDbCourseExist(courseCodeSet);
			if (checkResult != null) {
				return checkResult;
			}

			// �ϥΤ�k-->getCourseList�A�z�� ��ҽҰ󪺱���:(1)�L�k�ײ�2���Φh���ۦP�ҵ{�W�٪��ҵ{�A(2)�Ǥ�����W�L10�Ǥ��A(3)����İ�ɶ�
			CourseResp res = getCourseList(courseCodeSet);
			List<Course> courseList = res.getCourseCodeList();
			if(courseList.isEmpty()) {
				return res;
			}

			// �N�z��L ���Ұ�N���a�J �Ұ�DB�A��^�ŦX���Ҧ��Ұ�
			Map<String, String> courseMap = new HashMap<>();
			for (Course course : courseList) {
				courseMap.put(course.getCourseCode(), course.getCourseName());// ��Ҫ�Map(�ҵ{�N�X�A�ҵ{�W��)
			}

			// �N�̫�s�n��Map���(courseMap) ��^String �å[�^�i �ǥ�DB�̭�
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* �[�� */
	@Override
	public CourseResp addCourses(String studentId, Set<String> courseCodeSet) {

		// �P�_���e�O�_����
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();

			// �P�_ �ǥͿ�Ҥ��e�O�_����ơA�Y�S���N��^"��Ҥ��� �~�వ�[��"���T��
			if (!StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student, CourseRtnCode.COURSE_IS_EMPTY.getMessage());
			}

			// �P�_ �ҥ[��ҵ{�O�_�� �ҵ{DB�̦��Ыت��ҵ{
			CourseResp checkResult = checkDbCourseExist(courseCodeSet);
			if (checkResult != null) {
				return checkResult;
			}

			// �Q�Τ�k-->getCourseCodeSet�A�N�ǥ�DB�̭� String���A���Ұ��Ƥ� �ҵ{�N�X��X�ӡA�åB�s�i�hSet��
			Set<String> oldCourseCodeSet = getCourseCodeSet(student);

			// �N �w��L/�n�[�諸�Ұ�N���a�J �Ұ�DB�A��^�ŦX���Ҧ��Ұ�List
			List<Course> oldCourseList = courseDao.findAllByCourseCodeIn(oldCourseCodeSet);// �w��L
			List<Course> addCourseList = courseDao.findAllById(courseCodeSet); // �n�[��

			// �w��L���ҵ{�N�X(�ǥ�DB) Vs. �n�s�[���ҵ{�N�X, �P�_ �ҵ{�N�X�Τ���ۦP
			for (Course oldCourse : oldCourseList) {
				for (Course course : addCourseList) {
					if (oldCourse.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
						return new CourseResp(oldCourse.getCourseCode(), CourseRtnCode.COURSE_EXISTED.getMessage());
					}
				}
			}

			Set<String> allCourseCode = new HashSet<>(); // �[�`�������ҵ{�N�X
			allCourseCode.addAll(oldCourseCodeSet); // �w��L���Ұ�N�X
			allCourseCode.addAll(courseCodeSet); // �Q�n�[�諸�Ұ�N�X(�w�z��L��)

			// �ϥΤ�k-->getCourseList�A�z�� �[�ҽҰ󪺱���:(1)�L�k�ײ�2���Φh���ۦP�ҵ{�W�٪��ҵ{�A(2)�Ǥ�����W�L10�Ǥ��A(3)����İ�ɶ�
			CourseResp res = getCourseList(allCourseCode);
			List<Course> courseList = res.getCourseCodeList();
			if(courseList.isEmpty()) {
				return res;
			}

			// �N�z��L ���Ұ�N���a�J �Ұ�DB�A��^�ŦX���Ҧ��Ұ�
			Map<String, String> courseMap = new HashMap<>();
			for (Course course : courseList) {
				courseMap.put(course.getCourseCode(), course.getCourseName());// ��Ҫ�Map(�ҵ{�N�X�A�ҵ{�W��)
			}

			// �N�̫�s�n��Map���(courseMap) ��^String �å[�^�i �ǥ�DB�̭�
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* �h�� */
	@Override
	public CourseResp dropCourses(String studentId, Set<String> courseCodeSet) {

		// �P�_���e�O�_����
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// �P�_�ǥͬO�_�s�b
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();

			// �P�_ �ǥͿ�Ҥ��e�O�_����ơA�Y�S���N��^"��Ҥ��� �~�వ�h��"���T��
			if (!StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student, CourseRtnCode.COURSE_IS_EMPTY.getMessage());
			}

			// �Q�Τ�k-->getCourseCodeSet�A�N�ǥ�DB�̭� String���A���Ұ��Ƥ� �ҵ{�N�X��X�ӡA�åB�s�i�hSet��
			Set<String> oldCourseCodeSet = getCourseCodeSet(student);

			// �N��J���Ұ�N���a�J �Ұ�DB�A��^�ŦX���Ҧ��Ұ�List
			List<Course> oldCourseList = courseDao.findAllByCourseCodeIn(oldCourseCodeSet);// �w��
			List<Course> courseList = courseDao.findAllById(courseCodeSet);// �h��
			
			// �w��L���ҵ{�N�X(�ǥ�DB) Vs. �n�h�諸�ҵ{�N�X�A��X�n�h�諸��
			Set<String> finallyCourseCodeSet = new HashSet<>();
			for (Course oldCourse : oldCourseList) { // �w��
				finallyCourseCodeSet.add(oldCourse.getCourseCode());
			}

			for (Course dropCourse : courseList) { // �h��
				if (finallyCourseCodeSet.contains(dropCourse.getCourseCode())) {
					finallyCourseCodeSet.remove(dropCourse.getCourseCode());
				}
			}

			// �N �z��n���ҵ{�N�X �a�J �Ұ�DB�A��^�ŦX���Ҧ��Ұ�List
			List<Course> finallyCourseList = courseDao.findAllByCourseCodeIn(finallyCourseCodeSet);
			
			Map<String, String> courseMap = new HashMap<>(); 
			for (Course finallyItem : finallyCourseList) {
				// �����ҵ{��Map(�ҵ{�N�X�A�ҵ{�W��)
				courseMap.put(finallyItem.getCourseCode(), finallyItem.getCourseName());
			}

			// �N�̫�s�n��Map���(courseMap) ��^String �å[�^�i �ǥ�DB�̭�
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/************************************************/

	/* �P�_�ҿ�ҵ{�O�_�� �ҵ{DB�̦��Ыت��ҵ{ */
	private CourseResp checkDbCourseExist(Set<String> courseCodeSet) {

		List<Course> courseList = courseDao.findAllById(courseCodeSet);// ���[A4,A5,A6]
		Set<String> oldCodeSet = new HashSet<>();
		Set<String> noExistCodeSet = new HashSet<>();

		for (Course existItem : courseList) {// �u�s�b���ҵ{�N�X[A4,A5]
			oldCodeSet.add(existItem.getCourseCode());
		}

		for (String noExistItem : courseCodeSet) {// ��ҥN�X[A4,A5,A6]
			if (!oldCodeSet.contains(noExistItem)) {
				noExistCodeSet.add(noExistItem);// ���s�b���N�X[A6]
			}
		}

		// �p�G ���s�b���N�X��Set�̭����Ȫ��ܡA�N�N���s�b���N�Xshow���ϥΪ̬�
		if (!CollectionUtils.isEmpty(noExistCodeSet)) {
			return new CourseResp(noExistCodeSet, CourseRtnCode.COURSE_NOT_EXISTED.getMessage());
		}
		return null;
	}

	/* �P�_���o���šB�Ǥ��B�P���B�ɶ��榡 */
	private CourseResp checkParam(String courseCode, String courseName, String days, LocalTime courseStrtime,
			LocalTime courseEndtime, int credit) {

		if (!StringUtils.hasText(courseCode) || !StringUtils.hasText(courseName)) {// �P�_���e���o����
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		} else if (credit <= 0 || credit > 3) { // �P�_�Ǥ����o�p��0�B�j��3
			return new CourseResp(CourseRtnCode.CREDIT_ERROR.getMessage());
		} else if (checkDays(days)) { // �P�_�P���O�_��g���~
			return new CourseResp(CourseRtnCode.DAY_ERROR.getMessage());
		} else if (courseStrtime == null || courseEndtime == null || courseStrtime.isAfter(courseEndtime)) { // �P�_�ɶ��O�_�ŦX�榡�A�ö}�l�ɶ�����ߩ󵲧��ɶ�
			return new CourseResp(CourseRtnCode.TIME_ERROR.getMessage());
		}
		return null;
	}

	/* �ˬd�ɶ��O�_�ŦX�榡 */
	public LocalTime checkTimeFormat(String timestr) {
		try {
			// �ɶ��r���ഫ��LocalTime
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
			LocalTime localTime = LocalTime.parse(timestr, formatter);
			return localTime;
		} catch (Exception e) {
			return null;
		}
	}

	/* �ˬd����O�_�ŦX�榡 */
	private boolean checkDays(String days) {
		List<String> week = Arrays.asList("�@", "�G", "�T", "�|", "��", "��", "��");
		return !week.contains(days);
	}

	/* �ˬd�O�_�İ�(�P���B�ɶ��ۦP) */
	private boolean checkClashCourse(List<Course> dayTimeList) {
		// i�q�}�C�Y�}�l���U�h���
		for (int i = 0; i < dayTimeList.size(); i++) {
			Course dayTime1 = dayTimeList.get(i);
			// j�qi+1�}�C�Y�}�l���U�h���Aj>i;�קK����ۤv
			for (int j = i + 1; j < dayTimeList.size(); j++) {
				Course dayTime2 = dayTimeList.get(j);
				// ���P�� �ۦP���ɭ�
				if (dayTime1.getDays().equalsIgnoreCase(dayTime2.getDays())) {
					// �P�_���ɶ������ۦP��
					if (dayTime2.getCourseStrtime().equals(dayTime1.getCourseStrtime())) {
						return true;
					} else if (dayTime2.getCourseEndtime().equals(dayTime1.getCourseEndtime())) {
						return true;
					}
					// �P�_�ɶ��i���\���d��
					if (dayTime2.getCourseStrtime().isAfter(dayTime1.getCourseStrtime())
							|| dayTime2.getCourseStrtime().isBefore(dayTime1.getCourseEndtime())) {
						return true;
					}
					if (dayTime2.getCourseEndtime().isAfter(dayTime1.getCourseStrtime())
							|| dayTime2.getCourseEndtime().isBefore(dayTime1.getCourseEndtime())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/* �N�ǥͪ���Ҹ�Ƨ�X�ӡA�åB�qString�নMap(�u�skey��-->�ҵ{�N�X) */
	private Set<String> getCourseCodeSet(Students student) {
		Set<String> oldCourseCodeSet = new HashSet<>();// �Nkey��(courseCode)��X�ӡA�åB�s�i�hSet�}�C��

		String[] studentCourseArray = student.getCourseData().split(",");
		// (String)courseData-->[A1=Math, A3=history]
		for (String item : studentCourseArray) {
			String str = item.trim(); // �N�C�ӵ��G����trim�h���ťաA�A�@�֦^��
			String[] info = str.split("="); // A1=Math A3=history
			oldCourseCodeSet.add(info[0]); // �Ұ�N�X�s�iSet�}�C
		}
		return oldCourseCodeSet;
	}

	/* �P�_ �[��Ұ󪺱���:(1)�L�k�ײ�2���Φh���ۦP�ҵ{�W�٪��ҵ{�A(2)�Ǥ�����W�L10�Ǥ��A(3)����İ�ɶ� */
	private CourseResp getCourseList(Set<String> courseCodeSet) {

		List<Course> courseList = courseDao.findAllById(courseCodeSet);

		int credits = 0;
		List<String> courseNameList = new ArrayList<>(); // DB�ŦXid���Ҧ��ҵ{�W��
		Set<String> courseNameSet = new HashSet<>(); // �h���ۦP���Ұ�W��

		for (Course course : courseList) {
			// ��Ҫ��Ұ�̡A�N�W�٩�JList�PSet��(���P�_�O�_�����ƪ��Ұ�)
			courseNameList.add(course.getCourseName());
			courseNameSet.add(course.getCourseName());
			// �p��Ǥ�
			credits += course.getCredit();
		}
			// �P�_�L�k�ײ�2���Φh���ۦP�ҵ{�W�٪��ҵ{�A�B�Ǥ�����W�L10�Ǥ�
			if (courseNameList.size() != courseNameSet.size()) {
				return new CourseResp(CourseRtnCode.CLASH_COURSE.getMessage());
			} else if (credits > 10) {
				return new CourseResp(CourseRtnCode.OVER_CREDIT.getMessage());		
		}

		// �P�_ �Q�[���Ұ�O�_�İ�ɶ�
		if (checkClashCourse(courseList)) {
			return new CourseResp(CourseRtnCode.CLASH_COURSE_DAYTIME.getMessage());
		}

		CourseResp res = new CourseResp();
		res.setCourseCodeList(courseList);
		return res;
	}

}
