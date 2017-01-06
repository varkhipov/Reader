package com.grsu.reader.models;

import com.grsu.reader.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lecture {
	private int id;
	private String name;
	private int timeBefore;
	private int timeAfter;
	private LocalDateTime dateTime;
	private Schedule schedule;
	private Discipline discipline;
	private Lecturer lecturer;
	private Group group;

	public String getDate() {
		return DateUtils.formatDate(dateTime);
	}

	public String getTime() {
		return DateUtils.formatTime(dateTime);
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

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Lecture{" +
				"id=" + id +
				", name='" + name + '\'' +
				", timeBefore=" + timeBefore +
				", timeAfter=" + timeAfter +
				", dateTime=" + dateTime +
				", schedule=" + schedule +
				", discipline=" + discipline +
				", lecturer=" + lecturer +
				", group=" + group +
				'}';
	}
}
