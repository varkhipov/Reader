package com.grsu.reader.dao;

import com.grsu.reader.models.Lecturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LecturerDAO {
	public static Lecturer getLecturerById(Connection connection, int id) {
		Lecturer lecture = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Lecturer WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lecture = new Lecturer();
				lecture.setId(resultSet.getInt("id"));
				lecture.setName(resultSet.getString("name"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLecturerById() -->" + e.getMessage());
			return null;
		}
		return lecture;
	}
}
