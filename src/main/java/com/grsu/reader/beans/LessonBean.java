package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.*;
import com.grsu.reader.entities.Class;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.LocaleUtils;
import com.grsu.reader.utils.SerialUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

	private Set<Student> lessonStudents;

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
				lessonStudents = new HashSet<>(selectedLesson.getGroup().getStudents());
			} else {
				lessonStudents = new HashSet<>();
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

		if (student.getStudentClasses() != null) {
			StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());
			if (studentClass == null) {
				studentClass = new StudentClass();
				studentClass.setStudent(student);
				studentClass.setClazz(selectedLesson.getClasses().get(0));
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
				new EntityDAO().add(studentClass);
				student.getStudentClasses().put(studentClass.getClazz().getId(), studentClass);
				selectedLesson.getClasses().get(0).getStudentClasses().put(studentClass.getStudent().getId(), studentClass);
			} else {
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
				new EntityDAO().update(studentClass);
			}
		}

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

		if (student.getStudentClasses() != null) {
			StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());
			if (studentClass == null) {
				studentClass = new StudentClass();
				studentClass.setStudent(student);
				studentClass.setClazz(selectedLesson.getClasses().get(0));
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				new EntityDAO().add(studentClass);
				student.getStudentClasses().put(studentClass.getClazz().getId(), studentClass);
				selectedLesson.getClasses().get(0).getStudentClasses().put(studentClass.getStudent().getId(), studentClass);
			} else {
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				new EntityDAO().update(studentClass);
			}
		}

		processedStudent = student;
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	public void removeStudent(Student student) {
		StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());
		if (lessonStudents.contains(student)) {

			studentClass.setRegistrationType(null);
			studentClass.setRegistrationTime(null);
			studentClass.setRegistered(false);
			new EntityDAO().update(studentClass);
			absentStudents.add(student);
		} else {
			student.getStudentClasses().remove(selectedLesson.getClasses().get(0).getId());
			selectedLesson.getClasses().get(0).getStudentClasses().remove(student.getId());
			new EntityDAO().delete(studentClass);
			allStudents.add(student);
		}

		presentStudents.remove(student);

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
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

	public Set<Student> getLessonStudents() {
		return lessonStudents;
	}

	public void setLessonStudents(Set<Student> lessonStudents) {
		this.lessonStudents = lessonStudents;
	}

	public String getAdditionalStudentsSize() {
		if (this.presentStudents != null) {
			List<Student> additionalStudents = new ArrayList<>(this.presentStudents);
			additionalStudents.removeAll(this.lessonStudents);
			if (additionalStudents.size() != 0) {
				return new LocaleUtils().getMessage("lesson.students.additional") + ": " + additionalStudents.size();
			}
		}
		return "";
	}

	public int getPresentStudentsSize() {
		if (this.lessonStudents != null) {
			List<Student> presentStudents = new ArrayList<>(this.lessonStudents);
			presentStudents.removeAll(this.absentStudents);
			return presentStudents.size();
		}
		return 0;
	}

	public void exitStudents() {
		setFilteredAllStudents(null);
		update("views");
		closeDialog("addStudentsDialog");
	}

}
