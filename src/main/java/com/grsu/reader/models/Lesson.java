package com.grsu.reader.models;

import com.grsu.reader.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ManagedBean(name = "newInstanceOfLesson")
public class Lesson extends Entity {
	private int id;
	private String name;
	private int timeBefore;
	private int timeAfter;
	private LocalDateTime createDate;
	private Discipline discipline;
	private Lecturer lecturer;
	private Stream stream;
	private List<Class> classes;

	public Lesson() {}

	public Lesson(Lesson lesson) {
		id = lesson.getId();
		name = lesson.getName();
		timeBefore = lesson.getTimeBefore();
		timeAfter = lesson.getTimeAfter();
		createDate = lesson.getCreateDate();
		discipline = lesson.getDiscipline();
		lecturer = lesson.getLecturer();
		stream = lesson.getStream();
		classes = lesson.getClasses();
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, name);
	}

	public String getDate() {
		return DateUtils.formatDate(createDate);
	}

	public String getTime() {
		return DateUtils.formatTime(createDate);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimeBefore() {
		return timeBefore;
	}

	public void setTimeBefore(int timeBefore) {
		this.timeBefore = timeBefore;
	}

	public int getTimeAfter() {
		return timeAfter;
	}

	public void setTimeAfter(int timeAfter) {
		this.timeAfter = timeAfter;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Lecturer getLecturer() {
		return lecturer;
	}

	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}
	
	@Override
	public String toString() {
		return "Lesson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", timeBefore=" + timeBefore +
				", timeAfter=" + timeAfter +
				", createDate=" + createDate +
				", discipline=" + discipline +
				", lecturer=" + lecturer +
				", stream=" + stream +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;

		if (obj.getClass() != null && obj instanceof Lesson) {
			Lesson lesson = (Lesson) obj;
			return StringUtils.equals(name, lesson.getName())
					&& Objects.equals(discipline, lesson.getDiscipline())
					&& Objects.equals(stream, lesson.getStream());
		}
		return false;
	}
}
