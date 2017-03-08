package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.Class;
import com.grsu.reader.entities.Department;
import com.grsu.reader.entities.Discipline;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Lesson;
import com.grsu.reader.entities.LessonType;
import com.grsu.reader.entities.Schedule;
import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;
import com.grsu.reader.utils.CSVUtils;
import com.grsu.reader.utils.PhotoStudentUtils;
import com.grsu.reader.utils.SerialUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

	private boolean connected;
	private String activeView = "lessons";

	private List<Schedule> schedules;
	private List<Discipline> disciplines;
	private List<Department> departments;
	private List<Stream> streams;
	private List<Group> groups;
	private List<Student> students;
	private List<Lesson> lessons;
	private List<LessonType> lessonTypes;

	@PostConstruct
	public void connect() {
		setConnected(true);
		initData();
	}

	@PreDestroy
	public void disconnect() {
		SerialUtils.disconnect();
		setConnected(false);
	}

	public void initData() {
		updateGroupsFromCSV();
	}

	public List<Group> updateGroupsFromCSV() {
		return CSVUtils.updateGroupsFromCSV();
	}

	public void updateEntities() {
		schedules = null;
		disciplines = null;
		departments = null;
		streams = null;
		groups = null;
		students = null;
		lessons = null;
		lessonTypes = null;
	}

	public void updateSchedules() {
		schedules = new EntityDAO().getAll(Schedule.class);
	}

	public void updateDisciplines() {
		disciplines = new EntityDAO().getAll(Discipline.class);
	}

	public void updateDepartments() {
		departments = new EntityDAO().getAll(Department.class);
	}

	public void updateStreams() {
		streams = new EntityDAO().getAll(Stream.class);
	}

	public void updateGroups() {
		groups = new EntityDAO().getAll(Group.class);
	}

	public void updateStudents() {
		students = new EntityDAO().getAll(Student.class);
	}

	public void updateLessons() {
		lessons = new EntityDAO().getAll(Lesson.class);
	}

	public void updateLessonTypes() {
		lessonTypes = new EntityDAO().getAll(LessonType.class);
	}

	public void loadStudentsPhoto() {
		PhotoStudentUtils.storeStudentsPhoto(students);
	}

	/* GETTERS AND SETTERS*/
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getActiveView() {
		return activeView;
	}

	public void setActiveView(String activeView) {
		this.activeView = activeView;
	}

	public List<Schedule> getSchedules() {
		if (schedules == null) {
			updateSchedules();
		}
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public List<Discipline> getDisciplines() {
		if (disciplines == null) {
			updateDisciplines();
		}
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<Department> getDepartments() {
		if (departments == null) {
			updateDepartments();
		}
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Stream> getStreams() {
		if (streams == null) {
			updateStreams();
		}
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public List<Group> getGroups() {
		if (groups == null) {
			updateGroups();
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Student> getStudents() {
		if (students == null) {
			updateStudents();
		}
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Lesson> getLessons() {
		if (lessons == null) {
			updateLessons();
		}
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<LessonType> getLessonTypes() {
		if (lessonTypes == null) {
			updateLessonTypes();
		}
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}

}
