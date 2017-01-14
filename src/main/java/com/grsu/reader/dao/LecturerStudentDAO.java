package com.grsu.reader.dao;

import com.grsu.reader.models.Lecture;
import com.grsu.reader.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.dao.StudentDAO.getStudentById;
import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class LecturerStudentDAO {
	public static List<Student> getStudentsByLectureId(Connection connection, int id) {
		List<Student> students = new ArrayList<>();
		try {
			Lecture lecture = LectureDAO.getLectureById(connection, id);
			if (lecture == null) {
				return students;
			}

			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT studentId FROM Lecture_Student WHERE lectureId = ?;",
					lecture.getId()
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(getStudentById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentsByLectureId() -->" + e.getMessage());
			return null;
		}
		return students;
	}
}
