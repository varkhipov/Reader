package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name = "newInstanceOfDiscipline")
public class Discipline extends Entity {
	private int id;
	private String name;

	public Discipline() {}

	public Discipline(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Discipline(Discipline discipline) {
		this.id = discipline.getId();
		this.name = discipline.getName();
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
		return "Discipline{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
