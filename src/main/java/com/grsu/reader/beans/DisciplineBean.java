package com.grsu.reader.beans;

import com.grsu.reader.dao.CourseDAO;
import com.grsu.reader.models.Course;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "disciplineBean")
@ViewScoped
public class DisciplineBean implements Serializable {

	private Course selectedCourse;
	private Course copyOfSelectedCourse;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
		copyOfSelectedCourse = this.selectedCourse == null ? null : new Course(selectedCourse);
	}

	public boolean isInfoChanged() {
		return selectedCourse != null && !selectedCourse.equals(copyOfSelectedCourse);
	}

	public List<Course> getCourses() {
		return sessionBean.getCourses();
	}

	public void exit() {
		setSelectedCourse(null);
		closeDialog("disciplineDialog");
	}

	public void save() {
		if (selectedCourse.getId() == 0) {
			CourseDAO.create(databaseBean.getConnection(), selectedCourse);
		} else {
			CourseDAO.update(databaseBean.getConnection(), selectedCourse);
		}
		sessionBean.updateDisciplines();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		CourseDAO.delete(databaseBean.getConnection(), selectedCourse);
		sessionBean.updateDisciplines();
		update("views");
		exit();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Course getSelectedCourse() {
		return selectedCourse;
	}

	public Course getCopyOfSelectedCourse() {
		return copyOfSelectedCourse;
	}

}
