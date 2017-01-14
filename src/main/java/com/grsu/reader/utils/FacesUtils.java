package com.grsu.reader.utils;

import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtils {
	public static void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addMessage(String id, String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(id, message);
	}

	public static void update(String clientId) {
		RequestContext.getCurrentInstance().update(clientId);
	}

	public static void execute(String javascript) {
		RequestContext.getCurrentInstance().execute(javascript);
	}
}
