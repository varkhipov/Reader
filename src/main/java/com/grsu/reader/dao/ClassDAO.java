package com.grsu.reader.dao;

import com.grsu.reader.models.Class;
import com.grsu.reader.models.Course;
import com.grsu.reader.models.Lesson;
import com.grsu.reader.utils.DBUtils;
import com.grsu.reader.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class ClassDAO {
	private static Class mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Class aClass = new Class();
		aClass.setId(resultSet.getInt("id"));
		aClass.setDate(LocalDate.parse(resultSet.getString("date")));
		aClass.setSchedule(
				ScheduleDAO.getScheduleById(connection, resultSet.getInt("schedule_id"))
		);
//		aClass.setLesson(
//				LessonDAO.getLessonById(connection, resultSet.getInt("lesson_id"))
//		);
		String sessionStart = resultSet.getString("session_start");
		if (sessionStart != null) {
			aClass.setSessionStart(LocalTime.parse(sessionStart));
		}
		String sessionEnd = resultSet.getString("session_end");
		if (sessionEnd != null) {
			aClass.setSessionEnd(LocalTime.parse(sessionEnd));
		}

		return aClass;
	}

	public static List<Class> getClasses(Connection connection) {
		List<Class> cls = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, date, schedule_id, lesson_id, session_start, session_end FROM CLASS;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				cls.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClasses() -->" + e.getMessage());
			return cls;
		}
		return cls;
	}

	public static Class getClassById(Connection connection, int id) {
		Class cls = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, date, schedule_id, lesson_id, session_start, session_end FROM CLASS WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				cls = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClassById() -->" + e.getMessage());
			return cls;
		}
		return cls;
	}

	public static List<Class> getClassesByLessonId(Connection connection, int id) {
		List<Class> cls = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, date, schedule_id, lesson_id, session_start, session_end " +
							"FROM CLASS " +
							"WHERE lesson_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				cls.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClassById() -->" + e.getMessage());
			return cls;
		}
		return cls;
	}

	public static int create(Connection connection, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO CLASS (date, schedule_id, lesson_id, session_start, session_end) " +
							"VALUES (?, ?, ?, ?, ?);",
					cls.getDate(),
					cls.getSchedule() != null && cls.getSchedule().getId() != 0
							? cls.getSchedule().getId()
							: null,
					cls.getLesson() != null && cls.getLesson().getId() != 0
							? cls.getLesson().getId()
							: null,
					cls.getSessionStart(),
					cls.getSessionEnd()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			return DBUtils.getLastInsertRowId(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void update(Connection connection, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE CLASS SET " +
							"date = ?, " +
							"schedule_id= ? " +
							"lesson_id = ?" +
							"session_start = ?" +
							"session_end= ?" +
							"WHERE id = ?;",
					cls.getDate(),
					cls.getSchedule() != null && cls.getSchedule().getId() != 0
							? cls.getSchedule().getId()
							: null,
					cls.getLesson() != null && cls.getLesson().getId() != 0
							? cls.getLesson().getId()
							: null,
					cls.getSessionStart(),
					cls.getSessionEnd(),

					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Class cls) {
		StudentClassDAO.deleteByClassId(connection, cls.getId());

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM CLASS WHERE id = ?;",
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
					"SELECT id " +
							"FROM CLASS " +
							"WHERE lesson_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				StudentClassDAO.deleteByClassId(connection, resultSet.getInt(1));
			}
			resultSet.close();
			preparedStatement.close();

			preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM CLASS WHERE lesson_id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
