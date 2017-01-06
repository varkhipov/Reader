package com.grsu.reader.models;

public class Student extends Person {
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
}
