package com.grsu.reader.beans;

import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Student;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.execute;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean implements Serializable {

	private Student selectedStudent;
	private Student copyOfSelectedStudent; // used to detect changes

	private DualListModel<Group> selectedGroups;
	private List<Student> filteredStudents;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
		copyOfSelectedStudent = this.selectedStudent == null ? null : new Student(selectedStudent);
	}

	public DualListModel<Group> getSelectedGroups() {
		if (selectedStudent == null) {
			setSelectedGroups(new DualListModel<>(
					sessionBean.getGroups(),
					Collections.emptyList()
			));
		} else if (selectedGroups == null) {
			List<Group> sourceGroups = new ArrayList<>(sessionBean.getGroups());
			sourceGroups.removeAll(selectedStudent.getGroups());

			setSelectedGroups(new DualListModel<>(
					sourceGroups,
					selectedStudent.getGroups()
			));
		}
		return selectedGroups;
	}

	public void setSelectedGroups(DualListModel<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
		if (selectedStudent != null) {
			selectedStudent.setGroups(selectedGroups == null ? null : selectedGroups.getTarget());
		}
	}

	public boolean isInfoChanged() {
		return selectedStudent != null && !selectedStudent.equals(copyOfSelectedStudent);
	}

	public List<Student> getStudents() {
		return sessionBean.getStudents();
	}

	public void exit() {
		setSelectedStudent(null);
		setSelectedGroups(null);
		closeDialog("studentDialog");
	}

	public void save() {
		if (selectedStudent.getId() == 0) {
			StudentDAO.create(databaseBean.getConnection(), selectedStudent);
		} else {
			StudentDAO.update(databaseBean.getConnection(), selectedStudent);
		}
		sessionBean.updateStudents();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		StudentDAO.delete(databaseBean.getConnection(), selectedStudent);
		if (filteredStudents != null) {
			filteredStudents.clear();
		}
		sessionBean.updateStudents();
		execute("PF('studentsTable').clearFilters()");
		exit();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public Student getCopyOfSelectedStudent() {
		return copyOfSelectedStudent;
	}

	public List<Student> getFilteredStudents() {
		return filteredStudents;
	}

	public void setFilteredStudents(List<Student> filteredStudents) {
		this.filteredStudents = filteredStudents;
	}
}
