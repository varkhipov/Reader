package com.grsu.reader.models;

import java.time.LocalDate;

public class Class {
	private int id;
	private LocalDate date;
	private Schedule schedule;

	public Class() {}

	public Class(int id, LocalDate date, Schedule schedule) {
		this.id = id;
		this.date = date;
		this.schedule = schedule;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		return "Class{" +
				"id=" + id +
				", date=" + date +
				", schedule=" + schedule +
				'}';
	}
}
