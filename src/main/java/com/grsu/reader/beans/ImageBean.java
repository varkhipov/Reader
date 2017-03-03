package com.grsu.reader.beans;

import com.grsu.reader.utils.FileUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by pavel on 2/19/17.
 */
@ManagedBean
@SessionScoped
public class ImageBean implements Serializable {

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent(context.getExternalContext()
					.getResourceAsStream("/resources/images/noavatar.png"), "image/png");
		} else {
			String cardUid = context.getExternalContext().getRequestParameterMap().get("cardUid");
			File photo = FileUtils.getFile(FileUtils.STUDENTS_PHOTO_FOLDER_PATH, cardUid, FileUtils.STUDENTS_PHOTO_EXTENSION);
			if (!photo.exists()) {
				return new DefaultStreamedContent(context.getExternalContext()
						.getResourceAsStream("/resources/images/noavatar.png"), "image/png");
			} else {
				return new DefaultStreamedContent(new FileInputStream(photo));
			}
		}
	}
}
