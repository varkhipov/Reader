package com.grsu.reader.utils;

import java.sql.*;

import static com.grsu.reader.utils.FileUtils.*;
import static com.grsu.reader.utils.PropertyUtils.*;

public class DBUtils {
	/**
	 * Loads jdbc driver and creates sql connection to database.
	 * Driver and database properties are specified in configuration file.
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName(getProperty("db.driver"));

		StringBuilder sbConnection = new StringBuilder("jdbc:");
		sbConnection.append(getProperty("db.protocol"));
		sbConnection.append(":");
		sbConnection.append(buildPath(APP_FILES_PATH, "database", getProperty("db.name")));

		return DriverManager.getConnection(sbConnection.toString());
	}

	/**
	 * Closes current database connection
	 *
	 * TODO: probably need to deregister JDBC driver
	 * http://stackoverflow.com/a/5315467
	 *
	 * @param connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void closeDB(Connection connection) throws ClassNotFoundException, SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	/**
	 * http://stackoverflow.com/a/3273378
	 *
	 * @param preparedStatement
	 * @param values
	 * @throws SQLException
	 */
	private static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
		for (int i = 0; i < values.length; i++) {
			preparedStatement.setObject(i + 1, values[i]);
		}
	}

	/**
	 * Builds instance of PreparedStatement with provided params
	 *
	 * @param connection
	 * @param sql
	 * @param values
	 * @return PreparedStatement with specified values
	 * @throws SQLException
	 */
	public static PreparedStatement buildPreparedStatement(Connection connection, String sql, Object... values) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		setValues(preparedStatement, values);
		return preparedStatement;
	}

	/**
	 * Return last inserted row Id. Note: function works only for current connection.
	 *
	 * http://www.sqlite.org/lang_corefunc.html#last_insert_rowid
	 * "The last_insert_rowid() function returns the ROWID of the last row insert
	 * from the database connection which invoked the function."
	 *
	 * @param connection
	 * @return
	 */
	public static int getLastInsertRowId(Connection connection) {
		int id = 0;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT last_insert_rowid();"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				id = resultSet.getInt(1);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
}
