package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.constants.Constants.GROUPS_DELIMITER;

@ManagedBean(name = "newInstanceOfStream")
public class Stream extends Entity {
	private int id;
	private String name;
	private String description;
	private Discipline discipline;
	private Department department;
	private Integer course;
	private List<Group> groups;

	public Stream() {
	}

	public Stream(int id, String name, String description, Discipline discipline, Department department, Integer course, List<Group> groups) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.discipline = discipline;
		this.department = department;
		this.course = course;
		this.groups = groups;
	}

	public Stream(Stream stream) {
		this(stream.getId(), stream.getName(), stream.getDescription(), stream.getDiscipline(), stream.getDepartment(), stream.getCourse(), stream.getGroups());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupNames() {
		StringBuilder sb = new StringBuilder();
		for (Group group : getGroups()) {
			sb.append(group.getName());
			sb.append(GROUPS_DELIMITER);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - GROUPS_DELIMITER.length());
		}
		return sb.toString();
	}

	public List<Group> getGroups() {
		if (groups == null) {
			return Collections.emptyList();
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Stream{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", discipline=" + discipline +
				", department=" + department +
				", course=" + course +
				", groups=" + groups +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Stream stream = (Stream) o;

		if (id != stream.id) return false;
		if (name != null ? !name.equals(stream.name) : stream.name != null) return false;
		if (description != null ? !description.equals(stream.description) : stream.description != null) return false;
		if (discipline != null ? !discipline.equals(stream.discipline) : stream.discipline != null) return false;
		if (department != null ? !department.equals(stream.department) : stream.department != null) return false;
		if (course != null ? !course.equals(stream.course) : stream.course != null) return false;
		return groups != null ? groups.equals(stream.groups) : stream.groups == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (discipline != null ? discipline.hashCode() : 0);
		result = 31 * result + (department != null ? department.hashCode() : 0);
		result = 31 * result + (course != null ? course.hashCode() : 0);
		result = 31 * result + (groups != null ? groups.hashCode() : 0);
		return result;
	}
}
