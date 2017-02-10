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
		return StringUtils.joinWith(" ", lastName, firstName, patronymic);
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
}
