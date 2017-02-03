package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.grsu.reader.constants.Constants.GROUPS_DELIMITER;

@ManagedBean(name = "newInstanceOfStudent")
public class Student extends Person {
	public Student() {}

	public Student(Student student) {
		setId(student.getId());
		setUid(student.getUid());
		setParsedUid(student.getParsedUid());
		setFirstName(student.getFirstName());
		setLastName(student.getLastName());
		setPatronymic(student.getPatronymic());
		setPhone(student.getPhone());
		setEmail(student.getEmail());
		setGroups(student.getGroups());
	}

	private List<Group> groups;

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
		return "Student{" +
				"id=" + getId() +
				", uid='" + getUid() + '\'' +
				", parsedUid='" + getParsedUid() + '\'' +
				", name='" + getFirstName() + '\'' +
				", surname='" + getLastName() + '\'' +
				", patronymic='" + getPatronymic() + '\'' +
				", phone='" + getPhone() + '\'' +
				", email='" + getEmail() + '\'' +
				", groups=" + groups +
//				", picture=" + getImage() +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Student) {
			Student student = (Student) obj;
			return StringUtils.equals(getUid(), student.getUid())
					&& getParsedUid() == student.getParsedUid()
					&& StringUtils.equals(getFirstName(), student.getFirstName())
					&& StringUtils.equals(getLastName(), student.getLastName())
					&& StringUtils.equals(getPatronymic(), student.getPatronymic())
					&& StringUtils.equals(getPhone(), student.getPhone())
					&& StringUtils.equals(getEmail(), student.getEmail())
					&& Objects.equals(getGroups(), student.getGroups());
		}
		return false;
	}
}
