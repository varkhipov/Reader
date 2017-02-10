package com.grsu.reader.beans;

import com.grsu.reader.dao.ClassDAO;
import com.grsu.reader.dao.DepartmentDAO;
import com.grsu.reader.dao.DisciplineDAO;
import com.grsu.reader.dao.GroupDAO;
import com.grsu.reader.dao.LessonDAO;
import com.grsu.reader.dao.LessonTypeDAO;
import com.grsu.reader.dao.ScheduleDAO;
import com.grsu.reader.dao.StreamDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.models.Class;
import com.grsu.reader.models.Department;
import com.grsu.reader.models.Discipline;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Lesson;
import com.grsu.reader.models.LessonType;
import com.grsu.reader.models.Schedule;
import com.grsu.reader.models.Stream;
import com.grsu.reader.models.Student;
import com.grsu.reader.utils.CSVUtils;
import com.grsu.reader.utils.SerialUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
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
	private List<Class> classes;
	private List<LessonType> lessonTypes;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@PostConstruct
	public void connect() {
		try {
			databaseBean.connect();
			if (!databaseBean.isConnected()) {
				System.out.println("No connection to db.");
			} else {
				setConnected(true);
				initData();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void disconnect() {
		SerialUtils.disconnect();
		databaseBean.disconnect();
		if (databaseBean.isConnected()) {
			System.out.println("Still not disconnected.");
		} else {
			setConnected(false);
		}
	}

	public void initData() {
		CSVUtils.updateGroupsFromCSV(databaseBean.getConnection());
		updateSchedules();
		updateDisciplines();
		updateDepartments();
		updateStreams();
		updateGroups();
		updateStudents();
		updateLessons();
		updateClasses();
		updateLessonTypes();
	}

	public void updateSchedules() {
		schedules = ScheduleDAO.getSchedules(databaseBean.getConnection());
	}

	public void updateDisciplines() {
		disciplines = DisciplineDAO.getDisciplines(databaseBean.getConnection());
	}

	public void updateDepartments() {
		departments = DepartmentDAO.getDepartments(databaseBean.getConnection());
	}

	public void updateStreams() {
		streams = StreamDAO.getStreams(databaseBean.getConnection());
	}

	public void updateGroups() {
		groups = GroupDAO.getGroups(databaseBean.getConnection());
	}

	public void updateStudents() {
		students = StudentDAO.getStudents(databaseBean.getConnection());
	}

	public void updateLessons() {
		lessons = LessonDAO.getLessons(databaseBean.getConnection());
	}

	public void updateClasses() {
		classes = ClassDAO.getClasses(databaseBean.getConnection());
	}

	public void updateLessonTypes() {
		lessonTypes = LessonTypeDAO.getLessonTypes(databaseBean.getConnection());
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

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
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Stream> getStreams() {
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}
}
