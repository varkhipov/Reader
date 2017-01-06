package com.grsu.reader.beans;

import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "studentBean")
@SessionScoped
public class StudentBean implements Serializable {

	private Student selectedStudent;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public List<Student> getStudents() {
		return StudentDAO.getStudents(databaseBean.getConnection());
	}
}
