package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.dao.LessonDAO;
import com.grsu.reader.entities.Department;
import com.grsu.reader.entities.Discipline;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Lesson;
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
import java.util.stream.Collectors;

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
		updateEntities();
	}

	public List<Group> updateGroupsFromCSV() {
		return CSVUtils.updateGroupsFromCSV();
	}

	public void updateEntities() {
		updateSchedules();
		updateDisciplines();
		updateDepartments();
		updateStreams();
		updateGroups();
		updateStudents();
		updateLessons();
	}

	public void updateSchedules() {
		schedules = null;
	}

	public void updateDisciplines() {
		disciplines = null;
	}

	public void updateDepartments() {
		departments = null;
	}

	public void updateStreams() {
		streams = null;
	}

	public void updateGroups() {
		groups = null;
	}

	public void updateStudents() {
		students = null;
	}

	public void updateLessons() {
		lessons = null;
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
			schedules = EntityDAO.getAll(Schedule.class);
		}
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public List<Discipline> getDisciplines() {
		if (disciplines == null) {
			disciplines = EntityDAO.getAll(Discipline.class);
		}
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<Department> getDepartments() {
		if (departments == null) {
			departments = EntityDAO.getAll(Department.class);
		}
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Stream> getStreams() {
		if (streams == null) {
			streams = EntityDAO.getAll(Stream.class);
		}
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public List<Group> getGroups() {
		if (groups == null) {
			groups = EntityDAO.getAll(Group.class);
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Student> getStudents() {
		if (students == null) {
			students = EntityDAO.getAll(Student.class);
			students = students != null ? students.stream().sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName())).collect(Collectors.toList()) : null;
		}
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Lesson> getLessons() {
		if (lessons == null) {
			lessons = (new LessonDAO()).getAll();
		}
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

}
