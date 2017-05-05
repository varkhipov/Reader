package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Lesson;
import com.grsu.reader.entities.Note;
import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;
import com.grsu.reader.entities.StudentClass;
import com.grsu.reader.models.LessonStudentModel;
import com.grsu.reader.models.LessonType;
import com.grsu.reader.models.SkipInfo;
import com.grsu.reader.utils.FacesUtils;
import lombok.Data;
import org.primefaces.component.inputnumber.InputNumber;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	private List<StudentClass> attestations;
	private StudentClass examClass;

	private StudentClass selectedStudentClass;
	private String newNote;
	private boolean registered;

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
		initNotesAndMarks();

		//init student classes
		studentClasses = stream.getLessons().stream()
				.filter(l -> Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB).contains(l.getType()) && student.getStudentClasses().containsKey(l.getClazz().getId()))
				.map(l -> student.getStudentClasses().get(l.getClazz().getId()))
				.collect(Collectors.toList());

		//init attestations
		attestations = stream.getLessons().stream()
				.filter(l -> LessonType.ATTESTATION.equals(l.getType()) && student.getStudentClasses().containsKey(l.getClazz().getId()))
				.map(l -> student.getStudentClasses().get(l.getClazz().getId()))
				.collect(Collectors.toList());
		updateAverageAttestation();
		lessonStudent.updateTotal();
	}

	public void initNotesAndMarks() {
		notes = new ArrayList<>();
		marks = new HashMap<>();
		notes.addAll(lessonStudent.getStudent().getNotes());
		lessonStudent.getStudent().getStudentClasses().values().forEach(sc -> {
			if (stream.getLessons().contains(sc.getClazz().getLesson())) {
				if (LessonType.EXAM.equals(sc.getClazz().getLesson().getType())) {
					examClass = sc;
					try {
						lessonStudent.setExamMark(Integer.valueOf(sc.getMark()));
					} catch (NumberFormatException ex) {
						lessonStudent.setExamMark(null);
					}
				}

				notes.addAll(sc.getNotes());
				if (sc.getMark() != null && !(LessonType.ATTESTATION.equals(sc.getClazz().getLesson().getType()) || LessonType.EXAM.equals(sc.getClazz().getLesson().getType()))) {
					Arrays.stream(sc.getMark().split(Constants.MARK_DELIMETER)).forEach(m -> {
								if (!marks.containsKey(m)) {
									marks.put(m, 0);
								}
								marks.put(m, marks.get(m) + 1);
							}
					);
				}
			}
		});
	}

	public boolean isAdditionalLesson(Lesson lesson) {
		if (lesson.getGroup() == null) {
			for (Group group : lesson.getStream().getGroups()) {
				if (lessonStudent.getStudent().getGroups().contains(group)) {
					return false;
				}
			}
		}
		if (lessonStudent.getStudent().getGroups().contains(lesson.getGroup())) {
			return false;
		}
		return true;
	}

	public void clear() {
		stream = null;
		lessonStudent = null;
		notes = null;
		marks = null;
		studentClasses = null;
		attestations = null;
		examClass = null;

		selectedStudentClass = null;
		newNote = null;
	}

	public List<Map.Entry<String, Integer>> getMarks() {
		if (marks != null) {
			return new ArrayList<>(marks.entrySet());
		}
		return null;
	}

	private void updateAverageAttestation() {
		lessonStudent.setAverageAttestation(null);
		List<String> marks = new ArrayList<>();
		attestations.stream().forEach(a -> {
			if (a.getMark() != null) {
				marks.addAll(Arrays.asList(a.getMark().split("[^0-9]")));
			}
		});
		if (marks.size() > 0) {
			lessonStudent.setAverageAttestation(marks.stream().mapToInt(Integer::parseInt).average().getAsDouble());
		}
	}

	public void changeExamMark(ValueChangeEvent event) {
		if ("examMark".equals(((InputNumber) event.getSource()).getId())) {
			lessonStudent.setExamMark((Integer) event.getNewValue());
			lessonStudent.updateTotal();
		}
		if ("totalMark".equals(((InputNumber) event.getSource()).getId())) {
			lessonStudent.setTotalMark((Integer) event.getNewValue());
			lessonStudent.updateExam();
		}
		if (examClass != null) {
			examClass.setMark(String.valueOf(lessonStudent.getExamMark()));
			EntityDAO.save(examClass);
		}
	}

	//NOTES
	public void saveNote() {
		if (newNote != null && !newNote.isEmpty()) {
			Note note = new Note();
			note.setCreateDate(LocalDateTime.now());
			note.setDescription(newNote);
			note.setType(Constants.STUDENT_CLASS);
			note.setEntityId(lessonStudent.getId());
			notes.add(note);
			EntityDAO.save(note);
			selectedStudentClass.getNotes().add(note);
		}
		newNote = null;
		FacesUtils.closeDialog("notesDialog");
	}

	public void removeNote(Note note) {
		EntityDAO.delete(note);
		notes.remove(note);
		selectedStudentClass.getNotes().remove(note);
	}

	//REGISTERED INFO
	public void saveRegisteredInfo() {
		boolean oldValue = selectedStudentClass.getRegistered();
		if (oldValue != registered) {
			selectedStudentClass.setRegistered(registered);
			if (!registered) {
				selectedStudentClass.setRegistrationTime(null);
				selectedStudentClass.setRegistrationType(null);
			} else {
				selectedStudentClass.setRegistrationTime(LocalTime.now());
				selectedStudentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
			}
			EntityDAO.save(selectedStudentClass);
		}
		FacesUtils.closeDialog("registeredDialog");
	}

}
