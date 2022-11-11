package com.example.demo_course.constants;

public enum CourseRtnCode {

	VALUE_REQUIRED("400", "Value error(Can not be null or empty)!"), // ���o��J�� �ũάO null
	CREATE_SUCCESSFUL("200", "Created success!"), 					 // �Ыظ�Ʀ��\
	CHOOSE_SUCCESSFUL("200", "Choose success!"), 					 // �Ыظ�Ʀ��\
	UPDATE_SUCCESSFUL("200", "Update data success!"), 				 // �ק��Ʀ��\
	DELETE_SUCCESSFUL("200", "Delete data success!"), 				 // �R����Ʀ��\
	DATA_IS_FOUND("200", "Data is found!"),  						 // �����
	CHOOSE_FAIL("200", "Choose fail!"), 					 		 // �Ыظ�ƥ���
	DAY_ERROR("400", "Day error!"),									 // ��J������~
	CREDIT_ERROR("400", "Credit error!"),							 // ��J�Ǥ����~
	TIME_ERROR("400", "Time error!"),								 // ��J�ɶ����~
	DATA_NOT_FOUND("400", "Data is not found!"),					 // �d�L���
	COURSE_NOT_EXISTED("400", "Course doesn't exist!"),		 		 // ���Ұ�ä��s�b
	CLASH_COURSE_DAYTIME("400", "Course is clash(Days or Time)!"),	 // �ҵ{�İ�(�P���B�ɶ�)
	CLASH_COURSE("400", "Course is clash!"),						 // �ҵ{�İ�
	OVER_CREDIT("400", "Credit is over 10!"),						 // �Ǥ��W�L10
	ALREADY_CHOOSE_COURSE("403", "�w�g��L�ҡA�n�ܰʽЦܥ[�h��!"),	 // �ҵ{�w�s�b
	COURSE_EXISTED("403", "Course is already exist!"),				 // �ҵ{�w�s�b
	STUDENT_EXISTED("403", "Student is already exist!"),		 	 // �ǥͤw�s�b		
	COURSE_IS_EMPTY("400", "�ǥͨèS����Ҹ��!");                    // �ǥͨèS����Ҹ��

	private String code;

	private String message;

	private CourseRtnCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
