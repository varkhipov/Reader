package com.grsu.reader.beans;

import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.entities.Note;
import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;
import com.grsu.reader.entities.StudentClass;
import com.grsu.reader.models.LessonStudentModel;
import com.grsu.reader.models.SkipInfo;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zaychick-pavel on 4/27/17.
 */
@ManagedBean(name = "studentModeBean")
@ViewScoped
@Data
public class StudentModeBean implements Serializable {
	private Stream stream;
	private LessonStudentModel lessonStudent;
	private List<Note> notes;
	private Map<String, Integer> marks;
	private List<StudentClass> studentClasses;

	public void initStudentMode(Student student, Stream stream) {
		this.stream = stream;
		lessonStudent = new LessonStudentModel(student);
		if (stream != null) {
			List<SkipInfo> studentSkipInfo = new StudentDAO().getStudentSkipInfo(Arrays.asList(student.getId()), stream.getId(), -1);
			if (studentSkipInfo != null && studentSkipInfo.size() > 0) {
				for (SkipInfo si : studentSkipInfo) {
					switch (si.getLessonType()) {
						case LECTURE:
							lessonStudent.setLectureSkip(si.getCount());
							break;
						case PRACTICAL:
							lessonStudent.setPracticalSkip(si.getCount());
							break;
						case LAB:
							lessonStudent.setLabSkip(si.getCount());
							break;
					}
				}
				if (lessonStudent.getLectureSkip() == null) {
					lessonStudent.setLectureSkip(0);
				}
				if (lessonStudent.getPracticalSkip() == null) {
					lessonStudent.setPracticalSkip(0);
				}
				if (lessonStudent.getLabSkip() == null) {
					lessonStudent.setLabSkip(0);
				}
				lessonStudent.setTotalSkip(lessonStudent.getLectureSkip() + lessonStudent.getPracticalSkip() + lessonStudent.getLabSkip());
			}
		}

		//init student notes and marks
		notes = new ArrayList<>();
		marks = new HashMap<>();
		notes.addAll(student.getNotes());
		student.getStudentClasses().values().forEach(sc -> {
			if (stream.getLessons().contains(sc.getClazz().getLesson())) {
				notes.addAll(sc.getNotes());
				if (sc.getMark() != null) {
					if (!marks.containsKey(sc.getMark())) {
						marks.put(sc.getMark(), 0);
					}
					marks.put(sc.getMark(), marks.get(sc.getMark()) + 1);
				}
			}
		});

		//init student classes
		studentClasses = stream.getLessons().stream()
				.filter(l -> student.getStudentClasses().containsKey(l.getClazz().getId()))
				.map(l -> student.getStudentClasses().get(l.getClazz().getId()))
				.collect(Collectors.toList());
	}

	public void clear() {
		stream = null;
		lessonStudent = null;
		notes = null;
		marks = null;
		studentClasses = null;
	}

	public List<Map.Entry<String, Integer>> getMarks() {
		if (marks != null) {
			return new ArrayList<>(marks.entrySet());
		}
		return null;
	}
}
