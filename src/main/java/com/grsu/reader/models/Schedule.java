package com.grsu.reader.models;

import java.time.LocalTime;

public class Schedule {
	private int id;
	private LocalTime begin;
	private LocalTime end;

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

	@Override
	public String toString() {
		return "Schedule{" +
				"id=" + id +
				", begin=" + begin +
				", end=" + end +
				'}';
	}
}
