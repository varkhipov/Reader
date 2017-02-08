package com.grsu.reader.models;

import javax.faces.model.SelectItem;

/**
 * Created by pavel on 2/3/17.
 */
public class LessonType extends Entity {
	private int id;
	private String name;

	public LessonType() {
	}

	public LessonType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, name);
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
		return "LessonType{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LessonType that = (LessonType) o;

		if (id != that.id) return false;
		return !(name != null ? !name.equals(that.name) : that.name != null);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
