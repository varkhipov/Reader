package com.grsu.reader.models;

import com.grsu.reader.entities.Student;
import lombok.Data;

import java.time.LocalTime;

/**
 * Created by pavel on 3/4/17.
 */
@Data
public class LessonStudentModel {
	private Integer id;
	public String name;
	public Integer totalSkip;
	private Integer lectureSkip;
	private Integer practicalSkip;
	private Integer labSkip;
	public LocalTime registrationTime;
	private Student student;
	private Double averageAttestation;
	private boolean additional;
	private Integer examMark;
	private Integer totalMark;

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

	public void updateTotal() {
		if (examMark == null || averageAttestation == null) {
			if (averageAttestation == null && examMark == null) {
				totalMark = null;
			} else if (examMark == null) {
				totalMark = averageAttestation.intValue();
			} else {
				totalMark = examMark;
			}
		} else {
			totalMark = (int) (averageAttestation + examMark) / 2;
		}
	}

}
