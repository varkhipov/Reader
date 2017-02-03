package com.grsu.reader.beans;

import com.grsu.reader.dao.GroupDAO;
import com.grsu.reader.dao.StudentGroupDAO;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.utils.EntityUtils.getEntityById;
import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "groupBean")
@ViewScoped
public class GroupBean implements Serializable {

	private Group selectedGroup;
	private Group copyOfSelectedGroup;

	private List<Student> groupStudents;
	private List<Student> filteredGroupStudents;

	private String dialogAction;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public boolean isInfoChanged() {
		return selectedGroup != null && !selectedGroup.equals(copyOfSelectedGroup);
	}

	/*
		GROUPS
	*/
	public void exitInfo() {
		setSelectedGroup(null);
		setDialogAction(null);
		closeDialog("groupInfoDialog");
	}

	public void save() {
		if (selectedGroup.getId() == 0) {
			GroupDAO.create(databaseBean.getConnection(), selectedGroup);
		} else {
			GroupDAO.update(databaseBean.getConnection(), selectedGroup);
		}
		sessionBean.updateGroups();
		update("views");
	}

	public void saveAndExit() {
		save();
		exitInfo();
	}

	public void deleteGroup() {
		GroupDAO.delete(databaseBean.getConnection(), selectedGroup.getId());
		sessionBean.updateStudents();
		sessionBean.updateGroups();
		update("views");
		exitInfo();
	}

	/*
		STUDENTS
	*/
	public void exitStudents() {
		setSelectedGroup(null);
		setDialogAction(null);
		setFilteredGroupStudents(null);
		sessionBean.updateStudents();
		closeDialog("groupStudentsDialog");
	}

	public void addStudent(Student student) {
		StudentGroupDAO.create(databaseBean.getConnection(), student.getId(), selectedGroup.getId());
		groupStudents.remove(student);
		if (filteredGroupStudents != null) {
			filteredGroupStudents.remove(student);
		}
	}

	public void deleteStudent(Student student) {
		StudentGroupDAO.delete(databaseBean.getConnection(), student, selectedGroup);
		groupStudents.remove(student);
		if (filteredGroupStudents != null) {
			filteredGroupStudents.remove(student);
		}
	}

	/*
		GETTERS & SETTERS
	 */
	public List<Group> getGroups() {
		return sessionBean.getGroups();
	}

	public List<SelectItem> getFacultiesItems() {
		return sessionBean.getFacultiesItems();
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
		copyOfSelectedGroup = selectedGroup == null ? null : new Group(selectedGroup);

		if (selectedGroup != null) {
			if (selectedGroup.getId() == 0) {
				groupStudents = Collections.emptyList();
			} else {
				if ("add".equals(dialogAction)) {
					List<Student> allStudents = new ArrayList<>(sessionBean.getStudents());
					List<Student> studentsFromGroup = new ArrayList<>(
							StudentGroupDAO.getStudentsByGroupId(
									databaseBean.getConnection(),
									selectedGroup.getId()
							)
					);
					allStudents.removeAll(studentsFromGroup);
					groupStudents = allStudents;
				} else if ("delete".equals(dialogAction)) {
					groupStudents = StudentGroupDAO.getStudentsByGroupId(
							databaseBean.getConnection(),
							selectedGroup.getId()
					);
				}
			}
		}
	}

	public int getSelectedDepartmentId() {
		if (selectedGroup.getDepartment() == null) {
			return 0;
		}
		return selectedGroup.getDepartment().getId();
	}

	public void setSelectedFacultyId(int selectedFacultyId) {
		selectedGroup.setDepartment(getEntityById(sessionBean.getFaculties(), selectedFacultyId));
	}

	public List<Student> getGroupStudents() {
		return groupStudents;
	}

	public void setGroupStudents(List<Student> groupStudents) {
		this.groupStudents = groupStudents;
	}

	public List<Student> getFilteredGroupStudents() {
		return filteredGroupStudents;
	}

	public void setFilteredGroupStudents(List<Student> filteredGroupStudents) {
		this.filteredGroupStudents = filteredGroupStudents;
	}

	public String getDialogAction() {
		return dialogAction;
	}

	public void setDialogAction(String dialogAction) {
		this.dialogAction = dialogAction;
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
