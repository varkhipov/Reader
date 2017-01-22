package com.grsu.reader.beans;

import com.grsu.reader.dao.StreamDAO;
import com.grsu.reader.models.Group;
import com.grsu.reader.models.Stream;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.closeDialog;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "streamBean")
@ViewScoped
public class StreamBean implements Serializable {

	private Stream selectedStream;
	private Stream copyOfSelectedStream;

	private DualListModel<Group> selectedGroups;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStream(Stream selectedStream) {
		this.selectedStream = selectedStream;
		copyOfSelectedStream = this.selectedStream == null ? null : new Stream(selectedStream);
	}

	public DualListModel<Group> getSelectedGroups() {
		if (selectedStream == null) {
			setSelectedGroups(new DualListModel<>(
					sessionBean.getGroups(),
					Collections.emptyList()
			));
		} else if (selectedGroups == null) {
			List<Group> sourceGroups = new ArrayList<>(sessionBean.getGroups());
			sourceGroups.removeAll(selectedStream.getGroups());

			setSelectedGroups(new DualListModel<>(
					sourceGroups,
					selectedStream.getGroups()
			));
		}
		return selectedGroups;
	}

	public void setSelectedGroups(DualListModel<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
		if (selectedStream != null) {
			selectedStream.setGroups(selectedGroups == null ? null : selectedGroups.getTarget());
		}
	}

	public boolean isInfoChanged() {
		return selectedStream != null && !selectedStream.equals(copyOfSelectedStream);
	}

	public List<Stream> getStreams() {
		return sessionBean.getStreams();
	}

	public void exit() {
		setSelectedStream(null);
		setSelectedGroups(null);
		closeDialog("streamDialog");
	}

	public void save() {
		if (selectedStream.getId() == 0) {
			StreamDAO.create(databaseBean.getConnection(), selectedStream);
		} else {
			StreamDAO.update(databaseBean.getConnection(), selectedStream);
		}
		sessionBean.updateStreams();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		StreamDAO.delete(databaseBean.getConnection(), selectedStream);
		sessionBean.updateStreams();
		update("views");
		exit();
	}

	public void setDatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Stream getSelectedStream() {
		return selectedStream;
	}

	public Stream getCopyOfSelectedStream() {
		return copyOfSelectedStream;
	}

}
