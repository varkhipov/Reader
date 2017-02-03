package com.grsu.reader.dao;

import com.grsu.reader.models.Group;
import com.grsu.reader.models.LessonType;
import com.grsu.reader.models.LessonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LessonTypeDAO {

	private static LessonType mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		LessonType lessonType = new LessonType();
		lessonType.setId(resultSet.getInt("id"));
		lessonType.setName(resultSet.getString("name"));
		return lessonType;
	}

	public static List<LessonType> getLessonTypes(Connection connection) {
		List<LessonType> lessonTypes = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name FROM LESSON_TYPE;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lessonTypes.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLessonTypes() -->" + e.getMessage());
			return lessonTypes;
		}
		return lessonTypes;
	}

	public static LessonType getLessonTypeById(Connection connection, int id) {
		LessonType lessonTypes = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name FROM LESSON_TYPE WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lessonTypes = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLessonTypeById() -->" + e.getMessage());
			return lessonTypes;
		}
		return lessonTypes;
	}

	public static void create(Connection connection, LessonType lessonType) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO LESSON_TYPE (name) VALUES (?);",
					lessonType.getName()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, LessonType lessonType) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE LESSON_TYPE SET name = ? WHERE id = ?;",
					lessonType.getName(),
					lessonType.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, LessonType lessonType) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM LESSON_TYPE WHERE id = ?;",
					lessonType.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
