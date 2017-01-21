package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name = "newInstanceOfStream")
public class Stream extends Entity {
	private int id;
	private String name;

	public Stream() {}

	public Stream(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Stream(Stream stream) {
		this.id = stream.getId();
		this.name = stream.getName();
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
		return "Stream{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
