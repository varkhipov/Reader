package com.grsu.reader.dao;

import com.grsu.reader.models.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class ScheduleDAO {
	public static Schedule getScheduleById(Connection connection, int id) {
		Schedule lecture = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Schedule WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lecture = new Schedule();
				lecture.setId(resultSet.getInt("id"));
				lecture.setBegin(LocalTime.parse(resultSet.getString("begin")));
				lecture.setEnd(LocalTime.parse(resultSet.getString("end")));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getScheduleById() -->" + e.getMessage());
			return null;
		}
		return lecture;
	}
}
