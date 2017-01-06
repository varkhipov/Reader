package com.grsu.reader.dao;

import com.grsu.reader.models.Discipline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class DisciplineDAO {
	public static Discipline getDisciplineById(Connection connection, int id) {
		Discipline lecture = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Discipline WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lecture = new Discipline();
				lecture.setId(resultSet.getInt("id"));
				lecture.setName(resultSet.getString("name"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDisciplineById() -->" + e.getMessage());
			return null;
		}
		return lecture;
	}
}
