package com.grsu.reader.models;

import com.grsu.reader.entities.Lesson;
import com.grsu.reader.utils.DateUtils;

/**
 * Created by pavel on 3/20/17.
 */
public class LessonModel {
	private Integer id;
	private Lesson lesson;
	private LessonType type;
	private String date;
	private Integer number;

	public LessonModel(Lesson lesson, Integer number) {
		id = lesson.getId();
		this.lesson = lesson;
		type = LessonType.getLessonTypeByCode(lesson.getType().getId());
		date = DateUtils.formatDate(lesson.getClazz().getDate(), DateUtils.FORMAT_DATE_SHORT_YEAR);
		this.number = number;
	}

	public LessonModel(Lesson lesson) {
		this(lesson, null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public LessonType getType() {
		return type;
	}

	public void setType(LessonType type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "LessonModel{" +
				"id=" + id +
				", lesson=" + lesson +
				", type=" + type +
				", date='" + date + '\'' +
				", number=" + number +
				'}';
	}
}
