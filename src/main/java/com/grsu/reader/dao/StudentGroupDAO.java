package com.grsu.reader.dao;

import com.grsu.reader.models.Group;
import com.grsu.reader.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.dao.GroupDAO.getGroupById;
import static com.grsu.reader.dao.StudentDAO.getStudentById;
import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class StudentGroupDAO {
	public static List<Student> getStudentsByGroupId(Connection connection, int id) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT student_id FROM STUDENT_GROUP WHERE group_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(getStudentById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentsByGroupId() -->" + e.getMessage());
			return students;
		}
		return students;
	}

	public static List<Student> getStudentsExcludeGroupId(Connection connection, int id) {
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT DISTINCT student_id FROM STUDENT_GROUP WHERE group_id != ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				students.add(getStudentById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStudentsExcludeGroupId() -->" + e.getMessage());
			return students;
		}
		return students;
	}

	public static List<Group> getGroupsByStudentId(Connection connection, int id) {
		List<Group> groups = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT group_id FROM STUDENT_GROUP WHERE student_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				groups.add(getGroupById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroupsByStudentId() -->" + e.getMessage());
			return groups;
		}
		return groups;
	}

	//TODO: Refactor all DAO create methods. Use only id's
	public static void create(Connection connection, int studentId, int groupId) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (?, ?);",
					studentId,
					groupId
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByStudentId(Connection connection, Student student, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STUDENT_GROUP SET group_id = ? WHERE student_id = ?;",
					group.getId(),
					student.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByGroupId(Connection connection, Student student, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STUDENT_GROUP SET student_id = ? WHERE group_id = ?;",
					student.getId(),
					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Student student, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STUDENT_GROUP WHERE student_id = ? AND group_id = ?;",
					student.getId(),
					group.getId()
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
					"DELETE FROM STUDENT_GROUP WHERE student_id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByGroupId(Connection connection, int id) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STUDENT_GROUP WHERE group_id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
