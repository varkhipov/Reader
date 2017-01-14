package com.grsu.reader.beans;

import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.execute;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean implements Serializable {

	private Student selectedStudent;
	private Student copyOfSelectedStudent; // used to detect changes

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public Student getCopyOfSelectedStudent() {
		return copyOfSelectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
		copyOfSelectedStudent = this.selectedStudent == null ? null : new Student(selectedStudent);
	}

	public boolean isInfoChanged() {
		return selectedStudent != null && !selectedStudent.equals(copyOfSelectedStudent);
	}

	public List<Student> getStudents() {
		return StudentDAO.getStudents(databaseBean.getConnection());
	}

	public void closeDialog() {
		execute("PF('studentDialog').hide();");
	}

	public void exit() {
		setSelectedStudent(null);
		closeDialog();
	}

	public void save() {
		if (selectedStudent.getId() == 0) {
			StudentDAO.addStudent(databaseBean.getConnection(), selectedStudent);
		} else {
			StudentDAO.updateStudent(databaseBean.getConnection(), selectedStudent);
		}
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		StudentDAO.deleteStudent(databaseBean.getConnection(), selectedStudent);
		update("views");
		closeDialog();
	}

}
