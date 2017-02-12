package com.grsu.reader.models;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Person extends Entity {
	private int id;
	private String cardUid;
	private int cardId;
	private String firstName;
	private String lastName;
	private String patronymic;
	private String phone;
	private String email;
	private Object image;

	public void setCardUidFromCardId(int cardId) {
		cardUid = Integer.toHexString(cardId).toUpperCase();
	}

	//http://stackoverflow.com/a/7038867/7464024
	public void setCardIdFromCardUid(String cardUid) {
		try {
			cardId = (int) Long.parseLong(cardUid, 16);
		} catch (NumberFormatException ex) {
			this.cardId = 0;
		}
	}

	public String getFullName() {
		return StringUtils.joinWith(" ", lastName, firstName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardUid() {
		return cardUid;
	}

	public void setCardUid(String cardUid) {
		if (isEmpty(cardUid)) {
			this.cardUid = null;
		} else {
			this.cardUid = cardUid.toUpperCase();
			setCardIdFromCardUid(this.cardUid);
		}
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
		if (this.cardId != 0) {
			setCardUidFromCardId(this.cardId);
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
				", cardUid='" + cardUid + '\'' +
				", cardId=" + cardId +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", patronymic='" + patronymic + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", image=" + image +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Person person = (Person) o;

		if (id != person.id) return false;
		if (cardId != person.cardId) return false;
		if (cardUid != null ? !cardUid.equals(person.cardUid) : person.cardUid != null) return false;
		if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
		if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
		if (patronymic != null ? !patronymic.equals(person.patronymic) : person.patronymic != null) return false;
		if (phone != null ? !phone.equals(person.phone) : person.phone != null) return false;
		if (email != null ? !email.equals(person.email) : person.email != null) return false;
		return image != null ? image.equals(person.image) : person.image == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (cardUid != null ? cardUid.hashCode() : 0);
		result = 31 * result + cardId;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (image != null ? image.hashCode() : 0);
		return result;
	}
}
