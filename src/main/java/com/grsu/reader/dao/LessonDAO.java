package com.grsu.reader.dao;

import com.grsu.reader.models.Lesson;
import com.grsu.reader.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LessonDAO {

	private static Lesson mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(resultSet.getInt("id"));
		lesson.setName(resultSet.getString("name"));
		lesson.setDescription(resultSet.getString("description"));
		lesson.setCreateDate(LocalDateTime.parse(resultSet.getString("create_date")));

		lesson.setCourse(
				CourseDAO.getCourseById(connection,
						resultSet.getInt("course_id"))
		);

		lesson.setGroup(
				GroupDAO.getGroupById(connection,
						resultSet.getInt("group_id"))
		);


		lesson.setLessonType(
				LessonTypeDAO.getLessonTypeById(connection,
						resultSet.getInt("type_id"))
		);

		lesson.setClasses(
				ClassDAO.getClassesByLessonId(connection, lesson.getId())
		);

		return lesson;
	}

	public static List<Lesson> getLessons(Connection connection) {
		List<Lesson> lessons = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description, course_id, create_date, type_id, group_id FROM LESSON;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lessons.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLessons() -->" + e.getMessage());
			return lessons;
		}
		return lessons;
	}

	public static Lesson getLessonById(Connection connection, int id) {
		Lesson lesson = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description, course_id, create_date, type_id, group_id FROM LESSON WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lesson = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLessonById() -->" + e.getMessage());
			return lesson;
		}
		return lesson;
	}

	public static int create(Connection connection, Lesson lesson) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO LESSON (" +
							"name, description, course_id, type_id, group_id" +
							") VALUES (" +
							"?, ?, " +
							"?, ?, ?" +
							");",
					lesson.getName(),
					lesson.getDescription(),
					lesson.getCourse() != null && lesson.getCourse().getId() != 0
							? lesson.getCourse().getId()
							: null,
					lesson.getLessonType() != null && lesson.getLessonType().getId() != 0
							? lesson.getLessonType().getId()
							: null,
					lesson.getGroup() != null && lesson.getGroup().getId() != 0
							? lesson.getGroup().getId()
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

	public static void update(Connection connection, Lesson lesson) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE LESSON " +
							"SET " +
							"name = ?, description = ?, course_id = ?, " +
							"type_id = ?, group_id = ? " +
							"WHERE id = ?;",
					lesson.getName(),
					lesson.getDescription(),
					lesson.getCourse() != null && lesson.getCourse().getId() != 0
							? lesson.getCourse().getId()
							: null,
					lesson.getLessonType() != null && lesson.getLessonType().getId() != 0
							? lesson.getLessonType().getId()
							: null,
					lesson.getGroup() != null && lesson.getGroup().getId() != 0
							? lesson.getGroup().getId()
							: null,

					lesson.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Lesson lesson) {
		ClassDAO.deleteByLessonId(connection, lesson.getId());

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM LESSON WHERE id = ?;",
					lesson.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
