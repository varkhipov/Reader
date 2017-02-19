package com.grsu.reader.beans;

import com.grsu.reader.entities.Student;
import com.grsu.reader.utils.EntityUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by pavel on 2/19/17.
 */
@ManagedBean
@SessionScoped
public class ImageBean {
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent(context.getExternalContext()
					.getResourceAsStream("/resources/images/noavatar.png"), "image/png");
		} else {
			String studentId = context.getExternalContext().getRequestParameterMap().get("studentId");
			Student student = EntityUtils.getEntityById(sessionBean.getStudents(), Integer.valueOf(studentId));
			if (student == null || student.getImage() == null) {
				return new DefaultStreamedContent(context.getExternalContext()
						.getResourceAsStream("/resources/images/noavatar.png"), "image/png");
			}
			return new DefaultStreamedContent(new ByteArrayInputStream(student.getImage()));
		}
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
}
