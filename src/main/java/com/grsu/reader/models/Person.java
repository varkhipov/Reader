package com.grsu.reader.models;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Person {
	private int id;
	private String uid;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String email;
	private String notes;
	private Object picture;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = isEmpty(uid) ? null : uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = isEmpty(name) ? null : name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = isEmpty(surname) ? null : surname;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = isEmpty(patronymic) ? null : patronymic;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = isEmpty(phone) ? null : phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = isEmpty(email) ? null : email;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = isEmpty(notes) ? null : notes;
	}

	public Object getPicture() {
		return picture;
	}

	public void setPicture(Object picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", uid='" + uid + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", patronymic='" + patronymic + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", notes='" + notes + '\'' +
//				", picture=" + picture +
				'}';
	}
}
