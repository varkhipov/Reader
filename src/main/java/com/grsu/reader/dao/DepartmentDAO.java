package com.grsu.reader.dao;

import com.grsu.reader.models.Department;
import com.grsu.reader.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class DepartmentDAO {
	public static List<Department> getDepartments(Connection connection) {
		List<Department> departments = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, abbreviation FROM DEPARTMENT;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				departments.add(new Department(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("abbreviation")
				));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDepartments() -->" + e.getMessage());
			return departments;
		}
		return departments;
	}

	public static Department getDepartmentById(Connection connection, int id) {
		Department department = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, abbreviation FROM DEPARTMENT WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				department = new Department(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("abbreviation"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDepartmentById() -->" + e.getMessage());
			return department;
		}
		return department;
	}

	public static Department getDepartmentByName(Connection connection, String name) {
		Department department = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, abbreviation FROM DEPARTMENT WHERE name = ?;",
					name
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				department = new Department(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("abbreviation"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDepartmentByName() -->" + e.getMessage());
			return department;
		}
		return department;
	}

	public static int create(Connection connection, Department department) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO DEPARTMENT (name, abbreviation) VALUES (?, ?);",
					department.getName(),
					department.getAbbreviation()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return DBUtils.getLastInsertRowId(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void update(Connection connection, Department department) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE DEPARTMENT SET name = ?, abbreviation = ? WHERE id = ?;",
					department.getName(),
					department.getAbbreviation(),
					department.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Department department) {
		GroupDAO.deleteByDepartmentId(connection, department.getId()); // TODO: delete or update to null?

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM DEPARTMENT WHERE id = ?;",
					department.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
