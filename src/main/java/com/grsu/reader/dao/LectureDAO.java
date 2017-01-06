package com.grsu.reader.dao;

import com.grsu.reader.models.Lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.*;

public class LectureDAO {

	private static Lecture mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Lecture lecture = new Lecture();
		lecture.setId(resultSet.getInt("id"));
		lecture.setName(resultSet.getString("name"));
		lecture.setDateTime(LocalDateTime.parse(resultSet.getString("dateTime")));

		lecture.setSchedule(
				ScheduleDAO.getScheduleById(connection,
						Integer.valueOf(resultSet.getString("scheduleId")))
		);

		lecture.setDiscipline(
				DisciplineDAO.getDisciplineById(connection,
						Integer.valueOf(resultSet.getString("disciplineId")))
		);

		lecture.setLecturer(
				LecturerDAO.getLecturerById(connection,
						Integer.valueOf(resultSet.getString("lecturerId")))
		);

		lecture.setGroup(
				GroupDAO.getGroupById(connection,
						Integer.valueOf(resultSet.getString("groupId")))
		);
		return lecture;
	}

	public static List<Lecture> getLectures(Connection connection) {
		List<Lecture> lectures = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Lecture"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lectures.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLectures() -->" + e.getMessage());
			return null;
		}
		return lectures;
	}

	public static Lecture getLectureById(Connection connection, int id) {
		Lecture lecture = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Lecture WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lecture = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getLectureById() -->" + e.getMessage());
			return null;
		}
		return lecture;
	}
}
