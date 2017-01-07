package com.grsu.reader.beans;

import com.grsu.reader.models.Lecture;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

	private boolean connected;
	private String activeView = "lectures";
	private Lecture currentLecture;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getActiveView() {
		return activeView;
	}

	public void setActiveView(String activeView) {
		this.activeView = activeView;
	}

	public Lecture getCurrentLecture() {
		return currentLecture;
	}

	public void setCurrentLecture(Lecture currentLecture) {
		this.currentLecture = currentLecture;
	}
}
