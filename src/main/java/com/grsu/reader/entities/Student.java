package com.grsu.reader.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
public class Student implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Column(name = "card_uid")
	private String cardUid;

	@Basic
	@Column(name = "card_id")
	private Integer cardId;

	@Basic
	@Column(name = "first_name")
	private String firstName;

	@Basic
	@Column(name = "last_name")
	private String lastName;

	@Basic
	@Column(name = "patronymic")
	private String patronymic;

	@Basic
	@Column(name = "phone")
	private String phone;

	@Basic
	@Column(name = "email")
	private String email;

	@Basic
	@Column(name = "image")
	private String image;

	@ManyToMany(mappedBy = "students")
	private List<Class> classes;

	@ManyToMany(mappedBy = "students")
	private List<Group> groups;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardUid() {
		return cardUid;
	}

	public void setCardUid(String cardUid) {
		this.cardUid = cardUid;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Student student = (Student) o;

		if (id != null ? !id.equals(student.id) : student.id != null) return false;
		if (cardUid != null ? !cardUid.equals(student.cardUid) : student.cardUid != null) return false;
		if (cardId != null ? !cardId.equals(student.cardId) : student.cardId != null) return false;
		if (firstName != null ? !firstName.equals(student.firstName) : student.firstName != null) return false;
		if (lastName != null ? !lastName.equals(student.lastName) : student.lastName != null) return false;
		if (patronymic != null ? !patronymic.equals(student.patronymic) : student.patronymic != null) return false;
		if (phone != null ? !phone.equals(student.phone) : student.phone != null) return false;
		if (email != null ? !email.equals(student.email) : student.email != null) return false;
		if (image != null ? !image.equals(student.image) : student.image != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (cardUid != null ? cardUid.hashCode() : 0);
		result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (image != null ? image.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", cardUid='" + cardUid + '\'' +
				", cardId='" + cardId + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", patronymic='" + patronymic + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", image='" + image + '\'' +
				'}';
	}
}
