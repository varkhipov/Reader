package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.*;
import com.grsu.reader.models.*;
import com.grsu.reader.models.Class;
import com.grsu.reader.utils.DateUtils;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.SerialUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

import static com.grsu.reader.utils.EntityUtils.getEntityById;
import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "lessonBean")
@ViewScoped
public class LessonBean implements Serializable {

	private Lesson selectedLesson;
	private Lesson copyOfSelectedLesson;
	private Student processedStudent;
	private int selectedScheduleId;
	private boolean recordStarted = false;
	private boolean soundEnabled = true;

	private List<Student> presentStudents;
	private List<Student> filteredPresentStudents;

	private List<Student> absentStudents;
	private List<Student> filteredAbsentStudents;

	private List<Student> allStudents;
	private List<Student> filteredAllStudents;

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
		setProcessedStudent(null);
		sessionBean.setActiveView("lessons");
	}

	public void createLesson() {
		if (selectedLesson != null) {
			selectedLesson.setName(String.format("%s [%s]",
					selectedLesson.getCourse().getName(),
					DateUtils.formatDate(LocalDateTime.now())
					)
			);
			selectedLesson.setClasses(Arrays.asList(
					new Class(
							LocalDate.now(),
							ScheduleDAO.getScheduleById(
									databaseBean.getConnection(),
									selectedScheduleId
							),
							this.selectedLesson
					)
			));

			int lessonId = LessonDAO.create(
					databaseBean.getConnection(),
					selectedLesson
			);
			selectedLesson.setId(lessonId);

			for (Class cls : selectedLesson.getClasses()) {
				int classId = ClassDAO.create(
						databaseBean.getConnection(),
						cls
				);
				cls.setId(classId);
			}

			List<Group> groups = StreamGroupDAO.getGroupsByStreamId(
					databaseBean.getConnection(),
					selectedLesson.getCourse().getStream().getId()
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
			sessionBean.updateLessons();
			sessionBean.setActiveView("lessons");
			update("views");
		}
		exit();
	}

	private void initPresentStudents() {
		if (selectedLesson == null || selectedLesson.getCourse() == null) {
			presentStudents = null;
		} else {
			List<Student> students = new ArrayList<>();
			for (Class cls : selectedLesson.getClasses()) {
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
		if (selectedLesson == null || selectedLesson.getCourse() == null) {
			absentStudents = null;
		} else {
			List<Student> students = new ArrayList<>();
			for (Class cls : selectedLesson.getClasses()) {
				students.addAll(StudentClassDAO.getStudentsByClassId(
						databaseBean.getConnection(),
						cls.getId(),
						false)
				);
			}
			absentStudents = students;
		}
	}

	public void initAllStudents() {
		List<Student> allStudents = new ArrayList<>(sessionBean.getStudents());
		if (presentStudents != null) {
			allStudents.removeAll(presentStudents);
		}
		if (absentStudents != null) {
			allStudents.removeAll(absentStudents);
		}
		this.allStudents = allStudents;
	}

	public boolean processStudent(Student student) {
		presentStudents.add(student);
		absentStudents.remove(student);
		allStudents.remove(student);

		try {
			StudentClassDAO.updateStudentClassInfo(
					databaseBean.getConnection(),
					student.getId(),
					selectedLesson.getClasses().get(0).getId(),
					true,
					LocalTime.now(),
					Constants.REGISTRATION_TYPE_MANUAL
			);
		} catch (SQLException e) {
			System.out.println("Student not added. Uid[ " + student.getUid() + " ]. Reason: SQLException:\n" + e);
			return false;
		}

		processedStudent = student;
		FacesUtils.push("/register", student);
		System.out.println("Student added");
		return true;
	}

	public boolean addStudent(Student student) {
		presentStudents.add(student);
		absentStudents.remove(student);
		allStudents.remove(student);

		try {
			StudentClassDAO.updateStudentClassInfo(
					databaseBean.getConnection(),
					student.getId(),
					selectedLesson.getClasses().get(0).getId(),
					true,
					LocalTime.now(),
					Constants.REGISTRATION_TYPE_AUTOMATIC
			);
		} catch (SQLException e) {
			System.out.println("Student not added. Uid[ " + student.getUid() + " ]. Reason: SQLException:\n" + e);
			return false;
		}

		System.out.println("Student added");
		return true;
	}

	public boolean removeStudent(Student student) {
		presentStudents.remove(student);
		absentStudents.add(student);
		//TODO: if from allStudents

		try {
			StudentClassDAO.updateStudentClassInfo(
					databaseBean.getConnection(),
					student.getId(),
					selectedLesson.getClasses().get(0).getId(),
					false,
					null,
					null
			);
		} catch (SQLException e) {
			System.out.println("Student not removed. Uid[ " + student.getUid() + " ]. Reason: SQLException:\n" + e);
			return false;
		}

		System.out.println("Student removed");
		return true;
	}

	public void startRecord() {
		recordStarted = SerialUtils.connect(this);
	}

	public void stopRecord() {
		recordStarted = !SerialUtils.disconnect();
	}

	public void enableSound() {
		soundEnabled = true;
	}

	public void disableSound() {
		soundEnabled = false;
	}

	public void removeLesson(Lesson lesson) {
		LessonDAO.delete(databaseBean.getConnection(), lesson);
		getLessons().remove(lesson);
	}

	/* GETTERS & SETTERS */
	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
		copyOfSelectedLesson = this.selectedLesson == null ? null : new Lesson(selectedLesson);
		if (selectedLesson != null) {
			initPresentStudents();
			initAbsentStudents();
			initAllStudents();
		}
	}

	public List<SelectItem> getSchedulesItems() {
		return sessionBean.getSchedulesItems();
	}

	public List<SelectItem> getCoursesItems() {
		return sessionBean.getCoursesItems();
	}

	public int getSelectedCourseId() {
		if (selectedLesson.getCourse() == null) {
			return 0;
		}
		return selectedLesson.getCourse().getId();
	}

	public void setSelectedCourseId(int selectedCourseId) {
		selectedLesson.setCourse(getEntityById(sessionBean.getCourses(), selectedCourseId));
	}

	public int getSelectedScheduleId() {
		return selectedScheduleId;
	}

	public void setSelectedScheduleId(int selectedScheduleId) {
		this.selectedScheduleId = selectedScheduleId;
	}

	public Student getProcessedStudent() {
		return processedStudent;
	}

	public void setProcessedStudent(Student processedStudent) {
		this.processedStudent = processedStudent;
	}

	public List<SelectItem> getStreamsItems() {
		return sessionBean.getStreamsItems();
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

	public boolean isRecordStarted() {
		return recordStarted;
	}

	public void setRecordStarted(boolean recordStarted) {
		this.recordStarted = recordStarted;
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

	public List<Student> getFilteredPresentStudents() {
		return filteredPresentStudents;
	}

	public void setFilteredPresentStudents(List<Student> filteredPresentStudents) {
		this.filteredPresentStudents = filteredPresentStudents;
	}

	public List<Student> getAbsentStudents() {
		return absentStudents;
	}

	public void setAbsentStudents(List<Student> absentStudents) {
		this.absentStudents = absentStudents;
	}

	public List<Student> getFilteredAbsentStudents() {
		return filteredAbsentStudents;
	}

	public void setFilteredAbsentStudents(List<Student> filteredAbsentStudents) {
		this.filteredAbsentStudents = filteredAbsentStudents;
	}

	public List<Student> getAllStudents() {
		return allStudents;
	}

	public void setAllStudents(List<Student> allStudents) {
		this.allStudents = allStudents;
	}

	public List<Student> getFilteredAllStudents() {
		return filteredAllStudents;
	}

	public void setFilteredAllStudents(List<Student> filteredAllStudents) {
		this.filteredAllStudents = filteredAllStudents;
	}

	public void exitStudents() {
		setFilteredAllStudents(null);
		update("views");
		closeDialog("addStudentsDialog");
	}

}
