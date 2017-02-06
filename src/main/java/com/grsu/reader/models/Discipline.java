package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name = "newInstanceOfDiscipline")
public class Discipline extends Entity {
	private int id;
	private String name;
	private String description;

	public Discipline() {
	}

	public Discipline(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Discipline(Discipline discipline) {
		this(discipline.getId(), discipline.getName(), discipline.getDescription());
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, name);
	}

	@Override
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Discipline{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
