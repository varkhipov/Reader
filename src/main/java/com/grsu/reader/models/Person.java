package com.grsu.reader.models;

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
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
