package com.grsu.reader.models;

import com.grsu.reader.entities.Student;

import java.time.LocalTime;

/**
 * Created by pavel on 3/4/17.
 */
public class LessonStudentModel {
	private Integer id;
	public String name;
	public Integer totalSkip;
	private Integer lectureSkip;
	private Integer practicalSkip;
	private Integer labSkip;
	public LocalTime registrationTime;
	private Student student;
	private boolean additional;

	public LessonStudentModel(Student student) {
		this.student = student;
		this.id = student.getId();
		this.name = student.getFullName();
	}

	public String getSkips() {
		String skips = "";
		if (lectureSkip != null || practicalSkip != null || labSkip != null) {
			return (lectureSkip == null ? "0" : lectureSkip) + "/" +
					(practicalSkip == null ? "0" : practicalSkip) + "/" +
					(labSkip == null ? "0" : labSkip);
		}
		return skips;
	}

	/*GETTERS AND SETTERS*/
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTotalSkip() {
		return totalSkip;
	}

	public void setTotalSkip(Integer totalSkip) {
		this.totalSkip = totalSkip;
	}

	public Integer getLectureSkip() {
		return lectureSkip;
	}

	public void setLectureSkip(Integer lectureSkip) {
		this.lectureSkip = lectureSkip;
	}

	public Integer getPracticalSkip() {
		return practicalSkip;
	}

	public void setPracticalSkip(Integer practicalSkip) {
		this.practicalSkip = practicalSkip;
	}

	public Integer getLabSkip() {
		return labSkip;
	}

	public void setLabSkip(Integer labSkip) {
		this.labSkip = labSkip;
	}

	public LocalTime getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(LocalTime registrationTime) {
		this.registrationTime = registrationTime;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isAdditional() {
		return additional;
	}

	public void setAdditional(boolean additional) {
		this.additional = additional;
	}

	@Override
	public String toString() {
		return "LessonStudentModel{" +
				"id=" + id +
				", name='" + name + '\'' +
				", totalSkip=" + totalSkip +
				", lectureSkip=" + lectureSkip +
				", practicalSkip=" + practicalSkip +
				", labSkip=" + labSkip +
				", registrationTime=" + registrationTime +
				", additional=" + additional +
				'}';
	}
}
