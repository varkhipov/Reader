package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name = "newInstanceOfDepartment")
public class Department extends Entity {
	private int id;
	private String name;
	private String abbreviation;

	public Department() {
	}

	public Department(int id, String name, String abbreviation) {
		this.id = id;
		this.name = name;
		this.abbreviation = abbreviation;
	}

	public Department(int id, String name) {
		this(id, name, null);
	}

	public Department(Department department) {
		this(department.getId(), department.getName(), department.getAbbreviation());
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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Override
	public String toString() {
		return "Department{" +
				"id=" + id +
				", name='" + name + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Department) {
			Department department = (Department) obj;
			return StringUtils.equals(name, department.getName());
		}
		return false;
	}
}
