package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.*;
import com.grsu.reader.entities.Class;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

/**
 * Created by pavel on 3/10/17.
 */
@ManagedBean(name = "newLessonBean")
@ViewScoped
public class NewLessonBean implements Serializable {
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	private Lesson lesson;

	private void initLesson() {
		if (lesson.getClasses() == null) {
			Class cl = new Class();
			cl.setDate(LocalDateTime.now());
			cl.setLesson(lesson);
			lesson.setClasses(Arrays.asList(cl));
		}
	}

	public void exit() {
		sessionBean.setActiveView("lessons");
		clear();
		update("views");
		closeDialog("lessonDialog");
	}

	private void clear() {
		lesson = null;
	}

	public void createLesson() {
		if (lesson != null) {
			if (lesson.getType() == null || lesson.getType().getId() == 1) {
				lesson.setGroup(null);
			}

			EntityDAO.add(lesson);
			EntityDAO.add(lesson.getClasses().get(0));


			List<Group> groups;

			if (lesson.getGroup() == null) {
				groups = lesson.getStream().getGroups();
			} else {
				groups = Arrays.asList(lesson.getGroup());
			}

			Set<Student> students = new HashSet<>();
			for (Group group : groups) {
				students.addAll(group.getStudents());
			}

			List<StudentClass> studentClasses = new ArrayList<>();
			for (Student student : students) {
				StudentClass sc = new StudentClass();
				sc.setStudent(student);
				sc.setClazz(lesson.getClasses().get(0));
				studentClasses.add(sc);
			}
			EntityDAO.add(new ArrayList<>(studentClasses));

			sessionBean.updateLessons();
		}
		exit();
	}

	/*GETTERS AND SETTERS*/
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
		initLesson();
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
