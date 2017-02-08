package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@ManagedBean(name = "newInstanceOfGroup")
public class Group extends Entity {
	private int id;
	private String name;
	private Department department;
	private List<Student> students;

	public Group() {}

	public Group(String name) {
		this.name = name;
	}

	public Group(int id, String name, Department department) {
		this.id = id;
		this.name = name;
		this.department = department;
	}

	public Group(Group group) {
		this.id = group.getId();
		this.name = group.getName();
		this.department = group.getDepartment();
	}

	public SelectItem getSelectItem() {
		return new SelectItem(id, name);
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
		this.name = isEmpty(name) ? null : name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				", name='" + name + '\'' +
				", department=" + department +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Group group = (Group) o;

		if (id != group.id) return false;
		if (name != null ? !name.equals(group.name) : group.name != null) return false;
		if (department != null ? !department.equals(group.department) : group.department != null) return false;
		return students != null ? students.equals(group.students) : group.students == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (department != null ? department.hashCode() : 0);
		result = 31 * result + (students != null ? students.hashCode() : 0);
		return result;
	}
}
