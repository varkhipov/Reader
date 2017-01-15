package com.grsu.reader.dao;

import com.grsu.reader.models.Class;
import com.grsu.reader.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class ClassDAO {
	public static Class getClassById(Connection connection, int id) {
		Class cls = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Class WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				cls = new Class(
						resultSet.getInt("id"),
						LocalDate.parse(resultSet.getString("date")),
						ScheduleDAO.getScheduleById(connection, resultSet.getInt("scheduleId"))
				);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClassById() -->" + e.getMessage());
			return cls;
		}
		return cls;
	}

	public static void create(Connection connection, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Class (date, scheduleId) VALUES (?, ?);",
					cls.getDate(),
					cls.getSchedule() != null && cls.getSchedule().getId() != 0
							? cls.getSchedule().getId()
							: null
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Class SET " +
								"date = ?, " +
								"scheduleId = ? " +
							"WHERE id = ?;",
					cls.getDate(),
					cls.getSchedule() != null && cls.getSchedule().getId() != 0
							? cls.getSchedule().getId()
							: null,

					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Class WHERE id = ?;",
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
