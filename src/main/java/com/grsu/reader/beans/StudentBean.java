package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Student;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
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

	private String url;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
		copyOfSelectedStudent = this.selectedStudent == null ? null : new Student(selectedStudent);
	}

	public DualListModel<Group> getSelectedGroups() {
		if (selectedStudent == null) {
			setSelectedGroups(new DualListModel<>(sessionBean.getGroups(), Collections.emptyList()));
		} else if (selectedGroups == null) {
			List<Group> sourceGroups = new ArrayList<>(sessionBean.getGroups());
			if (selectedStudent.getGroups() != null) {
				sourceGroups.removeAll(selectedStudent.getGroups());
				setSelectedGroups(new DualListModel<>(sourceGroups, selectedStudent.getGroups()));
			} else {
				setSelectedGroups(new DualListModel<>(sourceGroups, Collections.emptyList()));
			}

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
		EntityDAO.save(selectedStudent);
		sessionBean.updateStudents();
		closeDialog("studentDialog");
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		EntityDAO.delete(selectedStudent);
		if (filteredStudents != null) {
			filteredStudents.clear();
		}
		sessionBean.updateStudents();
		execute("PF('studentsTable').clearFilters()");
		exit();
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
