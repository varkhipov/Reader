package com.grsu.reader.models;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.time.LocalDate;
import java.time.LocalTime;

@ManagedBean(name = "newInstanceOfClass")
public class Class extends Entity {
	private int id;
	private LocalDate date;
	private Schedule schedule;
	private LocalTime sessionStart;
	private LocalTime sessionEnd;

	public Class() {}

	public Class(int id, LocalDate date, Schedule schedule, LocalTime sessionStart, LocalTime sessionEnd) {
		this.id = id;
		this.date = date;
		this.schedule = schedule;
		this.sessionStart = sessionStart;
		this.sessionEnd = sessionEnd;
	}

	public Class(LocalDate date, Schedule schedule) {
		this(0, date, schedule, null, null);
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, String.format("[%s] %s", schedule.getNumber(), date));
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

	public LocalTime getSessionStart() {
		return sessionStart;
	}

	public void setSessionStart(LocalTime sessionStart) {
		this.sessionStart = sessionStart;
	}

	public LocalTime getSessionEnd() {
		return sessionEnd;
	}

	public void setSessionEnd(LocalTime sessionEnd) {
		this.sessionEnd = sessionEnd;
	}

	@Override
	public String toString() {
		return "Class{" +
				"id=" + id +
				", date=" + date +
				", schedule=" + schedule +
				", sessionStart=" + sessionStart +
				", sessionEnd=" + sessionEnd +
				'}';
	}
}
