package com.grsu.reader.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by pavel on 2/8/17.
 */
@Entity
public class Schedule {
	private Integer id;
	private String begin;
	private String end;
	private Integer number;

	@Id
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Basic
	@Column(name = "begin")
	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	@Basic
	@Column(name = "end")
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Basic
	@Column(name = "number")
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Schedule schedule = (Schedule) o;

		if (id != null ? !id.equals(schedule.id) : schedule.id != null) return false;
		if (begin != null ? !begin.equals(schedule.begin) : schedule.begin != null) return false;
		if (end != null ? !end.equals(schedule.end) : schedule.end != null) return false;
		if (number != null ? !number.equals(schedule.number) : schedule.number != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (begin != null ? begin.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (number != null ? number.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Schedule{" +
				"id=" + id +
				", begin='" + begin + '\'' +
				", end='" + end + '\'' +
				", number=" + number +
				'}';
	}
}
