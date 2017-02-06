package com.grsu.reader.models;

import com.grsu.reader.utils.DateUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.time.LocalDateTime;
import java.util.List;

@ManagedBean(name = "newInstanceOfLesson")
public class Lesson extends Entity {
	private int id;
	private String name;
	private String description;
	private LocalDateTime createDate;
	private LessonType lessonType;
	private Stream stream;
	private Group group;
	private List<Class> classes;

	public Lesson() {
	}

	public Lesson(int id, String name, String description, LocalDateTime createDate, LessonType lessonType, Stream stream, Group group, List<Class> classes) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createDate = createDate;
		this.lessonType = lessonType;
		this.stream = stream;
		this.group = group;
		this.classes = classes;
	}

	public Lesson(Lesson lesson) {
		this(lesson.getId(), lesson.getName(), lesson.getDescription(), lesson.getCreateDate(), lesson.getLessonType(), lesson.getStream(),
				lesson.getGroup(), lesson.getClasses());
	}

	public String getFormattedCreateDate() {
		return DateUtils.formatDateTime(createDate);
	}

	@Override
	public SelectItem getSelectItem() {
		return new SelectItem(id, name);
	}

	@Override
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}

	@Override
	public String toString() {
		return "Lesson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", createDate=" + createDate +
				", lessonType=" + lessonType +
				", stream=" + stream +
				", group=" + group +
				", classes=" + classes +
				'}';
	}
}
