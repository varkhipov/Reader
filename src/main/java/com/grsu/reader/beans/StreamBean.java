package com.grsu.reader.beans;

import com.grsu.reader.dao.StreamDAO;
import com.grsu.reader.models.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.reader.utils.FacesUtils.execute;
import static com.grsu.reader.utils.FacesUtils.update;

@ManagedBean(name = "streamBean")
@ViewScoped
public class StreamBean implements Serializable {

	private Stream selectedStream;
	private Stream copyOfSelectedStream;

	@ManagedProperty(value = "#{databaseBean}")
	private DatabaseBean databaseBean;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStream(Stream selectedStream) {
		this.selectedStream = selectedStream;
		copyOfSelectedStream = this.selectedStream == null ? null : new Stream(selectedStream);
	}

	public boolean isInfoChanged() {
		return selectedStream != null && !selectedStream.equals(copyOfSelectedStream);
	}

	public List<Stream> getStreams() {
		return sessionBean.getStreams();
	}

	public void closeDialog() {
		execute("PF('streamDialog').hide();");
	}

	public void exit() {
		setSelectedStream(null);
		closeDialog();
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
		closeDialog();
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
