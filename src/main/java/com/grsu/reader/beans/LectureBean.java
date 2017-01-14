package com.grsu.reader.beans;

import com.grsu.reader.dao.LectureDAO;
import com.grsu.reader.dao.LecturerStudentDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.models.Lecture;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "lectureBean")
@ViewScoped
public class LectureBean implements Serializable {

	private Lecture selectedLecture;

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

	public Lecture getSelectedLecture() {
		return selectedLecture;
	}

	public void setSelectedLecture(Lecture selectedLecture) {
		this.selectedLecture = selectedLecture;
	}

	public List<Lecture> getLectures() {
		return LectureDAO.getLectures(databaseBean.getConnection());
	}

	public List<Student> getLectureStudents() {
		if (sessionBean.getCurrentLecture() == null) {
			return Collections.emptyList();
		}
		return LecturerStudentDAO.getStudentsByLectureId(databaseBean.getConnection(), sessionBean.getCurrentLecture().getId());
	}
}
