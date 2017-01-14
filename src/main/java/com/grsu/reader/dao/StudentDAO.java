package com.grsu.reader.dao;

import com.grsu.reader.models.Lecture;
import com.grsu.reader.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.*;

public class StudentDAO {

	private static Student mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Student student = new Student();
		student.setId(resultSet.getInt("id"));
		student.setUid(resultSet.getString("uid"));
		student.setName(resultSet.getString("name"));
		student.setSurname(resultSet.getString("surname"));
		student.setPatronymic(resultSet.getString("patronymic"));
		student.setPhone(resultSet.getString("phone"));
		student.setEmail(resultSet.getString("email"));
		student.setNotes(resultSet.getString("notes"));
		// TODO: add photo

		String groupId = resultSet.getString("groupId");
		if (groupId != null) {
			student.setGroup(GroupDAO.getGroupById(connection, Integer.valueOf(groupId)));
		}
		return student;
	}

	public static List<Student> getStudents(Connection connection) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Student;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudents() -->" + e.getMessage());
			return null;
		}
		return students;
	}

	public static Student getStudentById(Connection connection, int id) {
		Student student = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Student WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				student = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentById() -->" + e.getMessage());
			return null;
		}
		return student;
	}

	public static Student getStudentByUID(Connection connection, String uid) {
		Student student = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Student WHERE uid = ?;",
					uid
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				student = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentByUID() -->" + e.getMessage());
			return null;
		}
		return student;
	}

	public static void updateStudent(Connection connection, Student student) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Student " +
							"SET " +
								"uid = ?, name = ?, surname = ?, " +
								"patronymic = ?, phone = ?, email = ?, " +
								"notes = ?, groupId = ? " +
							"WHERE id = ?;",
					student.getUid(),
					student.getName(),
					student.getSurname(),
					student.getPatronymic(),
					student.getPhone(),
					student.getEmail(),
					student.getNotes(),
					student.getGroup() != null && student.getGroup().getId() != 0
							? student.getGroup().getId()
							: null,

					student.getId()
			);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addStudent(Connection connection, Student student) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Student (" +
								"uid, name, surname, " +
								"patronymic, phone, email, " +
								"notes, groupId" +
							") VALUES (" +
								"?, ?, ?, ?, ?, ?, ?, ?" +
							");",
					student.getUid(),
					student.getName(),
					student.getSurname(),
					student.getPatronymic(),
					student.getPhone(),
					student.getEmail(),
					student.getNotes(),
					student.getGroup() != null && student.getGroup().getId() != 0
							? student.getGroup().getId()
							: null
			);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteStudent(Connection connection, Student student) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Student WHERE id = ?;",
					student.getId()
			);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
