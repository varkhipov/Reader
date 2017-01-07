package com.grsu.reader.beans;

import com.grsu.reader.utils.DBUtils;

import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

@ManagedBean(name = "databaseBean")
@ApplicationScoped
public class DatabaseBean implements Serializable {

	private boolean connected;
	private Connection connection;

	public boolean isConnected() {
		return connected;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	//@PostConstruct
	public void connect() throws SQLException, ClassNotFoundException {
		connection = DBUtils.connect();
		if (connection != null) {
			connected = true;
		}
	}

	@PreDestroy
	public void disconnect() {
		try {
			DBUtils.closeDB(connection);
			connected = false;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
