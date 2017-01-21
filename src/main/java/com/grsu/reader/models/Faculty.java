package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name = "newInstanceOfFaculty")
public class Faculty extends Entity {
	private int id;
	private String name;

	public Faculty() {}

	public Faculty(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Faculty(Faculty faculty) {
		this.id = faculty.getId();
		this.name = faculty.getName();
	}

	@Override
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
		this.name = name;
	}

	@Override
	public String toString() {
		return "Faculty{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Faculty) {
			Faculty faculty = (Faculty) obj;
			return StringUtils.equals(name, faculty.getName());
		}
		return false;
	}
}
