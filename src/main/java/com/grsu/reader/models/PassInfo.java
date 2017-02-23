package com.grsu.reader.models;

/**
 * Created by zaychick-pavel on 2/22/17.
 */
public class PassInfo {
	private Integer studentId;
	private Integer streamId;
	private LessonType lessonType;
	private int count;

	public PassInfo(Integer studentId, Integer streamId, Integer lessonType, int count) {
		this.studentId = studentId;
		this.streamId = streamId;
		this.lessonType = LessonType.getLessonTypeByCode(lessonType);
		this.count = count;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getStreamId() {
		return streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
