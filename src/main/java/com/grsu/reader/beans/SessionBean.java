package com.grsu.reader.beans;

import com.grsu.reader.dao.*;
import com.grsu.reader.models.*;
import com.grsu.reader.models.Class;
import com.grsu.reader.utils.CSVUtils;
import com.grsu.reader.utils.SerialUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import static com.grsu.reader.utils.EntityUtils.generateSelectItems;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

	private boolean connected;
	private String activeView = "lessons";

	private List<Schedule> schedules;
	private List<SelectItem> schedulesItems;
	private List<Discipline> disciplines;
	private List<SelectItem> disciplinesItems;
	private List<Department> departments;
	private List<SelectItem> departmentsItems;
	private List<Stream> streams;
	private List<SelectItem> streamsItems;
	private List<Group> groups;
	private List<SelectItem> groupsItems;
	private List<Student> students;
	private List<SelectItem> studentsItems;
	private List<Lesson> lessons;
	private List<SelectItem> lessonsItems;
	private List<Class> classes;
	private List<SelectItem> classesItems;
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
		schedulesItems = generateSelectItems(schedules);
	}

	public void updateDisciplines() {
		disciplines = DisciplineDAO.getDisciplines(databaseBean.getConnection());
		disciplinesItems = generateSelectItems(disciplines);
	}

	public void updateDepartments() {
		departments = DepartmentDAO.getDepartments(databaseBean.getConnection());
		departmentsItems = generateSelectItems(departments);
	}

	public void updateStreams() {
		streams = StreamDAO.getStreams(databaseBean.getConnection());
		streamsItems = generateSelectItems(streams);
	}

	public void updateGroups() {
		groups = GroupDAO.getGroups(databaseBean.getConnection());
		groupsItems = generateSelectItems(groups);
	}

	public void updateStudents() {
		students = StudentDAO.getStudents(databaseBean.getConnection());
		studentsItems = generateSelectItems(students);
	}

	public void updateLessons() {
		lessons = LessonDAO.getLessons(databaseBean.getConnection());
		lessonsItems = generateSelectItems(lessons);
	}

	public void updateClasses() {
		classes = ClassDAO.getClasses(databaseBean.getConnection());
		classesItems = generateSelectItems(classes);
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

	public List<SelectItem> getSchedulesItems() {
		return schedulesItems;
	}

	public void setSchedulesItems(List<SelectItem> schedulesItems) {
		this.schedulesItems = schedulesItems;
	}

	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<SelectItem> getDisciplinesItems() {
		return disciplinesItems;
	}

	public void setDisciplinesItems(List<SelectItem> disciplinesItems) {
		this.disciplinesItems = disciplinesItems;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<SelectItem> getDepartmentsItems() {
		return departmentsItems;
	}

	public void setDepartmentsItems(List<SelectItem> departmentsItems) {
		this.departmentsItems = departmentsItems;
	}

	public List<Stream> getStreams() {
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public List<SelectItem> getStreamsItems() {
		return streamsItems;
	}

	public void setStreamsItems(List<SelectItem> streamsItems) {
		this.streamsItems = streamsItems;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<SelectItem> getGroupsItems() {
		return groupsItems;
	}

	public void setGroupsItems(List<SelectItem> groupsItems) {
		this.groupsItems = groupsItems;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<SelectItem> getStudentsItems() {
		return studentsItems;
	}

	public void setStudentsItems(List<SelectItem> studentsItems) {
		this.studentsItems = studentsItems;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<SelectItem> getLessonsItems() {
		return lessonsItems;
	}

	public void setLessonsItems(List<SelectItem> lessonsItems) {
		this.lessonsItems = lessonsItems;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	public List<SelectItem> getClassesItems() {
		return classesItems;
	}

	public void setClassesItems(List<SelectItem> classesItems) {
		this.classesItems = classesItems;
	}

	public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}
}
