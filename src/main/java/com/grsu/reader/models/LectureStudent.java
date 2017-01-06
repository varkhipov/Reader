package com.grsu.reader.models;

public class LectureStudent {
	private Lecture lecture;
	private Student student;

	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "LectureStudent{" +
				"lecture=" + lecture +
				", student=" + student +
				'}';
	}
}
