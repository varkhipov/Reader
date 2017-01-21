package com.grsu.reader.beans;

import com.grsu.reader.dao.FacultyDAO;
import com.grsu.reader.models.Faculty;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.execute;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "facultyBean")
@ViewScoped
public class FacultyBean implements Serializable {

	private Faculty selectedFaculty;
	private Faculty copyOfSelectedFaculty;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedFaculty(Faculty selectedFaculty) {
		this.selectedFaculty = selectedFaculty;
		copyOfSelectedFaculty = this.selectedFaculty == null ? null : new Faculty(selectedFaculty);
	}

	public boolean isInfoChanged() {
		return selectedFaculty != null && !selectedFaculty.equals(copyOfSelectedFaculty);
	}

	public List<Faculty> getFaculties() {
		return sessionBean.getFaculties();
	}

	public void closeDialog() {
		execute("PF('facultyDialog').hide();");
	}

	public void exit() {
		setSelectedFaculty(null);
		closeDialog();
	}

	public void save() {
		if (selectedFaculty.getId() == 0) {
			FacultyDAO.create(databaseBean.getConnection(), selectedFaculty);
		} else {
			FacultyDAO.update(databaseBean.getConnection(), selectedFaculty);
		}
		sessionBean.updateFaculties();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		FacultyDAO.delete(databaseBean.getConnection(), selectedFaculty);
		sessionBean.updateFaculties();
		update("views");
		closeDialog();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Faculty getSelectedFaculty() {
		return selectedFaculty;
	}

	public Faculty getCopyOfSelectedFaculty() {
		return copyOfSelectedFaculty;
	}

}
