package com.grsu.reader.dao;

import com.grsu.reader.models.Course;
import com.grsu.reader.models.Student;
import com.grsu.reader.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class CourseDAO {
	private static Course mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Course course = new Course();
		course.setId(resultSet.getInt("id"));
		course.setName(resultSet.getString("name"));
		course.setDescription(resultSet.getString("description"));

		course.setStream(StreamDAO.getStreamById(connection, resultSet.getInt("stream_id")));
		return course;
	}

	public static List<Course> getCourses(Connection connection) {
		List<Course> schedules = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description, stream_id FROM COURSE;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				schedules.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getCourses() -->" + e.getMessage());
			return schedules;
		}
		return schedules;
	}

	public static Course getCourseById(Connection connection, int id) {
		Course course = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description, stream_id FROM COURSE WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				course = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getCourseById() -->" + e.getMessage());
			return course;
		}
		return course;
	}

	public static int create(Connection connection, Course course) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO COURSE (name, description, stream_id) VALUES (?, ?, ?);",
					course.getName(),
					course.getDescription(),
					course.getStream() != null && course.getStream().getId() != 0
							? course.getStream().getId()
							: null
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return DBUtils.getLastInsertRowId(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void update(Connection connection, Course course) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE COURSE SET name = ?, description = ?, stream_id = ? WHERE id = ?;",
					course.getName(),
					course.getDescription(),
					course.getStream() != null && course.getStream().getId() != 0
							? course.getStream().getId()
							: null,
					course.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Course course) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM COURSE WHERE id = ?;",
					course.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
