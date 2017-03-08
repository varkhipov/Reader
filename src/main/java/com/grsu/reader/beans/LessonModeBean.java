package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.*;
import com.grsu.reader.utils.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private List<Student> lessonStudents;
	private List<Student> filteredLessonStudents;
	private List<Lesson> lessons;

	private Lesson selectedLesson;
	private Student selectedStudent;
	private List<Note> notes;

	private String newNote;
	private String cellClientId;
	private String noteType;
	private Integer entityId;

	private void clear() {
		stream = null;
		lesson = null;
		selectedLesson = null;
		selectedStudent = null;
		notes = null;
	}

	private void initLessonStudents() {
		Set<Student> studentSet = new HashSet<>();
		if (stream != null && lesson != null) {
			if (lesson.getGroup() != null) {
				studentSet.addAll(lesson.getGroup().getStudents());
				lessons = stream.getLessons().stream().filter(l -> l.getGroup() == null || (lesson.getGroup().equals(l.getGroup()))).collect(Collectors.toList());
			} else {
				for (Group group : stream.getGroups()) {
					studentSet.addAll(group.getStudents());
				}
				lessons = stream.getLessons();
			}


		}
		lessonStudents = new ArrayList<>(studentSet);
	}

	public void initNotes() {
		notes = null;
		if (noteType != null && ((selectedStudent != null && (selectedLesson != null || Constants.STUDENT.equals(noteType))) || Constants.LESSON.equals(noteType))) {
			switch (noteType) {
				case Constants.STUDENT_CLASS:
					StudentClass sc = selectedStudent.getStudentClasses().get(selectedLesson.getId());
					if (sc != null) {
						notes = sc.getNotes();
						entityId = sc.getId();
					}
					break;
				case Constants.STUDENT:
					notes = selectedStudent.getNotes();
					entityId = selectedStudent.getId();
					break;
				case Constants.LESSON:
					notes = selectedLesson.getNotes();
					entityId = selectedLesson.getId();
					break;
			}
		}
	}

	public void backToLesson() {
		clear();
		sessionBean.setActiveView("lesson");
	}

	public void saveNote() {
		if (newNote != null && !newNote.isEmpty()) {
			Note note = new Note();
			note.setCreateDate(LocalDateTime.now());
			note.setDescription(newNote);
			switch (noteType) {
				case Constants.STUDENT_CLASS:
					note.setStudentClass(selectedStudent.getStudentClasses().get(selectedLesson.getId()));
					break;
				case Constants.STUDENT:
					note.setStudent(selectedStudent);
					break;
				case Constants.LESSON:
					note.setLesson(selectedLesson);
					break;
			}
			note.setType(noteType);
			note.setEntityId(entityId);
			notes.add(note);
			new EntityDAO().save(note);
			newNote = null;
		}

	}

	public void closeDialog() {
		newNote = null;
		selectedLesson = null;
		noteType = null;
		if (cellClientId != null) {
			FacesUtils.update(cellClientId);
		}
		cellClientId = null;
	}

	public void removeNote(Note note) {
		new EntityDAO().delete(note);
		notes.remove(note);
	}

	public void saveClientId(AjaxBehaviorEvent event) {
		UIComponent component = event.getComponent();
		cellClientId = component.getClientId();
	}

	/*GETTERS AND SETTERS*/
	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
		initLessonStudents();
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
		initLessonStudents();
	}

	public List<Student> getLessonStudents() {
		return lessonStudents;
	}

	public void setLessonStudents(List<Student> lessonStudents) {
		this.lessonStudents = lessonStudents;
	}

	public List<Student> getFilteredLessonStudents() {
		return filteredLessonStudents;
	}

	public void setFilteredLessonStudents(List<Student> filteredLessonStudents) {
		this.filteredLessonStudents = filteredLessonStudents;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Lesson getSelectedLesson() {
		return selectedLesson;
	}

	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
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

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
}
