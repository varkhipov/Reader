package com.grsu.reader.models;

import javax.faces.model.SelectItem;
import java.time.LocalTime;

public class Schedule extends Entity {
	private int id;
	private LocalTime begin;
	private LocalTime end;
	private String number;

	public Schedule() {
	}

	public Schedule(int id, LocalTime begin, LocalTime end, String number) {
		this.id = id;
		this.begin = begin;
		this.end = end;
		this.number = number;
	}

	public String getCaption() {
		return String.format("[%s] %s - %s", number, begin, end);
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, getCaption());
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Schedule schedule = (Schedule) o;

		if (id != schedule.id) return false;
		if (begin != null ? !begin.equals(schedule.begin) : schedule.begin != null) return false;
		if (end != null ? !end.equals(schedule.end) : schedule.end != null) return false;
		return !(number != null ? !number.equals(schedule.number) : schedule.number != null);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (begin != null ? begin.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (number != null ? number.hashCode() : 0);
		return result;
	}
}
