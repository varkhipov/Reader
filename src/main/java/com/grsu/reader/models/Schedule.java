package com.grsu.reader.models;

import javax.faces.model.SelectItem;
import java.time.LocalTime;

public class Schedule extends Entity {
	private int id;
	private LocalTime begin;
	private LocalTime end;
	private String number;

	public Schedule() {}

	public Schedule(int id, LocalTime begin, LocalTime end, String number) {
		this.id = id;
		this.begin = begin;
		this.end = end;
		this.number = number;
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, String.format("[%s] %s - %s", number, begin, end));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalTime getBegin() {
		return begin;
	}

	public void setBegin(LocalTime begin) {
		this.begin = begin;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Schedule{" +
				"id=" + id +
				", begin=" + begin +
				", end=" + end +
				", number='" + number + '\'' +
				'}';
	}
}
