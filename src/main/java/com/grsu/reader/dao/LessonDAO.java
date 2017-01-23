package com.grsu.reader.dao;

import com.grsu.reader.models.Lesson;

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
		lesson.setTimeBefore(resultSet.getInt("timeBefore"));
		lesson.setTimeAfter(resultSet.getInt("timeAfter"));
		lesson.setCreateDate(LocalDateTime.parse(resultSet.getString("createDate")));

		lesson.setDiscipline(
				DisciplineDAO.getDisciplineById(connection,
						resultSet.getInt("disciplineId"))
		);

		lesson.setLecturer(
				LecturerDAO.getLecturerById(connection,
						resultSet.getInt("lecturerId"))
		);

		lesson.setStream(
				StreamDAO.getStreamById(connection,
						resultSet.getInt("streamId"))
		);

		lesson.setClasses(
				LessonClassDAO.getClassesByLessonId(connection, lesson.getId())
		);
		return lesson;
	}

	public static List<Lesson> getLessons(Connection connection) {
		List<Lesson> lessons = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Lesson;"
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
					"SELECT * FROM Lesson WHERE id = ?;",
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
	
	public static void create(Connection connection, Lesson lesson) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Lesson (" +
								"name, timeBefore, timeAfter, " +
								"disciplineId, lecturerId, streamId" +
							") VALUES (" +
								"?, ?, ?, " +
								"?, ?, ?" +
							");",
					lesson.getName(),
					lesson.getTimeBefore(),
					lesson.getTimeAfter(),
					lesson.getDiscipline() != null && lesson.getDiscipline().getId() != 0
							? lesson.getDiscipline().getId()
							: null,
					lesson.getLecturer() != null && lesson.getLecturer().getId() != 0
							? lesson.getLecturer().getId()
							: null,
					lesson.getStream() != null && lesson.getStream().getId() != 0
							? lesson.getStream().getId()
							: null
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Lesson lesson) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Lesson " +
							"SET " +
								"name = ?, timeBefore = ?, timeAfter = ?, " +
								"disciplineId = ?, lecturerId = ?, streamId = ? " +
							"WHERE id = ?;",
					lesson.getName(),
					lesson.getTimeBefore(),
					lesson.getTimeAfter(),
					lesson.getDiscipline() != null && lesson.getDiscipline().getId() != 0
							? lesson.getDiscipline().getId()
							: null,
					lesson.getLecturer() != null && lesson.getLecturer().getId() != 0
							? lesson.getLecturer().getId()
							: null,
					lesson.getStream() != null && lesson.getStream().getId() != 0
							? lesson.getStream().getId()
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
		LessonClassDAO.deleteByLessonId(connection, lesson.getId());

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Lesson WHERE id = ?;",
					lesson.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
