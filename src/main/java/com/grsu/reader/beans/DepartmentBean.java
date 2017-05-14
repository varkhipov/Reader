package com.grsu.reader.beans;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.entities.Department;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "departmentBean")
@ViewScoped
public class DepartmentBean implements Serializable {

	private Department selectedDepartment;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public void exit() {
		setSelectedDepartment(null);
		closeDialog("departmentDialog");
	}

	public void save() {
		EntityDAO.save(selectedDepartment);
		sessionBean.updateDepartments();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}


}
