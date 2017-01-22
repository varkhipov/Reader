package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.constants.Constants.GROUPS_DELIMITER;

@ManagedBean(name = "newInstanceOfStream")
public class Stream extends Entity {
	private int id;
	private String name;
	private List<Group> groups;

	public Stream() {}

	public Stream(int id, String name, List<Group> groups) {
		this.id = id;
		this.name = name;
		this.groups = groups;
	}

	public Stream(Stream stream) {
		this.id = stream.getId();
		this.name = stream.getName();
		this.groups = stream.getGroups();
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

	public String getGroupNames() {
		StringBuilder sb = new StringBuilder();
		for (Group group : getGroups()) {
			sb.append(group.getName());
			sb.append(GROUPS_DELIMITER);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - GROUPS_DELIMITER.length());
		}
		return sb.toString();
	}

	public List<Group> getGroups() {
		if (groups == null) {
			return Collections.emptyList();
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "Stream{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
