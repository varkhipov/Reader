package com.grsu.reader.dao;

import com.grsu.reader.models.Faculty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class FacultyDAO {
	public static List<Faculty> getFaculties(Connection connection) {
		List<Faculty> faculties = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Faculty;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				faculties.add(new Faculty(
						resultSet.getInt("id"),
						resultSet.getString("name")
				));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getFaculties() -->" + e.getMessage());
			return faculties;
		}
		return faculties;
	}
	
	public static Faculty getFacultyById(Connection connection, int id) {
		Faculty faculty = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Faculty WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				faculty = new Faculty(resultSet.getInt("id"), resultSet.getString("name"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getFacultyById() -->" + e.getMessage());
			return faculty;
		}
		return faculty;
	}

	public static void create(Connection connection, Faculty faculty) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Faculty (name) VALUES (?);",
					faculty.getName()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Faculty faculty) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Faculty SET name = ? WHERE id = ?;",
					faculty.getName(),
					faculty.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Faculty faculty) {
		GroupDAO.deleteByFacultyId(connection, faculty.getId()); // TODO: delete or update to null?

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Faculty WHERE id = ?;",
					faculty.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
