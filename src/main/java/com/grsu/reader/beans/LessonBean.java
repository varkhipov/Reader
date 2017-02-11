package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.*;
import com.grsu.reader.entities.Class;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.SerialUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

			if (selectedLesson.getType() == null || selectedLesson.getType().getId() == 1) {
				selectedLesson.setGroup(null);
			}

			EntityDAO entityDAO = new EntityDAO();
			entityDAO.add(selectedLesson);
			entityDAO.add(selectedLesson.getClasses().get(0));


			List<Group> groups;

			if (selectedLesson.getGroup() == null) {
				groups = selectedLesson.getStream().getGroups();
			} else {
				groups = Arrays.asList(selectedLesson.getGroup());
			}

			Set<Student> students = new HashSet<>();
			for (Group group : groups) {
				students.addAll(group.getStudents());
			}

			List<StudentClass> studentClasses = new ArrayList<>();
			for (Student student : students) {
//				student.getClasses().add(selectedLesson.getClasses().get(0));
				StudentClass sc = new StudentClass();
				sc.setStudent(student);
				sc.setClazz(selectedLesson.getClasses().get(0));
				studentClasses.add(sc);
			}
			entityDAO.add(new ArrayList<>(studentClasses));

			sessionBean.updateLessons();
			sessionBean.setActiveView("lessons");
			update("views");
		}
		exit();
	}

	private void initStudents() {
		if (selectedLesson == null || selectedLesson.getStream() == null) {
			presentStudents = null;
			absentStudents = null;
		} else {
			List<StudentClass> studentClasses = new ArrayList<>();
			for (Class cls : selectedLesson.getClasses()) {
				studentClasses.addAll(cls.getStudentClasses().values());
			}
			presentStudents = new ArrayList<>();
			absentStudents = new ArrayList<>();
			for (StudentClass studentClass : studentClasses)
				if (studentClass.isRegistered()) {
					presentStudents.add(studentClass.getStudent());
				} else {
					absentStudents.add(studentClass.getStudent());
				}
		}
	}
/*
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
	}*/

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
					lessonStudents.addAll(group.getStudents());
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

	/*	try {

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
		}*/

		processedStudent = student;
		FacesUtils.push("/register", student);
		System.out.println("Student added");
		return true;
	}

	public void addStudent(Student student) {
		presentStudents.add(student);
		absentStudents.remove(student);
		allStudents.remove(student);
		if (filteredAllStudents != null) {
			filteredAllStudents.remove(student);
		}

		/*try {
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
		}*/

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
//		System.out.println("Student added");
	}

	public void removeStudent(Student student) {
		/*try {
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
*/
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
//		System.out.println("Student removed");
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
		new EntityDAO().delete(lesson);
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
			initStudents();
			initAllStudents();
			initLessonStudents();
			if (selectedLesson.getClasses() == null) {
				Class cl = new Class();
				cl.setDate(LocalDateTime.now());
				cl.setLesson(selectedLesson);
				selectedLesson.setClasses(Arrays.asList(cl));
			}
		}
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
