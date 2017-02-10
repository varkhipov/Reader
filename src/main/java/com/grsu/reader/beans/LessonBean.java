package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.ClassDAO;
import com.grsu.reader.dao.LessonDAO;
import com.grsu.reader.dao.StreamGroupDAO;
import com.grsu.reader.dao.StudentClassDAO;
import com.grsu.reader.dao.StudentGroupDAO;
import com.grsu.reader.models.Class;
import com.grsu.reader.models.Department;
import com.grsu.reader.models.Discipline;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Lesson;
import com.grsu.reader.models.Stream;
import com.grsu.reader.models.Student;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.SerialUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.grsu.reader.utils.EntityUtils.getEntityById;
import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "lessonBean")
@ViewScoped
public class LessonBean implements Serializable {

	private Lesson selectedLesson;
	private Lesson copyOfSelectedLesson;
	private Student processedStudent;
	private boolean recordStarted = false;
	private boolean soundEnabled = true;

	private List<Student> presentStudents;
	private List<Student> filteredPresentStudents;

	private List<Student> absentStudents;
	private List<Student> filteredAbsentStudents;

	private List<Student> allStudents;
	private List<Student> filteredAllStudents;

	private List<Student> lessonStudents;

	/* Filter */
	private Department department;
	private Discipline discipline;
	private Integer course;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public boolean isInfoChanged() {
		return selectedLesson != null && !selectedLesson.equals(copyOfSelectedLesson);
	}

	public void exit() {
		setSelectedLesson(null);
		department = null;
		discipline = null;
		course = null;
		closeDialog("lessonDialog");
	}

	public void returnToLessons() {
		setSelectedLesson(null);
		setProcessedStudent(null);
		sessionBean.setActiveView("lessons");
	}

	public void createLesson() {
		if (selectedLesson != null) {
			if (StringUtils.isEmpty(selectedLesson.getName())) {
				selectedLesson.setName(String.format("%s [%s]",
						selectedLesson.getStream().getName(),
						LocalDateTime.now()
						)
				);
			}

			if (selectedLesson.getLessonType() == null || selectedLesson.getLessonType().getId() == 1) {
				selectedLesson.setGroup(null);
			}

			selectedLesson.setId(LessonDAO.create(
					databaseBean.getConnection(),
					selectedLesson
			));

			selectedLesson.getClasses().get(0).setId(ClassDAO.create(
					databaseBean.getConnection(),
					selectedLesson.getClasses().get(0)
			));


			List<Group> groups;

			if (selectedLesson.getGroup() == null) {
				groups = StreamGroupDAO.getGroupsByStreamId(
						databaseBean.getConnection(),
						selectedLesson.getStream().getId()
				);
			} else {
				groups = Arrays.asList(selectedLesson.getGroup());
			}

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
		if (selectedLesson == null || selectedLesson.getStream() == null) {
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
		if (selectedLesson == null || selectedLesson.getStream() == null) {
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

	private void initLessonStudents() {
		if (selectedLesson.getStream() != null) {
			if (selectedLesson.getGroup() != null) {
				lessonStudents = selectedLesson.getGroup().getStudents();
			} else {
				lessonStudents = new ArrayList<>();
				for (Group group : selectedLesson.getStream().getGroups()) {
					lessonStudents.addAll(StudentGroupDAO.getStudentsByGroupId(databaseBean.getConnection(), group.getId()));
				}
			}
		}
	}

	public boolean processStudent(Student student) {
		presentStudents.add(student);
		absentStudents.remove(student);
		if (filteredAbsentStudents != null) {
			filteredAbsentStudents.remove(student);
		}

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
			System.out.println("Student not added. Uid[ " + student.getCardUid() + " ]. Reason: SQLException:\n" + e);
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
		if (filteredAllStudents != null) {
			filteredAllStudents.remove(student);
		}

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
			System.out.println("Student not added. Uid[ " + student.getCardUid() + " ]. Reason: SQLException:\n" + e);
			return false;
		}

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
//		System.out.println("Student added");
		return true;
	}

	public boolean removeStudent(Student student) {
		try {
			if (lessonStudents.contains(student)) {
				StudentClassDAO.updateStudentClassInfo(
						databaseBean.getConnection(),
						student.getId(),
						selectedLesson.getClasses().get(0).getId(),
						false,
						null,
						null
				);
				absentStudents.add(student);
			} else {
				StudentClassDAO.deleteByStudentId(databaseBean.getConnection(), student.getId());
				allStudents.add(student);
			}

			presentStudents.remove(student);

		} catch (SQLException e) {
			System.out.println("Student not removed. Uid[ " + student.getCardUid() + " ]. Reason: SQLException:\n" + e);
			return false;
		}

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
//		System.out.println("Student removed");
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

	/* Filter */
	public List<Stream> getFilteredStream() {
		return sessionBean.getStreams().stream()
				.filter(stream -> {
					boolean discipline = (this.discipline == null) || (this.discipline.equals(stream.getDiscipline()));
					boolean department = (this.department == null) || (this.department.equals(stream.getDepartment()));
					boolean course = (this.course == null) || (this.course.equals(stream.getCourse()));
					return discipline && department && course;
				})
				.collect(Collectors.toList());
	}

	/* GETTERS & SETTERS */
	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
		copyOfSelectedLesson = this.selectedLesson == null ? null : new Lesson(selectedLesson);
		if (selectedLesson != null) {
			initPresentStudents();
			initAbsentStudents();
			initAllStudents();
			initLessonStudents();
		}
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

	public Student getProcessedStudent() {
		return processedStudent;
	}

	public void setProcessedStudent(Student processedStudent) {
		this.processedStudent = processedStudent;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public void exitStudents() {
		setFilteredAllStudents(null);
		update("views");
		closeDialog("addStudentsDialog");
	}

}
