package com.grsu.reader.beans;

import com.grsu.reader.dao.GroupDAO;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Student;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.utils.EntityUtils.getEntityById;
import static com.grsu.reader.utils.FacesUtils.execute;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "groupBean")
@ViewScoped
public class GroupBean implements Serializable {

	private Group selectedGroup;
	private Group copyOfSelectedGroup;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
		copyOfSelectedGroup = this.selectedGroup == null ? null : new Group(selectedGroup);
	}

	public int getSelectedFacultyId() {
		if (selectedGroup.getFaculty() == null) {
			return 0;
		}
		return selectedGroup.getFaculty().getId();
	}

	public void setSelectedFacultyId(int selectedFacultyId) {
		selectedGroup.setFaculty(getEntityById(sessionBean.getFaculties(), selectedFacultyId));
	}

	public boolean isInfoChanged() {
		return selectedGroup != null && !selectedGroup.equals(copyOfSelectedGroup);
	}

	public List<Group> getGroups() {
		return sessionBean.getGroups();
	}

	public List<SelectItem> getFacultiesItems() {
		return sessionBean.getFacultiesItems();
	}

	public List<Student> getGroupStudents() { return Collections.emptyList(); }

	public void closeDialog() {
		execute("PF('groupDialog').hide();");
	}

	public void exit() {
		setSelectedGroup(null);
		closeDialog();
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
		exit();
	}

	public void delete() {
		GroupDAO.delete(databaseBean.getConnection(), selectedGroup);
		sessionBean.updateGroups();
		update("views");
		closeDialog();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public Group getCopyOfSelectedGroup() {
		return copyOfSelectedGroup;
	}

}
