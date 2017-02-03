package com.grsu.reader.dao;

import com.grsu.reader.models.Lecturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LecturerDAO {
	private static Lecturer mapFromResultSet(ResultSet resultSet) throws SQLException {
		Lecturer lecturer = new Lecturer();
		lecturer.setId(resultSet.getInt("id"));
		lecturer.setUid(resultSet.getString("uid"));
		lecturer.setParsedUid(resultSet.getInt("parsed_uid"));
		lecturer.setFirstName(resultSet.getString("first_name"));
		lecturer.setLastName(resultSet.getString("last_name"));
		lecturer.setPatronymic(resultSet.getString("patronymic"));
		lecturer.setPhone(resultSet.getString("phone"));
		lecturer.setEmail(resultSet.getString("email"));
		// TODO: add photo
		return lecturer;
	}
	
	public static Lecturer getLecturerById(Connection connection, int id) {
		Lecturer lecturer = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, uid, parsed_uid, first_name, last_name, patronymic, phone, email FROM LECTURER WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lecturer = mapFromResultSet(resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLecturerById() -->" + e.getMessage());
			return lecturer;
		}
		return lecturer;
	}


	// TODO:Implement full CRUD
}
