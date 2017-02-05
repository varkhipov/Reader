package com.grsu.reader.dao;

import com.grsu.reader.models.Group;
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
		student.setParsedUid(resultSet.getInt("parsed_uid"));
		student.setFirstName(resultSet.getString("first_name"));
		student.setLastName(resultSet.getString("last_name"));
		student.setPatronymic(resultSet.getString("patronymic"));
		student.setPhone(resultSet.getString("phone"));
		student.setEmail(resultSet.getString("email"));
		// TODO: add photo

		student.setGroups(StudentGroupDAO.getGroupsByStudentId(connection, student.getId()));
		return student;
	}

	public static List<Student> getStudents(Connection connection) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, uid, parsed_uid, first_name, last_name, patronymic, phone, email FROM STUDENT;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudents() -->" + e.getMessage());
			return students;
		}
		return students;
	}

	public static Student getStudentById(Connection connection, int id) {
		Student student = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, uid, parsed_uid, first_name, last_name, patronymic, phone, email FROM STUDENT WHERE id = ?;",
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
			return student;
		}
		return student;
	}

	public static Student getStudentByUID(Connection connection, String uid) {
		Student student = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, uid, parsed_uid, first_name, last_name, patronymic, phone, email FROM STUDENT WHERE uid = ?;",
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
			return student;
		}
		return student;
	}


	public static int getStudentIdByUID(Connection connection, String uid) {
		int id = 0;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id FROM STUDENT WHERE uid = ?;",
					uid
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				id = resultSet.getInt(1);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentIdByUID() [returned 0] -->" + e.getMessage());
			return id;
		}
		return id;
	}

	public static int create(Connection connection, Student student) {
		if (student.getUid() == null || student.getUid().isEmpty()) {
			System.out.println("Warning! Student [ "
					+ student.getFullName()
					+ " ] has no UID and will be added to database without it."
			);
		}
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO STUDENT (" +
								"uid, parsed_uid, first_name, last_name, " +
								"patronymic, phone, email" +
							") VALUES (" +
								"?, ?, ?, ?, " +
								"?, ?, ?" +
							");",
					student.getUid(),
					student.getParsedUid(),
					student.getFirstName(),
					student.getLastName(),
					student.getPatronymic(),
					student.getPhone(),
					student.getEmail()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int newStudentId = getStudentIdByUID(connection, student.getUid());
		if (newStudentId == 0) {
			System.out.println("Error in [com.grsu.reader.dao.StudentDAO.create]. " +
					"No groups added for student with id 0: " + student);
		} else {
			for (Group group : student.getGroups()) {
				StudentGroupDAO.create(connection, newStudentId, group.getId());
			}
		}
		System.out.println("Student [ " + student.getFullName() + " ] successfully added to database.");
		return newStudentId;
	}

	public static void update(Connection connection, Student student) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STUDENT " +
							"SET " +
								"uid = ?, parsed_uid = ?, first_name = ?, last_name = ?, " +
								"patronymic = ?, phone = ?, email = ? " +
							"WHERE id = ?;",
					student.getUid(),
					student.getParsedUid(),
					student.getFirstName(),
					student.getLastName(),
					student.getPatronymic(),
					student.getPhone(),
					student.getEmail(),

					student.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		StudentGroupDAO.deleteByStudentId(connection, student.getId());
		for (Group group : student.getGroups()) {
			StudentGroupDAO.create(connection, student.getId(), group.getId());
		}
	}

	public static void delete(Connection connection, Student student) {
		StudentClassDAO.deleteByStudentId(connection, student.getId());
		StudentGroupDAO.deleteByStudentId(connection, student.getId());

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STUDENT WHERE id = ?;",
					student.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
