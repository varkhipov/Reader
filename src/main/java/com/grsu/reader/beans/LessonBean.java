package com.grsu.reader.beans;

import com.grsu.reader.dao.*;
import com.grsu.reader.models.Class;
import com.grsu.reader.models.Lesson;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "lessonBean")
@ViewScoped
public class LessonBean implements Serializable {

	private Lesson selectedLesson;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Lesson getSelectedLesson() {
		return selectedLesson;
	}

	public void setSelectedLesson(Lesson selectedLesson) {
		this.selectedLesson = selectedLesson;
	}

	public List<Lesson> getLessons() {
		return LessonDAO.getLessons(databaseBean.getConnection());
	}

	public List<Student> getLessonStudents() {
		if (sessionBean.getCurrentLesson() == null) {
			return Collections.emptyList();
		}
		List<Class> classes = LessonClassDAO.getClassesByLessonId(
				databaseBean.getConnection(),
				sessionBean.getCurrentLesson().getId()
		);

		List<Student> students = new ArrayList<>();
		for (Class cls : classes) {
			students.addAll(StudentClassDAO.getStudentsByClassId(
					databaseBean.getConnection(),
					cls.getId())
			);
		}
		return students;
	}
}
