package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@ManagedBean(name = "newInstanceOfGroup")
public class Group extends Entity {
	private int id;
	private String name;
	private Faculty faculty;

	public Group() {}

	public Group(int id, String name, Faculty faculty) {
		this.id = id;
		this.name = name;
		this.faculty = faculty;
	}

	public Group(Group group) {
		this.id = group.getId();
		this.name = group.getName();
		this.faculty = group.getFaculty();
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

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				", name='" + name + '\'' +
				", faculty=" + faculty +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Group) {
			Group group = (Group) obj;
			return StringUtils.equals(name, group.getName())
					&& Objects.equals(faculty, group.getFaculty());
		}
		return false;
	}
}
