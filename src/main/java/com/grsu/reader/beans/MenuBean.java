package com.grsu.reader.beans;

import com.grsu.reader.utils.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = "menuBean")
@ViewScoped
public class MenuBean implements Serializable {

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	private boolean showMenu = true;

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

	public void quit() {
		FacesUtils.addInfo("Quit", "Some quit action");
	}
*/

	public void connect() {
		sessionBean.connect();
	}

	public void disconnect() {
		sessionBean.disconnect();
	}

	public void changeView(String viewName) {
		sessionBean.setActiveView(viewName);
	}

	public void showMenu() {
		setShowMenu(true);
	}

	public void hideMenu() {
		setShowMenu(false);
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
}
