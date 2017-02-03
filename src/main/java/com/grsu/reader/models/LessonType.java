package com.grsu.reader.models;

/**
 * Created by pavel on 2/3/17.
 */
public class LessonType {
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
}
