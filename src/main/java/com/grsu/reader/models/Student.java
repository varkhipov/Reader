package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import java.util.Objects;

@ManagedBean(name = "newInstanceOfStudent")
public class Student extends Person {

	public Student() {
	}

	public Student(Student student) {
		setId(student.getId());
		setUid(student.getUid());
		setName(student.getName());
		setSurname(student.getSurname());
		setPatronymic(student.getPatronymic());
		setPhone(student.getPhone());
		setEmail(student.getEmail());
		setNotes(student.getNotes());
		setGroup(student.getGroup());
	}

	private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + getId() +
				", uid='" + getUid() + '\'' +
				", name='" + getName() + '\'' +
				", surname='" + getSurname() + '\'' +
				", patronymic='" + getPatronymic() + '\'' +
				", phone='" + getPhone() + '\'' +
				", email='" + getEmail() + '\'' +
				", notes='" + getNotes() + '\'' +
				", group=" + group +
//				", picture=" + getPicture() +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Student) {
			Student student = (Student) obj;
			return StringUtils.equals(getUid(), student.getUid())
					&& StringUtils.equals(getName(), student.getName())
					&& StringUtils.equals(getSurname(), student.getSurname())
					&& StringUtils.equals(getPatronymic(), student.getPatronymic())
					&& StringUtils.equals(getPhone(), student.getPhone())
					&& StringUtils.equals(getEmail(), student.getEmail())
					&& StringUtils.equals(getNotes(), student.getNotes())
					&& Objects.equals(getGroup(), student.getGroup());
		}
		return false;
	}
}
