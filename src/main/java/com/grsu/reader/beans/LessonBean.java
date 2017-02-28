package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.entities.*;
import com.grsu.reader.entities.Class;
import com.grsu.reader.models.*;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.LocaleUtils;
import com.grsu.reader.utils.SerialUtils;

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
	private List<Student> selectedPresentStudents;

	private List<Student> absentStudents;
	private List<Student> filteredAbsentStudents;
	private List<Student> selectedAbsentStudents;

	private List<Student> allStudents;
	private List<Student> filteredAllStudents;

	private Set<Student> lessonStudents;

	private Map<Integer, Map<String, Integer>> skipInfo;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	@ManagedProperty(value = "#{menuBean}")
	private MenuBean menuBean;

	public boolean isInfoChanged() {
		return selectedLesson != null && !selectedLesson.equals(copyOfSelectedLesson);
	}

	public void exit() {
		sessionBean.setActiveView("lessons");
		update("views");
		clear();
		closeDialog("lessonDialog");
	}

	public void returnToLessons() {
		clear();
		menuBean.showMenu();
		sessionBean.setActiveView("lessons");
	}

	public void clear() {
		selectedLesson = null;
		processedStudent = null;
		presentStudents = null;
		filteredPresentStudents = null;
		absentStudents = null;
		filteredAbsentStudents = null;
		lessonStudents = null;
		allStudents = null;
		filteredAllStudents = null;
		skipInfo = null;
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
		lessonStudents = new HashSet<>();
		if (selectedLesson.getStream() != null) {
			if (selectedLesson.getGroup() != null) {
				lessonStudents = new HashSet<>(selectedLesson.getGroup().getStudents());
			} else {
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
				updateSkipInfo(student, false);
			}
		}

		processedStudent = student;
		FacesUtils.push("/register", processedStudent);
		System.out.println("Student registered");
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
				updateSkipInfo(student, false);
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
			updateSkipInfo(student, true);
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
		if (!recordStarted) {
			LocaleUtils localeUtils = new LocaleUtils();
			FacesUtils.addWarning(
					localeUtils.getMessage("warning"),
					localeUtils.getMessage("warning.device.not.connected.reconnect")
			);
			FacesUtils.update("menuForm:messages");
		}
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

	public void addAbsentStudents() {
		if (selectedAbsentStudents != null && selectedAbsentStudents.size() > 0) {

			List<StudentClass> studentClassList = new ArrayList<>();
			for (Student student : selectedAbsentStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				studentClassList.add(studentClass);
				updateSkipInfo(student, false);
			}
			new EntityDAO().update(new ArrayList<>(studentClassList));

			presentStudents.addAll(selectedAbsentStudents);
			absentStudents.removeAll(selectedAbsentStudents);
			allStudents.removeAll(selectedAbsentStudents);
			selectedAbsentStudents.clear();
		}

		processedStudent = null;
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	public void removePresentStudents() {
		if (selectedPresentStudents != null && selectedPresentStudents.size() > 0) {

			List<StudentClass> updateStudentClassList = new ArrayList<>();
			List<StudentClass> removeStudentClassList = new ArrayList<>();

			for (Student student : selectedPresentStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());

				if (lessonStudents.contains(student)) {

					studentClass.setRegistrationType(null);
					studentClass.setRegistrationTime(null);
					studentClass.setRegistered(false);
					updateStudentClassList.add(studentClass);
					absentStudents.add(student);
					updateSkipInfo(student, true);
				} else {
					student.getStudentClasses().remove(selectedLesson.getClasses().get(0).getId());
					selectedLesson.getClasses().get(0).getStudentClasses().remove(student.getId());
					allStudents.add(student);
					removeStudentClassList.add(studentClass);
				}
			}
			new EntityDAO().update(new ArrayList<>(updateStudentClassList));
			new EntityDAO().delete(new ArrayList<>(removeStudentClassList));

			presentStudents.removeAll(selectedPresentStudents);
			selectedPresentStudents.clear();
		}

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	private void updateSkipInfo(Student student, boolean addSkip) {
		Map<String, Integer> studentSkipInfoMap = skipInfo.get(student.getId());
		String lessonType = com.grsu.reader.models.LessonType.getLessonTypeByCode(selectedLesson.getType().getId()).getKey();

		if (studentSkipInfoMap == null && addSkip) {
			studentSkipInfoMap = new HashMap<>();
			skipInfo.put(student.getId(), studentSkipInfoMap);
		}

		if (studentSkipInfoMap != null) {
			if (studentSkipInfoMap.containsKey(lessonType)) {
				int count = studentSkipInfoMap.get(lessonType) + (addSkip ? 1 : -1);
				if (count > 0) {
					studentSkipInfoMap.put(lessonType, count);
				} else {
					studentSkipInfoMap.remove(lessonType);
				}
			} else {
				if (addSkip) {
					studentSkipInfoMap.put(lessonType, 1);
				}
			}

			if (studentSkipInfoMap.containsKey(Constants.TOTAL_SKIP)) {
				int count = studentSkipInfoMap.get(Constants.TOTAL_SKIP) + (addSkip ? 1 : -1);
				if (count > 0) {
					studentSkipInfoMap.put(Constants.TOTAL_SKIP, count);
				} else {
					skipInfo.remove(student.getId());
				}
			} else {
				if (addSkip) {
					studentSkipInfoMap.put(Constants.TOTAL_SKIP, 1);
				}
			}


		}
	}

	/* GETTERS & SETTERS */
	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
		copyOfSelectedLesson = this.selectedLesson == null ? null : new Lesson(selectedLesson);
		if (selectedLesson != null) {
			initStudents();
			initAllStudents();
			initLessonStudents();

			if (selectedLesson.getStream() != null) {
				skipInfo = new HashMap<>();
				List<SkipInfo> skipInfos = new StudentDAO().getSkipInfo(selectedLesson.getStream().getId());
				if (skipInfos != null) {
					for (SkipInfo si : skipInfos) {
						if (!skipInfo.containsKey(si.getStudentId())) {
							Map<String, Integer> studentSkipInfoMap = new HashMap<>();
							studentSkipInfoMap.put(Constants.TOTAL_SKIP, 0);
							skipInfo.put(si.getStudentId(), studentSkipInfoMap);
						}
						skipInfo.get(si.getStudentId()).put(si.getLessonType().getKey(), si.getCount());
						int total = skipInfo.get(si.getStudentId()).get(Constants.TOTAL_SKIP);
						skipInfo.get(si.getStudentId()).put(Constants.TOTAL_SKIP, total + si.getCount());
					}
				}
			}

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

	public Set<Student> getLessonStudents() {
		return lessonStudents;
	}

	public void setLessonStudents(Set<Student> lessonStudents) {
		this.lessonStudents = lessonStudents;
	}

	public List<Student> getSelectedPresentStudents() {
		return selectedPresentStudents;
	}

	public void setSelectedPresentStudents(List<Student> selectedPresentStudents) {
		this.selectedPresentStudents = selectedPresentStudents;
	}

	public List<Student> getSelectedAbsentStudents() {
		return selectedAbsentStudents;
	}

	public void setSelectedAbsentStudents(List<Student> selectedAbsentStudents) {
		this.selectedAbsentStudents = selectedAbsentStudents;
	}

	public void setMenuBean(MenuBean menuBean) {
		this.menuBean = menuBean;
	}

	public String getAdditionalStudentsSize() {
		if (this.presentStudents != null) {
			List<Student> additionalStudents = new ArrayList<>(this.presentStudents);
			additionalStudents.removeAll(this.lessonStudents);

			if (additionalStudents.size() != 0) {
				return String.valueOf(additionalStudents.size());
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

	public String getStudentSkip(Student student) {
		Map<String, Integer> studentSkipInfoMap = skipInfo.get(student.getId());
		if (studentSkipInfoMap != null) {
			List<String> skipInfoList = new ArrayList<>();
			for (Map.Entry<String, Integer> skipInfo : studentSkipInfoMap.entrySet()) {
				if (!Constants.TOTAL_SKIP.equals(skipInfo.getKey())) {
					skipInfoList.add(new LocaleUtils().getMessage(skipInfo.getKey()) + ":" + skipInfo.getValue());
				}
			}
			return skipInfoList.stream().collect(Collectors.joining(" \\ "));
		}
		return new LocaleUtils().getMessage("lesson.visit.noSkip");
	}

	public String getSkipCount(String type, Student student) {
		if (student != null) {
			Map<String, Integer> studentSkipInfoMap = skipInfo.get(student.getId());
			if (studentSkipInfoMap != null) {
				return studentSkipInfoMap.containsKey(type) ? Integer.toString(studentSkipInfoMap.get(type)) : "";
			}
		}
		return "";
	}
}
