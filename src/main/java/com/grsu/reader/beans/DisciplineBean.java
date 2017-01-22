package com.grsu.reader.beans;

import com.grsu.reader.dao.DisciplineDAO;
import com.grsu.reader.models.Discipline;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "disciplineBean")
@ViewScoped
public class DisciplineBean implements Serializable {

	private Discipline selectedDiscipline;
	private Discipline copyOfSelectedDiscipline;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedDiscipline(Discipline selectedDiscipline) {
		this.selectedDiscipline = selectedDiscipline;
		copyOfSelectedDiscipline = this.selectedDiscipline == null ? null : new Discipline(selectedDiscipline);
	}

	public boolean isInfoChanged() {
		return selectedDiscipline != null && !selectedDiscipline.equals(copyOfSelectedDiscipline);
	}

	public List<Discipline> getDisciplines() {
		return sessionBean.getDisciplines();
	}

	public void exit() {
		setSelectedDiscipline(null);
		closeDialog("disciplineDialog");
	}

	public void save() {
		if (selectedDiscipline.getId() == 0) {
			DisciplineDAO.create(databaseBean.getConnection(), selectedDiscipline);
		} else {
			DisciplineDAO.update(databaseBean.getConnection(), selectedDiscipline);
		}
		sessionBean.updateDisciplines();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		DisciplineDAO.delete(databaseBean.getConnection(), selectedDiscipline);
		sessionBean.updateDisciplines();
		update("views");
		exit();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Discipline getSelectedDiscipline() {
		return selectedDiscipline;
	}

	public Discipline getCopyOfSelectedDiscipline() {
		return copyOfSelectedDiscipline;
	}

}
