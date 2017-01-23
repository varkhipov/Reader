package com.grsu.reader.beans;

import com.grsu.reader.dao.*;
import com.grsu.reader.models.*;
import com.grsu.reader.models.Class;
import com.grsu.reader.utils.DBUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static com.grsu.reader.utils.EntityUtils.getEntityById;
import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "lessonBean")
@ViewScoped
public class LessonBean implements Serializable {

	private Lesson selectedLesson;
	private Lesson copyOfSelectedLesson;
	private int selectedScheduleId;
	private boolean recordStarted = false;
	private boolean soundEnabled = true;
	private List<Class> classes;
	private List<Student> presentStudents;
	private List<Student> absentStudents;


	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public boolean isInfoChanged() {
		return selectedLesson != null && !selectedLesson.equals(copyOfSelectedLesson);
	}

	public void exit() {
		setSelectedLesson(null);
		setSelectedScheduleId(0);
		closeDialog("lessonDialog");
	}

	public void returnToLessons() {
		setSelectedLesson(null);
		sessionBean.setActiveView("lessons");
	}

	public void createLesson() {
		if (selectedLesson != null) {
			selectedLesson.setClasses(Arrays.asList(
					new Class(
							LocalDate.now(),
							ScheduleDAO.getScheduleById(
									databaseBean.getConnection(),
									selectedScheduleId
							)
					)
			));
			selectedLesson.setLecturer(
					LecturerDAO.getLecturerById(
							databaseBean.getConnection(),
							1
					)
			);

			LessonDAO.create(
					databaseBean.getConnection(),
					selectedLesson
			);
			selectedLesson.setId(DBUtils.getLastInsertRowId(
					databaseBean.getConnection()
			));

			for (Class cls : selectedLesson.getClasses()) {
				ClassDAO.create(
						databaseBean.getConnection(),
						cls
				);
				cls.setId(DBUtils.getLastInsertRowId(
						databaseBean.getConnection()
				));

				LessonClassDAO.create(
						databaseBean.getConnection(),
						selectedLesson,
						cls
				);
			}

			List<Group> groups = StreamGroupDAO.getGroupsByStreamId(
					databaseBean.getConnection(),
					selectedLesson.getStream().getId()
			);

			List<Student> students = new ArrayList<>();
			for (Group group : groups) {
				students.addAll(StudentGroupDAO.getStudentsByGroupId(
						databaseBean.getConnection(),
						group.getId()
				));
			}

			for (Student student : students) {
				StudentClassDAO.create(
						databaseBean.getConnection(),
						student,
						selectedLesson.getClasses().get(0)
				);
			}
			update("views");
		}
		exit();
	}

	private void addListener() {
//		addVisit(new Student());
	}

	public void addVisit() {
		if (!absentStudents.isEmpty()) {
			Student student = new Student(absentStudents.get(0));
			presentStudents.add(student);
			absentStudents.remove(student);
			StudentClassDAO.updateByStudentId(
					databaseBean.getConnection(),
					student.getId(),
					selectedLesson.getClasses().get(0).getId(),
					true
			);
		}
	}

	private void initPresentStudents() {
		if (selectedLesson == null) {
			presentStudents = null;
		} else {
			List<Class> classes = LessonClassDAO.getClassesByLessonId(
					databaseBean.getConnection(),
					selectedLesson.getId()
			);
			List<Student> students = new ArrayList<>();
			for (Class cls : classes) {
				students.addAll(StudentClassDAO.getStudentsByClassId(
						databaseBean.getConnection(),
						cls.getId(),
						true)
				);
			}
			presentStudents = students;
		}
	}

	private void initAbsentStudents() {
		if (selectedLesson == null || selectedLesson.getStream() == null) {
			absentStudents = null;
		} else {
			List<Class> classes = LessonClassDAO.getClassesByLessonId(
					databaseBean.getConnection(),
					selectedLesson.getId()
			);
			List<Student> students = new ArrayList<>();
			for (Class cls : classes) {
				students.addAll(StudentClassDAO.getStudentsByClassId(
						databaseBean.getConnection(),
						cls.getId(),
						false)
				);
			}
			absentStudents = students;
		}
	}

	private void processStudent() {

	}

	public void startRecord() {
		recordStarted = true;
	}

	public void stopRecord() {
		recordStarted = false;
	}

	public void enableSound() {
		soundEnabled = true;
	}

	public void disableSound() {
		soundEnabled = false;
	}


	/* GETTERS & SETTERS */
	public List<SelectItem> getSchedulesItems() {
		return sessionBean.getSchedulesItems();
	}

	public int getSelectedScheduleId() {
		return selectedScheduleId;
	}

	public void setSelectedScheduleId(int selectedScheduleId) {
		this.selectedScheduleId = selectedScheduleId;
	}

	public List<SelectItem> getDisciplinesItems() {
		return sessionBean.getDisciplinesItems();
	}

	public int getSelectedDisciplineId() {
		if (selectedLesson.getDiscipline() == null) {
			return 0;
		}
		return selectedLesson.getDiscipline().getId();
	}

	public void setSelectedDisciplineId(int selectedDisciplineId) {
		selectedLesson.setDiscipline(getEntityById(sessionBean.getDisciplines(), selectedDisciplineId));
	}

	public List<SelectItem> getStreamsItems() {
		return sessionBean.getStreamsItems();
	}

	public int getSelectedStreamId() {
		if (selectedLesson.getStream() == null) {
			return 0;
		}
		return selectedLesson.getStream().getId();
	}

	public void setSelectedStreamId(int selectedStreamId) {
		selectedLesson.setStream(getEntityById(sessionBean.getStreams(), selectedStreamId));
	}

	public List<Lesson> getLessons() {
		return sessionBean.getLessons();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Lesson getSelectedLesson() {
		return selectedLesson;
	}

	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
		copyOfSelectedLesson = this.selectedLesson == null ? null : new Lesson(selectedLesson);
		if (selectedLesson != null) {
			initPresentStudents();
			initAbsentStudents();
		}
	}

	public boolean isRecordStarted() {
		return recordStarted;
	}

	public void setRecordStarted(boolean recordStarted) {
		this.recordStarted = recordStarted;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}

	public List<Student> getPresentStudents() {
		return presentStudents;
	}

	public void setPresentStudents(List<Student> presentStudents) {
		this.presentStudents = presentStudents;
	}

	public List<Student> getAbsentStudents() {
		return absentStudents;
	}

	public void setAbsentStudents(List<Student> absentStudents) {
		this.absentStudents = absentStudents;
	}
}
