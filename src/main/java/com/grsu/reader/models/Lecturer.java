package com.grsu.reader.models;

public class Lecturer extends Person {



	@Override
	public String toString() {
		return "Lecturer{" +
				"id=" + getId() +
				", uid='" + getUid() + '\'' +
				", firstName='" + getFirstName() + '\'' +
				", lastName='" + getLastName() + '\'' +
				", patronymic='" + getPatronymic() + '\'' +
				", phone='" + getPhone() + '\'' +
				", email='" + getEmail() + '\'' +
//				", picture=" + getImage() +
				'}';
	}
}
