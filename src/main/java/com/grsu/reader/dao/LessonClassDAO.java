package com.grsu.reader.dao;

import com.grsu.reader.models.Lesson;
import com.grsu.reader.models.Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.dao.LessonDAO.getLessonById;
import static com.grsu.reader.dao.ClassDAO.getClassById;
import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LessonClassDAO {
	public static List<Lesson> getLessonsByClassId(Connection connection, int id) {
		List<Lesson> lessons = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT lessonId FROM Lesson_Class WHERE classId = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lessons.add(getLessonById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLessonsByClassId() -->" + e.getMessage());
			return lessons;
		}
		return lessons;
	}

	public static List<Class> getClassesByLessonId(Connection connection, int id) {
		List<Class> classes = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT classId FROM Lesson_Class WHERE lessonId = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				classes.add(getClassById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClassesByLessonId() -->" + e.getMessage());
			return classes;
		}
		return classes;
	}

	public static void create(Connection connection, Lesson lesson, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Lesson_Class (lessonId, classId) VALUES (?, ?);",
					lesson.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByLessonId(Connection connection, Lesson lesson, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Lesson_Class SET classId = ? WHERE lessonId = ?;",
					cls.getId(),
					lesson.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByClassId(Connection connection, Lesson lesson, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Lesson_Class SET lessonId = ? WHERE classId = ?;",
					lesson.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Lesson lesson, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Lesson_Class WHERE lessonId = ? AND classId = ?;",
					lesson.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByLessonId(Connection connection, int id) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Lesson_Class WHERE lessonId = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByClassId(Connection connection, int id) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Lesson_Class WHERE classId = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
