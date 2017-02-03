package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.List;

@ManagedBean(name = "newInstanceOfDiscipline")
public class Course extends Entity {
	private int id;
	private String name;
	private String description;
	private Stream stream;

	public Course() {
	}

	public Course(int id, String name, String description, Stream stream) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.stream = stream;
	}

	public Course(Course course) {
		this(course.getId(), course.getName(), course.getDescription(), course.getStream());
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

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	@Override
	public String toString() {
		return "Course{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", stream=" + stream +
				'}';
	}
}
