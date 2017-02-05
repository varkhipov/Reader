package com.grsu.reader.dao;

import com.grsu.reader.models.Class;
import com.grsu.reader.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.dao.ClassDAO.getClassById;
import static com.grsu.reader.dao.StudentDAO.getStudentById;
import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class StudentClassDAO {
	public static List<Student> getStudentsByClassId(Connection connection, int id, boolean registered) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT student_id FROM STUDENT_CLASS WHERE class_id = ? AND registered = ?;",
					id,
					registered ? 1 : 0
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(getStudentById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentsByClassId(id, registered) -->" + e.getMessage());
			return students;
		}
		return students;
	}

	public static List<Student> getStudentsByClassId(Connection connection, int id) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT student_id FROM STUDENT_CLASS WHERE class_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(getStudentById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentsByClassId(id) -->" + e.getMessage());
			return students;
		}
		return students;
	}

	public static List<Class> getClassesByStudentId(Connection connection, int id) {
		List<Class> classes = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT class_id FROM STUDENT_CLASS WHERE student_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				classes.add(getClassById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getClassesByStudentId() -->" + e.getMessage());
			return classes;
		}
		return classes;
	}

	public static void create(Connection connection, Student student, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO STUDENT_CLASS (student_id, class_id) VALUES (?, ?);",
					student.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void create(Connection connection, Student student, Class cls, boolean registered) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO STUDENT_CLASS (student_id, class_id, registered) VALUES (?, ?, ?);",
					student.getId(),
					cls.getId(),
					registered ? 1 : 0
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateStudentClassInfo(Connection connection,
											  int studentId,
											  int classId,
											  boolean registered,
											  LocalTime registrationTime,
											  String registrationType) throws SQLException {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT count(id) as count FROM STUDENT_CLASS WHERE class_id = ? AND student_id = ?;",
					classId,
					studentId
			);
			ResultSet resultSet = preparedStatement.executeQuery();
			int count = 0;
			while (resultSet.next()) {
				count = resultSet.getInt("count");
			}
			resultSet.close();
			preparedStatement.close();

			if (count == 0) {
				preparedStatement = buildPreparedStatement(
						connection,
						"INSERT INTO STUDENT_CLASS (student_id, class_id, registered, " +
								"registration_time, registration_type) " +
								"VALUES (?, ?, ?, ?, ?)",
						studentId,
						classId,
						registered ? 1 : 0,
						registrationTime,
						registrationType
				);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				preparedStatement = buildPreparedStatement(
						connection,
						"UPDATE STUDENT_CLASS SET registered = ?, registration_time = ?, registration_type = ? " +
								"WHERE class_id = ? AND student_id = ?;",
						registered ? 1 : 0,
						registrationTime,
						registrationType,
						classId,
						studentId
				);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}

			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void updateByStudentId(Connection connection, int studentId,
										 int classId, boolean registered, LocalTime registrationTime, String registrationType) throws SQLException {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STUDENT_CLASS SET registered = ?, registration_time = ?, registration_type = ? WHERE class_id = ? AND student_id = ?;",
					registered ? 1 : 0,
					registrationTime,
					registrationType,
					classId,
					studentId
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByClassId(Connection connection, Student student, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STUDENT_CLASS SET student_id = ? WHERE class_id = ?;",
					student.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Student student, Class cls) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STUDENT_CLASS WHERE student_id = ? AND class_id = ?;",
					student.getId(),
					cls.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByStudentId(Connection connection, int id) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STUDENT_CLASS WHERE student_id = ?;",
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
					"DELETE FROM STUDENT_CLASS WHERE class_id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
