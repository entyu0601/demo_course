CREATE TABLE IF NOT EXISTS `course` (
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(20) DEFAULT NULL,
  `days` varchar(20) DEFAULT NULL,
  `course_strtime` time DEFAULT NULL,
  `course_endtime` time DEFAULT NULL,
  `credit` int DEFAULT NULL,
  PRIMARY KEY (`course_code`)
);

CREATE TABLE IF NOT EXISTS `students` (
  `student_id` varchar(20) NOT NULL,
  `student_name` varchar(20) DEFAULT NULL,
  `course_code` varchar(20) DEFAULT NULL,
  `course_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
);