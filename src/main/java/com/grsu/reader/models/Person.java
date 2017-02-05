package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;
import javax.faces.model.SelectItem;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Person extends Entity {
	private int id;
	private String uid;
	private int parsedUid;
	private String firstName;
	private String lastName;
	private String patronymic;
	private String phone;
	private String email;
	private Object image;

	// TODO: give more clear names
	public void setIntToHexUid(int intUid) {
		uid = Integer.toHexString(intUid).toUpperCase();
	}

	//http://stackoverflow.com/a/7038867/7464024
	public void setHexToParsedUid(String hexUid) {
		try {
			parsedUid = (int) Long.parseLong(hexUid, 16);
		} catch (NumberFormatException ex) {
			this.parsedUid = 0;
		}
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, getFullName());
	}

	public String getFullName() {
		return StringUtils.joinWith(" ", lastName, firstName, patronymic);
	}

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
		if (isEmpty(uid)) {
			this.uid = null;
		} else {
			this.uid = uid.toUpperCase();
			setHexToParsedUid(this.uid);
		}
	}

	public int getParsedUid() {
		return parsedUid;
	}

	public void setParsedUid(int parsedUid) {
		this.parsedUid = parsedUid;
		if (this.parsedUid != 0) {
			setIntToHexUid(this.parsedUid);
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = isEmpty(firstName) ? null : firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = isEmpty(lastName) ? null : lastName;
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

	public Object getImage() {
		return image;
	}

	public void setImage(Object image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", uid='" + uid + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", patronymic='" + patronymic + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
//				", image=" + image +
				'}';
	}
}
