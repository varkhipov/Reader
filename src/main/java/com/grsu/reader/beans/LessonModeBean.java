package com.grsu.reader.beans;

import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zaychick-pavel on 3/3/17.
 */
@ManagedBean(name = "lessonModeBean")
@ViewScoped
public class LessonModeBean implements Serializable {
	private Stream stream;
	private List<Student> lessonStudents;
	private List<Student> filteredLessonStudents;

	private void initLessonStudents() {
		Set<Student> studentSet = new HashSet<>();
		if (stream != null) {
			for (Group group : stream.getGroups()) {
				studentSet.addAll(group.getStudents());
			}
		}
		lessonStudents = new ArrayList<>(studentSet);
	}


	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
		initLessonStudents();
	}

	public List<Student> getLessonStudents() {
		return lessonStudents;
	}

	public void setLessonStudents(List<Student> lessonStudents) {
		this.lessonStudents = lessonStudents;
	}

	public List<Student> getFilteredLessonStudents() {
		return filteredLessonStudents;
	}

	public void setFilteredLessonStudents(List<Student> filteredLessonStudents) {
		this.filteredLessonStudents = filteredLessonStudents;
	}
}
