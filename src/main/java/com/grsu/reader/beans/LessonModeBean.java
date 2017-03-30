package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.Class;
import com.grsu.reader.entities.Lesson;
import com.grsu.reader.entities.Note;
import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;
import com.grsu.reader.entities.StudentClass;
import com.grsu.reader.models.LazyStudentDataModel;
import com.grsu.reader.models.LessonModel;
import com.grsu.reader.models.LessonStudentModel;
import com.grsu.reader.models.LessonType;
import com.grsu.reader.utils.FacesUtils;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.event.CellEditEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zaychick-pavel on 3/3/17.
 */
@ManagedBean(name = "lessonModeBean")
@ViewScoped
public class LessonModeBean implements Serializable {
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	private Stream stream;
	private Lesson lesson;
	private List<LessonModel> lessons;
	private List<LessonModel> attestations;

	private List<Note> notes;
	private String newNote;
	private Integer entityId;

	private List<LessonStudentModel> students;
	private LessonStudentModel selectedStudent;
	private LazyStudentDataModel studentsLazyModel;

	private LessonModel selectedLesson;

	private Integer selectedCell;
	private String selectedClientId;
	private String selectedType;

	private boolean registered;
	private boolean showAttestations = false;

	public void initLessonMode() {
		initLessonStudents();
	}

	public void clear() {
		stream = null;
		lesson = null;
		lessons = null;

		notes = null;
		newNote = null;
		entityId = null;

		students  = null;
		selectedStudent = null;
		studentsLazyModel = null;

		selectedType = null;
		selectedCell = null;
		selectedClientId = null;

		selectedLesson = null;
	}

	private void initLessonStudents() {
		List<Lesson> lessons = new ArrayList<>();
		Set<Student> studentSet = new HashSet<>();
		if (stream != null && lesson != null) {
			if (lesson.getGroup() != null) {
				studentSet.addAll(lesson.getGroup().getStudents());
				lessons = stream.getLessons().stream().filter(l -> l.getGroup() == null || (lesson.getGroup().equals(l.getGroup()))).collect(Collectors.toList());
			} else {
				stream.getGroups().stream().forEach(g -> studentSet.addAll(g.getStudents()));
				lessons = stream.getLessons();
			}
		}

		this.lessons = lessons.stream()
				.filter(l -> Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB).contains(l.getType()))
				.sorted((l1, l2) -> {
					if (l1.getClazz().getDate().isAfter(l2.getClazz().getDate())) return -1;
					if (l1.getClazz().getDate().isBefore(l2.getClazz().getDate())) return 1;
					return 0;
				})
				.map(LessonModel::new).collect(Collectors.toList());

		this.attestations = lessons.stream()
				.filter(l -> LessonType.ATTESTATION.equals(l.getType()))
				.map(LessonModel::new).collect(Collectors.toList());

		students = studentSet.stream().map(LessonStudentModel::new).sorted((s1, s2) -> s1.name.compareTo(s2.name)).collect(Collectors.toList());
		studentsLazyModel = new LazyStudentDataModel(students);
	}

	public void initRegisteredDialog() {
		if (Constants.STUDENT_CLASS.equals(selectedType)) {
			selectedLesson = calculateSelectedLesson();
			registered = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId()).getRegistered();
		} else {
			selectedLesson = null;
		}
	}

	public void initNotes() {
		notes = null;
		selectedLesson = calculateSelectedLesson();

		switch (selectedType) {
			case Constants.STUDENT_CLASS:
				StudentClass sc = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId());
				if (sc != null) {
					notes = sc.getNotes();
					entityId = sc.getId();
				}
				break;
			case Constants.STUDENT:
				notes = selectedStudent.getStudent().getNotes();
				entityId = selectedStudent.getStudent().getId();
				break;
			case Constants.LESSON:
				notes = selectedLesson.getLesson().getNotes();
				entityId = selectedLesson.getId();
				break;
		}
	}

	private LessonModel calculateSelectedLesson() {
		if (selectedCell != null) {
			if (showAttestations) {
				if (selectedCell < attestations.size()) {
					return attestations.get(selectedCell);
				} else {
					return lessons.get(selectedCell - attestations.size());
				}
			} else {
				return lessons.get(selectedCell);
			}
		}
		return null;
	}

	public void onCellEdit(CellEditEvent event) {
		Integer id;
		if (event.getColumn().getColumnKey().contains("attestation")) {
			id = attestations.get(((DynamicColumn) event.getColumn()).getIndex()).getId();
		} else {
			id = lessons.get(((DynamicColumn) event.getColumn()).getIndex()).getId();
		}
		EntityDAO.update(studentsLazyModel.getRowData().getStudent().getStudentClasses().get(id));
	}

	public void backToLesson() {
		clear();
//		lessonBean.calculateTimer();
		sessionBean.setActiveView("lesson");
	}

	public void saveNote() {
		if (newNote != null && !newNote.isEmpty()) {
			Note note = new Note();
			note.setCreateDate(LocalDateTime.now());
			note.setDescription(newNote);
			note.setType(selectedType);
			note.setEntityId(entityId);
			notes.add(note);
			EntityDAO.save(note);
			newNote = null;
		}
		FacesUtils.closeDialog("notesDialog");

	}

	public void saveRegisteredInfo() {
		boolean oldValue = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId()).getRegistered();
		if (oldValue != registered) {
			StudentClass studentClass = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId());
			studentClass.setRegistered(registered);
			if (!registered) {
				studentClass.setRegistrationTime(null);
				studentClass.setRegistrationType(null);

				FacesUtils.execute("$('#"+ selectedClientId.replaceAll("\\:", "\\\\\\\\:")+ "').closest('td').addClass('skip');");
			} else {
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				FacesUtils.execute("$('#"+ selectedClientId.replaceAll("\\:", "\\\\\\\\:")+ "').closest('td').removeClass('skip')");
			}
			EntityDAO.save(studentClass);
		}
		FacesUtils.closeDialog("registeredDialog");
		selectedLesson = null;
	}

	public void closeDialog() {
		newNote = null;
		notes = null;
		selectedType = null;
		selectedCell = null;
		selectedLesson = null;
		System.out.println("selectedClientId " + selectedClientId);
		if (selectedClientId != null) {
			FacesUtils.update(selectedClientId);
		}
		selectedClientId = null;
	}

	public void removeNote(Note note) {
		EntityDAO.delete(note);
		notes.remove(note);
	}

	public void createAttestation() {
		Lesson lesson = new Lesson();
		Class cl = new Class();
		cl.setDate(LocalDateTime.now());
		cl.setLesson(lesson);
		lesson.setClasses(new HashSet<>());
		lesson.getClasses().add(cl);
		lesson.setType(LessonType.ATTESTATION);
		lesson.setStream(this.stream);
		lesson.setGroup(this.lesson.getGroup());
		lesson.setNotes(new ArrayList<>());

		EntityDAO.add(lesson);
		EntityDAO.add(lesson.getClazz());
		stream.getLessons().add(lesson);

		List<StudentClass> studentClasses = new ArrayList<>();
		students.stream().forEach(s -> {
			StudentClass sc = new StudentClass();
			sc.setStudent(s.getStudent());
			sc.setClazz(lesson.getClazz());
			sc.setNotes(new ArrayList<>());
			studentClasses.add(sc);
			s.getStudent().getStudentClasses().put(lesson.getClazz().getId(), sc);
		});
		EntityDAO.add(new ArrayList<>(studentClasses));
		lesson.getClazz().setStudentClasses(new HashMap<>());
		studentClasses.stream().forEach(sc -> lesson.getClazz().getStudentClasses().put(sc.getStudentId(), sc));
		attestations.add(new LessonModel(lesson));
	}


	/*GETTERS AND SETTERS*/
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public String getNewNote() {
		return newNote;
	}

	public void setNewNote(String newNote) {
		this.newNote = newNote;
	}


	public List<LessonModel> getLessons() {
		return lessons;
	}

	public LazyStudentDataModel getStudentsLazyModel() {
		return studentsLazyModel;
	}

	public void setStudentsLazyModel(LazyStudentDataModel studentsLazyModel) {
		this.studentsLazyModel = studentsLazyModel;
	}

	public List<LessonStudentModel> getStudents() {
		return students;
	}

	public void setStudents(List<LessonStudentModel> students) {
		this.students = students;
	}

	public LessonStudentModel getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(LessonStudentModel selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public Integer getSelectedCell() {
		return selectedCell;
	}

	public void setSelectedCell(Integer selectedCell) {
		this.selectedCell = selectedCell;
	}

	public String getSelectedClientId() {
		return selectedClientId;
	}

	public void setSelectedClientId(String selectedClientId) {
		this.selectedClientId = selectedClientId;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public LessonModel getSelectedLesson() {
		return selectedLesson;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public List<LessonModel> getAttestations() {
		return attestations;
	}

	public boolean isShowAttestations() {
		return showAttestations;
	}

	public void setShowAttestations(boolean showAttestations) {
		this.showAttestations = showAttestations;
	}
}
