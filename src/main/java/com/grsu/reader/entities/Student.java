package com.grsu.reader.entities;

import com.grsu.reader.models.SkipInfo;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.grsu.reader.constants.Constants.GROUPS_DELIMITER;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@SqlResultSetMapping(
		name = "SkipInfoMapping",
		classes = {
				@ConstructorResult(
						targetClass = SkipInfo.class,
						columns = {
								@ColumnResult(name = "studentId", type = Integer.class),
								@ColumnResult(name = "lessonType", type = Integer.class),
								@ColumnResult(name = "count", type = int.class)
						}
				)
		}
)
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "SkipInfoQuery",
				query = "select st.id as studentId, l.type_id as lessonType, count(*) as count\n" +
						"from STUDENT st\n" +
						"join STUDENT_CLASS sc on sc.student_id = st.id\n" +
						"join CLASS cl on cl.id = sc.class_id \n" +
						"join SCHEDULE sch on sch.id = cl.schedule_id \n" +
						"join LESSON l on l.id = cl.lesson_id\n" +
						"join STREAM str on str.id = l.stream_id\n" +
						"where (sc.registered is null or sc.registered = 0) and str.id = :streamId " +
						"and ((date(cl.date) < date('now', 'localtime')) or (date(cl.date) = date('now', 'localtime') and time(sch.begin) <= time('now', 'localtime')))\n" +
						"group by st.id, str.id, l.type_id",
				resultSetMapping = "SkipInfoMapping"),
		@NamedNativeQuery(
				name = "StudentSkipInfoQuery",
				query = "select st.id as studentId, l.type_id as lessonType, count(*) as count\n" +
						"from STUDENT st\n" +
						"join STUDENT_CLASS sc on sc.student_id = st.id\n" +
						"join CLASS cl on cl.id = sc.class_id " +
						"join SCHEDULE sch on sch.id = cl.schedule_id " +
						"join LESSON l on l.id = cl.lesson_id\n" +
						"join STREAM str on str.id = l.stream_id\n" +
						"where st.id in (:studentId) and (sc.registered is null or sc.registered = 0) and str.id = :streamId " +
						"and ((date(cl.date) < date('now', 'localtime')) or (date(cl.date) = date('now', 'localtime') and time(sch.begin) <= time('now', 'localtime')))\n" +
						"group by st.id, str.id, l.type_id",
				resultSetMapping = "SkipInfoMapping")
})
@Entity
@ManagedBean(name = "newInstanceOfStudent")
public class Student implements AssistantEntity, Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "STUDENT_GROUP",
			joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private List<Group> groups;

	@MapKey(name = "classId")
	@OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
	private Map<Integer, StudentClass> studentClasses;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", referencedColumnName = "id")
	@Where(clause = "type = 'STUDENT'")
	private List<Note> notes;

	public Student() {
	}

	public Student(Student student) {
		this.cardUid = student.cardUid;
		this.cardId = student.cardId;
		this.firstName = student.firstName;
		this.lastName = student.lastName;
		this.patronymic = student.patronymic;
		this.phone = student.phone;
		this.email = student.email;
		this.groups = student.groups;
		this.studentClasses = student.studentClasses;
	}

	public String getFullName() {
		return String.join(" ", lastName, firstName);
	}

	public String getGroupNames() {
		return groups.stream().map(Group::getName).collect(Collectors.joining(GROUPS_DELIMITER));
	}

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

	/* GETTERS & SETTERS */
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
		if (isEmpty(cardUid)) {
			this.cardUid = null;
		} else {
			this.cardUid = cardUid.toUpperCase();
			setCardIdFromCardUid(this.cardUid);
		}
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
		if (this.cardId != 0) {
			setCardUidFromCardId(this.cardId);
		}
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

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Map<Integer, StudentClass> getStudentClasses() {
		return studentClasses;
	}

	public void setStudentClasses(Map<Integer, StudentClass> studentClasses) {
		this.studentClasses = studentClasses;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
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
				'}';
	}
}
