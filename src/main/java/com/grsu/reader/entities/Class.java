package com.grsu.reader.entities;

import com.grsu.reader.converters.db.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by zaychick-pavel on 2/9/17.
 */
@Entity
@Table(name = "CLASS")
public class Class implements AssistantEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "date")
	private LocalDateTime date;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "session_start")
	private LocalDateTime sessionStart;

	@Basic
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "session_end")
	private LocalDateTime sessionEnd;

	@ManyToOne
	@JoinColumn(name = "lesson_id", referencedColumnName = "id")
	private Lesson lesson;

	@ManyToOne
	@JoinColumn(name = "schedule_id", referencedColumnName = "id")
	private Schedule schedule;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "STUDENT_CLASS",
			joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
	private List<Student> students;

	@MapKey(name = "studentId")
	@OneToMany(mappedBy = "clazz", fetch = FetchType.EAGER)
	private Map<Object, StudentClass> studentClasses;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getSessionStart() {
		return sessionStart;
	}

	public void setSessionStart(LocalDateTime sessionStart) {
		this.sessionStart = sessionStart;
	}

	public LocalDateTime getSessionEnd() {
		return sessionEnd;
	}

	public void setSessionEnd(LocalDateTime sessionEnd) {
		this.sessionEnd = sessionEnd;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Map<Object, StudentClass> getStudentClasses() {
		return studentClasses;
	}

	public void setStudentClasses(Map<Object, StudentClass> studentClasses) {
		this.studentClasses = studentClasses;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Class aClass = (Class) o;

		if (id != null ? !id.equals(aClass.id) : aClass.id != null) return false;
		if (date != null ? !date.equals(aClass.date) : aClass.date != null) return false;
		if (sessionStart != null ? !sessionStart.equals(aClass.sessionStart) : aClass.sessionStart != null)
			return false;
		if (sessionEnd != null ? !sessionEnd.equals(aClass.sessionEnd) : aClass.sessionEnd != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (sessionStart != null ? sessionStart.hashCode() : 0);
		result = 31 * result + (sessionEnd != null ? sessionEnd.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Class{" +
				"id=" + id +
				", date='" + date + '\'' +
				", sessionStart='" + sessionStart + '\'' +
				", sessionEnd='" + sessionEnd + '\'' +
				'}';
	}
}
