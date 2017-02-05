package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Objects;

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
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Group) {
			Group group = (Group) obj;
			return StringUtils.equals(name, group.getName())
					&& Objects.equals(department, group.getDepartment());
		}
		return false;
	}
}
