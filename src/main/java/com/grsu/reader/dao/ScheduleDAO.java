package com.grsu.reader.dao;

import com.grsu.reader.models.Schedule;
import com.grsu.reader.models.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class ScheduleDAO {
	public static List<Schedule> getSchedules(Connection connection) {
		List<Schedule> schedules = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Schedule;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				schedules.add(new Schedule(
						resultSet.getInt("id"),
						LocalTime.parse(resultSet.getString("begin")),
						LocalTime.parse(resultSet.getString("end")),
						resultSet.getString("number")
				));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getSchedules() -->" + e.getMessage());
			return schedules;
		}
		return schedules;
	}
	
	public static Schedule getScheduleById(Connection connection, int id) {
		Schedule schedule = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Schedule WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				schedule = new Schedule(
						resultSet.getInt("id"),
						LocalTime.parse(resultSet.getString("begin")),
						LocalTime.parse(resultSet.getString("end")),
						resultSet.getString("number")
				);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getScheduleById() -->" + e.getMessage());
			return schedule;
		}
		return schedule;
	}

	public static void create(Connection connection, Schedule schedule) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Schedule (begin, end, number) VALUES (?, ?, ?);",
					schedule.getBegin(),
					schedule.getEnd(),
					schedule.getNumber()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Schedule schedule) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Schedule " +
							"SET " +
								"begin = ?, " +
								"end = ?, " +
								"number = ? " +
							"WHERE id = ?;",
					schedule.getBegin(),
					schedule.getEnd(),
					schedule.getNumber(),
					schedule.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Schedule schedule) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Schedule WHERE id = ?;",
					schedule.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
