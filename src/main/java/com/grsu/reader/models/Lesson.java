package com.grsu.reader.models;

import com.grsu.reader.utils.DateUtils;

import java.time.LocalDateTime;

public class Lesson {
	private int id;
	private String name;
	private int timeBefore;
	private int timeAfter;
	private LocalDateTime createDate;
	private Discipline discipline;
	private Lecturer lecturer;
	private Stream stream;

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
}
