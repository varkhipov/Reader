package com.grsu.reader.beans;

import com.grsu.reader.utils.DBUtils;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import java.io.Serializable;
import java.sql.SQLException;

import static com.grsu.reader.utils.FacesUtils.addMessage;

@ManagedBean(name = "menuBean")
@SessionScoped
public class MenuBean implements Serializable {

	private boolean connected;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public boolean isConnected() {
		return connected;
	}

	public void save() {
		addMessage("Success", "Data saved");
	}

	public void update() {
		addMessage("Success", "Data updated");
	}

	public void delete() {
		addMessage("Success", "Data deleted");
	}

	public void quit() {
		addMessage("Quit", "Some quit action");
//		System.out.println("quit action runned");
	}

	public void connect() {
		try {
			databaseBean.connect();
			if (!databaseBean.isConnected()) {
				System.out.println("No connection to db.");
			} else {
				connected = true;
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
			connected = false;
		}
	}

	public void addStudent() {
		//
	}
}
