package com.example.demo_course.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	/* 判斷所選課程是否為 課程DB裡有創建的課程 */
	private CourseResp checkDbCourseExist(Set<String> courseCodeSet) {
		List<Course> courseList = courseDao.findAllById(courseCodeSet);// 選課[A4,A5,A6]
		Set<String> oldCodeSet = new HashSet<>();
		Set<String> noExistCodeSet = new HashSet<>();
		for (Course existItem : courseList) {// 只存在的課程代碼[A4,A5]
			oldCodeSet.add(existItem.getCourseCode());
		}
		for (String noExistItem : courseCodeSet) {// 選課代碼[A4,A5,A6]
			if (!oldCodeSet.contains(noExistItem)) {
				noExistCodeSet.add(noExistItem);// 不存在的代碼[A6]
			}
		}
		if (!CollectionUtils.isEmpty(noExistCodeSet)) {
			return new CourseResp(noExistCodeSet, CourseRtnCode.COURSE_NOT_EXISTED.getMessage());
		}
		return null;
	}

	/* 判斷不得為空、學分、星期、時間格式 */
	private CourseResp checkParam(String courseCode, String courseName, String days, LocalTime courseStrtime,
			LocalTime courseEndtime, int credit) {
		if (!StringUtils.hasText(courseCode) || !StringUtils.hasText(courseName)) {// 判斷內容不得為空
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		} else if (credit <= 0 || credit > 3) { // 判斷學分不得小於0且大於3
			return new CourseResp(CourseRtnCode.CREDIT_ERROR.getMessage());
		} else if (checkDays(days)) { // 判斷星期是否填寫錯誤
			return new CourseResp(CourseRtnCode.DAY_ERROR.getMessage());
		} else if (courseStrtime == null || courseEndtime == null || courseStrtime.isAfter(courseEndtime)) { // 判斷時間是否符合格式，並開始時間不能晚於結束時間
			return new CourseResp(CourseRtnCode.TIME_ERROR.getMessage());
		}
		return null;
	}

	/* 檢查時間是否符合格式 */
	public LocalTime checkTimeFormat(String timestr) {
		try {
			// 時間字串轉換成LocalTime
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
			LocalTime localTime = LocalTime.parse(timestr, formatter);
			return localTime;
		} catch (Exception e) {
			return null;
		}
	}

	/* 檢查日期是否符合格式 */
	private boolean checkDays(String days) {
		List<String> week = Arrays.asList("一", "二", "三", "四", "五", "六", "日");
		return !week.contains(days);
	}

	/* 檢查是否衝堂(星期、時間相同) */
	private boolean checkClashCourse(List<Course> dayTimeList) {
		// i從陣列頭開始往下去比對
		for (int i = 0; i < dayTimeList.size(); i++) {
			Course dayTime1 = dayTimeList.get(i);
			// j從i+1陣列頭開始往下去比對，j>i;避免對比到自己
			for (int j = i + 1; j < dayTimeList.size(); j++) {
				Course dayTime2 = dayTimeList.get(j);
				// 比對星期 相同的時候
				if (dayTime1.getDays().equalsIgnoreCase(dayTime2.getDays())) {
					// 判斷兩堂時間完全相同時
					if (dayTime2.getCourseStrtime().equals(dayTime1.getCourseStrtime())) {
						return true;
					} else if (dayTime2.getCourseEndtime().equals(dayTime1.getCourseEndtime())) {
						return true;
					}
					// 判斷時間可允許的範圍
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

//	private boolean checkClashCourse1(int str1, int end1, int str2, int end2) {
//		return str1 >= end2 || end1 <= str2;
//	}

	/****************************************/
	/* 創建課程 */
	@Override
	public CourseResp createCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit) {

		LocalTime strLocalTime = checkTimeFormat(courseStrtime);
		LocalTime endLocalTime = checkTimeFormat(courseEndtime);
		if (checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit) != null) {
			return checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
		}
		// 判斷是否已存在這個課程代碼, 若沒有就創建新課程
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			return new CourseResp(CourseRtnCode.COURSE_EXISTED.getMessage());
		} else {
			Course course = new Course(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
			courseDao.save(course);
			return new CourseResp(course, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		}
	}

	/* 修改課程 */
	@Override
	public CourseResp updateCourse(String courseCode, String courseName, String days, String courseStrtime,
			String courseEndtime, int credit) {

		LocalTime strLocalTime = checkTimeFormat(courseStrtime);
		LocalTime endLocalTime = checkTimeFormat(courseEndtime);
		if (checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit) != null) {
			return checkParam(courseCode, courseName, days, strLocalTime, endLocalTime, credit);
		}
		// 找到課程代碼
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			// 修改資料，新覆蓋舊
			Course course = courseOp.get();
			course.updateCourse(courseName, days, strLocalTime, endLocalTime, credit);
			courseDao.save(course);
			return new CourseResp(course, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* 刪除課程 */
	@Override
	public CourseResp deleteCourse(String courseCode) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(courseCode)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否已存在這個課程代碼，若有就刪除資料
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			courseDao.delete(course);
			return new CourseResp(course, CourseRtnCode.DELETE_SUCCESSFUL.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* 查詢課程(課堂代碼) */
	@Override
	public CourseResp getCourseByCode(String courseCode) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(courseCode)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否已存在這個課程代碼，若有找到就去取課程資訊出來
		Optional<Course> courseOp = courseDao.findById(courseCode);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			return new CourseResp(course, CourseRtnCode.DATA_IS_FOUND.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* 查詢課程(課堂名稱) */
	@Override
	public CourseResp getCourseByName(String courseName) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(courseName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否已存在這個課程名稱，若有找到就去取課程資訊出來
		List<Course> courseNameList = courseDao.findByCourseName(courseName);
		if (CollectionUtils.isEmpty(courseNameList)) {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
		return new CourseResp(courseNameList, CourseRtnCode.DATA_IS_FOUND.getMessage());
	}

	/* 已選上課程查詢(學生ID)-->顯示學號、姓名、課程代碼..等 */
	@Override
	public CourseResp getCourseByStudentId(String studentId) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(studentId)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否存在這個學生ID
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();
			// 判斷選課內容是否有資料
			if (!StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student, CourseRtnCode.COURSE_IS_EMPTY.getMessage());
			}
			// 將學生的選課資料找出來，並且從String轉成Map
			Map<String, String> courseDataMap = new HashMap<>();
			// courseData-->[A1=Math, A3=history]
			String[] courseArray = student.getCourseData().split(",");
			for (String item : courseArray) {
				String str = item.trim();// 將每個結果都用trim去掉空白，再一併回傳
				// A1=Math A3=history
				String[] info = str.split("=");
				courseDataMap.put(info[0], info[1]);
			}
			// 將key值(courseCode)找出來，並且存進去Set陣列裡
			Set<String> courseCodeSet = new HashSet<>();
			for (Entry<String, String> courseId : courseDataMap.entrySet()) {
				courseCodeSet.add(courseId.getKey());
			}
			// 用key值(courseCode)的Set陣列，去Course的DB內找到符合的課堂，並回傳List
			List<Course> courseList = courseDao.findAllByCourseCodeIn(courseCodeSet);
			return new CourseResp(student, courseList, CourseRtnCode.DATA_IS_FOUND.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* 創建學生資料 */
	@Override
	public CourseResp createStudents(String studentId, String studentName) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(studentId) || !StringUtils.hasText(studentName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否已存在資料, 若沒有就創建新資料
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			return new CourseResp(CourseRtnCode.STUDENT_EXISTED.getMessage());
		} else {
			Students student = new Students(studentId, studentName);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		}
	}

	/* 修改學生資料 */
	@Override
	public CourseResp updateStudents(String studentId, String studentName) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(studentId) || !StringUtils.hasText(studentName)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 找到學生ID
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			// 修改資料，舊覆蓋新
			Students student = studentOp.get();
			student.updateStudents(studentName);
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* 刪除學生資料 */
	@Override
	public CourseResp deleteStudents(String studentId) {
		// 判斷內容不得為空
		if (!StringUtils.hasText(studentId)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}
		// 判斷是否已存在這個學生ID，若有就刪除資料
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();
			studentDao.delete(student);
			return new CourseResp(student, CourseRtnCode.DELETE_SUCCESSFUL.getMessage());
		}
		return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
	}

	/* 選課 */
	@Override
	public CourseResp seletionCourses(String studentId, Set<String> courseCodeSet) {
		int credits = 0;
		Map<String, String> courseMap = new HashMap<>(); // Map(課程代碼，課程名稱)
		List<String> courseNameList = new ArrayList<>(); // DB符合id的所有課程名稱
		Set<String> courseNameSet = new HashSet<>(); // 去掉相同的課堂名稱

		// 判斷內容是否為空
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// 判斷學生是否存在
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {
			Students student = studentOp.get();

			// 判斷是否已經有課堂資料了，若已含資料的話就返回"已經選過課，要變動請至加退選"
			if (StringUtils.hasText(student.getCourseData())) {
				return new CourseResp(student.getCourseData(), CourseRtnCode.ALREADY_CHOOSE_COURSE.getMessage());
			}

			// (TODO)先判斷所選課程是否為 課程DB裡有創建的課程
			CourseResp checkResult = checkDbCourseExist(courseCodeSet);
			if (checkResult != null) {
				return checkResult;
			}

			// 將想加的課堂代號帶入 課堂DB，返回符合的所有課堂
			List<Course> courseList = courseDao.findAllById(courseCodeSet);
			for (Course course : courseList) {
				// 選課的Map(課程代碼，課程名稱)
				courseMap.put(course.getCourseCode(), course.getCourseName());
				// 選課的課堂們，將名稱放入List與Set中(為判斷是否有重複的課堂)
				courseNameList.add(course.getCourseName());
				courseNameSet.add(course.getCourseName());
				// 計算學分
				credits += course.getCredit();

				// 判斷無法修習2門或多門相同課程名稱的課程，且學分不能超過10學分
				if (courseNameList.size() != courseNameSet.size()) {
					return new CourseResp(CourseRtnCode.CLASH_COURSE.getMessage());
				} else if (credits > 10) {
					return new CourseResp(CourseRtnCode.OVER_CREDIT.getMessage());
				}
			}

			// 判斷是否衝堂
			if (checkClashCourse(courseList)) {
				return new CourseResp(CourseRtnCode.CLASH_COURSE_DAYTIME.getMessage());
			}

			// Map{"A3":"history"}用String去接，-->[A3=history]會包含中括號，所以要將前後的中括號用位置(.substring())去做刪掉
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);// 上面將集合轉回字串，並且已經去掉前後中括號之後，這邊再將值set回去
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.CREATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* 加選 */
	@Override
	public CourseResp addCourses(String studentId, Set<String> courseCodeSet) {
		int credits = 0;
		Map<String, String> courseMap = new HashMap<>(); // 全部的Map(課程代碼，課程名稱)
		Map<String, String> studentDBMap = new HashMap<>();// 學生DB 內已選課的Map(課程代碼，課程名稱)
		Set<String> oldCourseCodeSet = new HashSet<>();// 已選課的課程代碼

		// 判斷內容是否為空
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// 判斷學生是否存在
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {

			// 找出 學生DB裡面的課堂是否有選課資料，並且將String轉為Map方便後續比較
			Students student = studentOp.get();
			if (StringUtils.hasText(student.getCourseData())) {

				String[] studentCourseArray = student.getCourseData().split(",");
				// (String)courseData-->[A1=Math, A3=history]
				for (String item : studentCourseArray) {
					String str = item.trim();// 將每個結果都用trim去掉空白，再一併回傳
					// A1=Math A3=history
					String[] info = str.split("=");
					studentDBMap.put(info[0], info[1]);// 轉成Map
					oldCourseCodeSet.add(info[0]);// 課堂代碼存進Set陣列
				}
			}
//TODO
			if (checkDbCourseExist(courseCodeSet) != null) {// 先判斷所選課程是否為 課程DB裡有創建的課程
				return checkDbCourseExist(courseCodeSet);
			}

			// 將 已選過的課堂代號帶入 課堂DB，返回符合的所有課堂的List
			List<Course> oldCourseList = courseDao.findAllByCourseCodeIn(oldCourseCodeSet);// 已選過
			List<Course> courseList = courseDao.findAllById(courseCodeSet);
			// 已選過的課程代碼(學生DB) Vs. 要新加的課程代碼, 判斷課程代碼及不能相同名稱
			for (Course oldCourse : oldCourseList) {
				for (Course course : courseList) {
					if (oldCourse.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
						return new CourseResp(oldCourse.getCourseCode(), CourseRtnCode.COURSE_EXISTED.getMessage());
					} else if (oldCourse.getCourseName().equalsIgnoreCase(course.getCourseName())) {
						return new CourseResp(oldCourse.getCourseName(), CourseRtnCode.CLASH_COURSE.getMessage());
					}
				}
			}
			Set<String> allCourseCode = new HashSet<>();// 加總全部的課程代碼
			allCourseCode.addAll(oldCourseCodeSet);// 已選過的課堂代碼
			allCourseCode.addAll(courseCodeSet);// 想要加選的課堂代碼

			// 將 加總全部的課程代碼 帶入 課堂DB，返回符合的所有課堂的List
			List<Course> allCourseList = courseDao.findAllByCourseCodeIn(allCourseCode);
			for (Course allCourse : allCourseList) {
				// 全部課程的Map(課程代碼，課程名稱)
				courseMap.put(allCourse.getCourseCode(), allCourse.getCourseName());
				// 計算全部課程的學分
				credits += allCourse.getCredit();
				// 判斷學分不能超過10學分
				if (credits > 10) {
					return new CourseResp(CourseRtnCode.OVER_CREDIT.getMessage());
				}
			}

			// 判斷是否衝堂(加選的課程)
			if (checkClashCourse(allCourseList)) {
				return new CourseResp(CourseRtnCode.CLASH_COURSE_DAYTIME.getMessage());
			}

			// Map{"A3":"history"}用String去接，-->[A3=history]會包含中括號，所以要將前後的中括號用位置(.substring())去做刪掉
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);// 上面將集合轉回字串，並且已經去掉前後中括號之後，這邊再將值set回去
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

	/* 退選 */
	@Override
	public CourseResp dropCourses(String studentId, Set<String> courseCodeSet) {

		Map<String, String> courseMap = new HashMap<>(); // 全部的Map(課程代碼，課程名稱)
		Map<String, String> studentDBMap = new HashMap<>();// 學生DB 內已選課的Map(課程代碼，課程名稱)
		Set<String> oldCourseCodeSet = new HashSet<>();// 已選課的課程代碼

		// 判斷內容是否為空
		if (!StringUtils.hasText(studentId) || CollectionUtils.isEmpty(courseCodeSet)) {
			return new CourseResp(CourseRtnCode.VALUE_REQUIRED.getMessage());
		}

		// 判斷學生是否存在
		Optional<Students> studentOp = studentDao.findById(studentId);
		if (studentOp.isPresent()) {

			// 找出 學生DB裡面的課堂是否有選課資料，並且將String轉為Map方便後續比較
			Students student = studentOp.get();
			if (StringUtils.hasText(student.getCourseData())) {

				String[] studentCourseArray = student.getCourseData().split(",");
				// (String)courseData-->[A1=Math, A3=history]
				for (String item : studentCourseArray) {
					String str = item.trim();// 將每個結果都用trim去掉空白，再一併回傳
					// A1=Math A3=history
					String[] info = str.split("=");
					studentDBMap.put(info[0], info[1]);// 轉成Map
					oldCourseCodeSet.add(info[0]);// 課堂代碼存進Set陣列
				}
			} else {
				return new CourseResp(student, CourseRtnCode.COURSE_IS_EMPTY.getMessage());
			}

			if (checkDbCourseExist(courseCodeSet) != null) {// 先判斷所選課程是否為 課程DB裡有創建的課程
				return checkDbCourseExist(courseCodeSet);
			}

			// 將輸入的課堂代號帶入 課堂DB，返回符合的所有課堂的List
			List<Course> oldCourseList = courseDao.findAllByCourseCodeIn(oldCourseCodeSet);// 已選
			Set<String> oldCourseSet = new HashSet<>();// 已選的課堂代號陣列
			List<Course> courseList = courseDao.findAllById(courseCodeSet);
			// 已選過的課程代碼(學生DB) Vs. 要退選的課程代碼
			for (Course oldCourse : oldCourseList) {// 已選
				oldCourseSet.add(oldCourse.getCourseCode());
			}
			for (Course dropCourse : courseList) {// 退選
				if (oldCourseSet.contains(dropCourse.getCourseCode())) {
					oldCourseSet.remove(dropCourse.getCourseCode());
				}
			}
			// 將 已退選的課程代碼 帶入 課堂DB，返回符合的所有課堂的List
			List<Course> finallyCourseList = courseDao.findAllByCourseCodeIn(oldCourseSet);
			for (Course finallyCourse : finallyCourseList) {
				// 全部課程的Map(課程代碼，課程名稱)
				courseMap.put(finallyCourse.getCourseCode(), finallyCourse.getCourseName());
			}

			// Map{"A3":"history"}用String去接，-->[A3=history]會包含中括號，所以要將前後的中括號用位置(.substring())去做刪掉
			String courseMapStr = courseMap.toString().substring(1, courseMap.toString().length() - 1);
			student.setCourseData(courseMapStr);// 上面將集合轉回字串，並且已經去掉前後中括號之後，這邊再將值set回去
			studentDao.save(student);
			return new CourseResp(student, CourseRtnCode.UPDATE_SUCCESSFUL.getMessage());
		} else {
			return new CourseResp(CourseRtnCode.DATA_NOT_FOUND.getMessage());
		}
	}

}
