package com.grsu.reader.beans;

import com.grsu.reader.dao.DepartmentDAO;
import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.Department;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "departmentBean")
@ViewScoped
public class DepartmentBean implements Serializable {

	private Department selectedDepartment;
	private Department copyOfSelectedDepartment;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		copyOfSelectedDepartment = this.selectedDepartment == null ? null : new Department(selectedDepartment);
	}

	public boolean isInfoChanged() {
		return selectedDepartment != null && !selectedDepartment.equals(copyOfSelectedDepartment);
	}

	public void exit() {
		setSelectedDepartment(null);
		closeDialog("departmentDialog");
	}

	public void save() {
		/*if (selectedDepartment.getId() == 0) {
			DepartmentDAO.create(databaseBean.getConnection(), selectedDepartment);
		} else {
			DepartmentDAO.update(databaseBean.getConnection(), selectedDepartment);
		}*/
		sessionBean.updateDepartments();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		new EntityDAO().delete(selectedDepartment);
		sessionBean.updateDepartments();
		update("views");
		exit();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public Department getCopyOfSelectedDepartment() {
		return copyOfSelectedDepartment;
	}

}
