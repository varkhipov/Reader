package com.grsu.reader.beans;

import com.grsu.reader.utils.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import java.io.Serializable;
import java.sql.SQLException;

@ManagedBean(name = "menuBean")
@ViewScoped
public class MenuBean implements Serializable {

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

/*
	public void save() {
		addMessage("Success", "Data saved");
	}

	public void update() {
		addMessage("Success", "Data updated");
	}

	public void delete() {
		addMessage("Success", "Data deleted");
	}
*/
	public void quit() {
		FacesUtils.addMessage("Quit", "Some quit action");
	}

	public void connect() {
		try {
			databaseBean.connect();
			if (!databaseBean.isConnected()) {
				System.out.println("No connection to db.");
			} else {
				sessionBean.setConnected(true);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		databaseBean.disconnect();
		if (databaseBean.isConnected()) {
			System.out.println("Still not disconnected.");
		} else {
			sessionBean.setConnected(false);
		}
	}

	public void changeView(String viewName) {
		sessionBean.setActiveView(viewName);
	}

	public void addStudent() {
		//
	}
}
