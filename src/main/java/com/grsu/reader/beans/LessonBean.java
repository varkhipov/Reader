package com.grsu.reader.beans;

import com.grsu.reader.constants.Constants;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.entities.Class;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Lesson;
import com.grsu.reader.entities.Student;
import com.grsu.reader.entities.StudentClass;
import com.grsu.reader.models.LazyStudentDataModel;
import com.grsu.reader.models.LessonStudentModel;
import com.grsu.reader.models.LessonType;
import com.grsu.reader.models.SkipInfo;
import com.grsu.reader.utils.FacesUtils;
import com.grsu.reader.utils.LocaleUtils;
import com.grsu.reader.utils.SerialUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;

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
import java.util.Map;
import java.util.Set;
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


	private List<LessonStudentModel> lessonPresentStudents;
	private List<LessonStudentModel> lessonAbsentStudents;

	private LazyStudentDataModel presentStudentsLazyModel;
	private LazyStudentDataModel absentStudentsLazyModel;

	private List<LessonStudentModel> selectedPresentStudents;
	private List<LessonStudentModel> selectedAbsentStudents;


	private boolean selectedAllPresentStudents;
	private boolean selectedAllAbsentStudents;

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

	public void lessonMode() {
		sessionBean.setActiveView("lessonMode");
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
			for (StudentClass studentClass : studentClasses) {
				if (studentClass.getRegistered()) {
					presentStudents.add(studentClass.getStudent());
				} else {
					absentStudents.add(studentClass.getStudent());
				}
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
				updateSkipInfo(Arrays.asList(student));
			}
		}

		processedStudent = student;
		updateLessonStudents();
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
				updateSkipInfo(Arrays.asList(student));
			}

		}

		processedStudent = student;
		updateLessonStudents();
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
			updateSkipInfo(Arrays.asList(student));
		} else {
			student.getStudentClasses().remove(selectedLesson.getClasses().get(0).getId());
			selectedLesson.getClasses().get(0).getStudentClasses().remove(student.getId());
			new EntityDAO().delete(studentClass);
			allStudents.add(student);
		}

		presentStudents.remove(student);
		updateLessonStudents();

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
			List<Student> selectedStudents = null;
			if (selectedAllAbsentStudents) {
				selectedStudents = lessonAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			} else {
				selectedStudents = selectedAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			}

			List<StudentClass> studentClassList = new ArrayList<>();
			for (Student student : selectedStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				studentClassList.add(studentClass);
			}
			new EntityDAO().update(new ArrayList<>(studentClassList));
			updateSkipInfo(selectedStudents);

			presentStudents.addAll(selectedStudents);
			absentStudents.removeAll(selectedStudents);
			allStudents.removeAll(selectedStudents);

			selectedAbsentStudents.clear();

			updateLessonStudents();
		}

		processedStudent = null;
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	public void removePresentStudents() {
		if (selectedPresentStudents != null && selectedPresentStudents.size() > 0) {
			List<Student> selectedStudents = null;
			if (selectedAllPresentStudents) {
				selectedStudents = lessonPresentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			} else {
				selectedStudents = selectedPresentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			}


			List<StudentClass> updateStudentClassList = new ArrayList<>();
			List<StudentClass> removeStudentClassList = new ArrayList<>();

			for (Student student : selectedStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClasses().get(0).getId());

				if (lessonStudents.contains(student)) {

					studentClass.setRegistrationType(null);
					studentClass.setRegistrationTime(null);
					studentClass.setRegistered(false);
					updateStudentClassList.add(studentClass);
					absentStudents.add(student);
				} else {
					student.getStudentClasses().remove(selectedLesson.getClasses().get(0).getId());
					selectedLesson.getClasses().get(0).getStudentClasses().remove(student.getId());
					allStudents.add(student);
					removeStudentClassList.add(studentClass);
				}
			}
			new EntityDAO().update(new ArrayList<>(updateStudentClassList));
			new EntityDAO().delete(new ArrayList<>(removeStudentClassList));
			updateSkipInfo(selectedStudents);

			presentStudents.removeAll(selectedStudents);
			selectedPresentStudents.clear();

			updateLessonStudents();
		}

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	private void updateSkipInfo(List<Student> students) {
		List<Integer> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());

		List<SkipInfo> studentSkipInfo = new StudentDAO().getStudentSkipInfo(studentIds, selectedLesson.getStream().getId());

		for (Integer id : studentIds) {
			skipInfo.remove(id);
		}

		if (studentSkipInfo != null && studentSkipInfo.size() > 0) {
			for (SkipInfo si : studentSkipInfo) {

				Map<String, Integer> studentSkipInfoMap = skipInfo.get(si.getStudentId());
				if (studentSkipInfoMap == null) {
					studentSkipInfoMap = new HashMap<>();
					studentSkipInfoMap.put(Constants.TOTAL_SKIP, 0);
					skipInfo.put(si.getStudentId(), studentSkipInfoMap);
				}

				studentSkipInfoMap.put(si.getLessonType().getKey(), si.getCount());
				int total = studentSkipInfoMap.get(Constants.TOTAL_SKIP);
				studentSkipInfoMap.put(Constants.TOTAL_SKIP, total + si.getCount());
			}
		}


	}

	/* LESSON STUDENTS TABLES */

	private void generateLessonStudents(List<LessonStudentModel> lessonStudentModelList, List<Student> students) {
		lessonStudentModelList.clear();
		for (Student st : students) {
			LessonStudentModel lessonStudentModel = new LessonStudentModel(st);
			lessonStudentModel.setRegistrationTime(
					st.getStudentClasses().get(selectedLesson.getClasses().get(0).getId()).getRegistrationTime());
			Map<String, Integer> stSkipInfo = skipInfo.get(st.getId());
			if (stSkipInfo != null) {
				lessonStudentModel.setTotalSkip(stSkipInfo.get(Constants.TOTAL_SKIP));
				lessonStudentModel.setLectureSkip(stSkipInfo.get(LessonType.LECTURE.getKey()));
				lessonStudentModel.setPracticalSkip(stSkipInfo.get(LessonType.PRACTICAL.getKey()));
				lessonStudentModel.setLabSkip(stSkipInfo.get(LessonType.LAB.getKey()));
			}

			lessonStudentModelList.add(lessonStudentModel);
		}
	}

	private void updateLessonStudents() {
		generateLessonStudents(lessonAbsentStudents, absentStudents);
		generateLessonStudents(lessonPresentStudents, presentStudents);
	}

	public void addLessonStudent(LessonStudentModel lessonStudentModel) {
		addStudent(lessonStudentModel.getStudent());
	}

	public void removeLessonStudent(LessonStudentModel lessonStudentModel) {
		removeStudent(lessonStudentModel.getStudent());
	}


	public String getStudentSkip(Student student) {
		LocaleUtils localeUtils = new LocaleUtils();
		Map<String, Integer> studentSkipInfoMap = skipInfo.get(student.getId());
		if (studentSkipInfoMap != null) {
			String totalSkip = "";
			List<String> skipInfoList = new ArrayList<>();
			for (Map.Entry<String, Integer> skipInfo : studentSkipInfoMap.entrySet()) {
				if (!Constants.TOTAL_SKIP.equals(skipInfo.getKey())) {
					skipInfoList.add(localeUtils.getMessage(skipInfo.getKey()) + " " + skipInfo.getValue());
				} else {
					totalSkip = Integer.toString(skipInfo.getValue());
				}
			}
			return localeUtils.getMessage("label.skips") + " " + totalSkip + " (" +
					skipInfoList.stream().collect(Collectors.joining(" / ")) + ")";
		}
		return localeUtils.getMessage("lesson.visit.noSkip");
	}

	public void onStudentRowSelect(SelectEvent event) {
		processedStudent = ((LessonStudentModel) event.getObject()).getStudent();
	}

	public void onPresentStudentsSelect(ToggleSelectEvent event) {
		selectedAllPresentStudents = event.isSelected();
	}

	public void onAbsentStudentsSelect(ToggleSelectEvent event) {
		selectedAllAbsentStudents = event.isSelected();
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

				lessonAbsentStudents = new ArrayList<>();
				lessonPresentStudents = new ArrayList<>();

				updateLessonStudents();

				absentStudentsLazyModel = new LazyStudentDataModel(lessonAbsentStudents);
				presentStudentsLazyModel = new LazyStudentDataModel(lessonPresentStudents);
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


	public LazyStudentDataModel getPresentStudentsLazyModel() {
		return presentStudentsLazyModel;
	}

	public void setPresentStudentsLazyModel(LazyStudentDataModel presentStudentsLazyModel) {
		this.presentStudentsLazyModel = presentStudentsLazyModel;
	}

	public LazyStudentDataModel getAbsentStudentsLazyModel() {
		return absentStudentsLazyModel;
	}

	public void setAbsentStudentsLazyModel(LazyStudentDataModel absentStudentsLazyModel) {
		this.absentStudentsLazyModel = absentStudentsLazyModel;
	}


	public List<LessonStudentModel> getSelectedPresentStudents() {
		return selectedPresentStudents;
	}

	public void setSelectedPresentStudents(List<LessonStudentModel> selectedPresentStudents) {
		this.selectedPresentStudents = selectedPresentStudents;
	}

	public List<LessonStudentModel> getSelectedAbsentStudents() {
		return selectedAbsentStudents;
	}

	public void setSelectedAbsentStudents(List<LessonStudentModel> selectedAbsentStudents) {
		this.selectedAbsentStudents = selectedAbsentStudents;
	}
}
