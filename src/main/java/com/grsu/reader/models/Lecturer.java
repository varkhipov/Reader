package com.grsu.reader.models;

public class Lecturer extends Person {

	@Override
	public String toString() {
		return "Lecturer{" +
				"id=" + getId() +
				", uid='" + getUid() + '\'' +
				", name='" + getName() + '\'' +
				", surname='" + getSurname() + '\'' +
				", patronymic='" + getPatronymic() + '\'' +
				", phone='" + getPhone() + '\'' +
				", email='" + getEmail() + '\'' +
				", notes='" + getNotes() + '\'' +
//				", picture=" + getPicture() +
				'}';
	}
}
