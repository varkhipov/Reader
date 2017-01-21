package com.grsu.reader.dao;

import com.grsu.reader.models.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.*;

public class GroupDAO {
	public static List<Group> getGroups(Connection connection) {
		List<Group> groups = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM [Group];"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				groups.add(new Group(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						FacultyDAO.getFacultyById(connection, resultSet.getInt("facultyId"))
				));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroups() -->" + e.getMessage());
			return groups;
		}
		return groups;
	}
	
	public static Group getGroupById(Connection connection, int id) {
		Group group = null;
			try {
				PreparedStatement preparedStatement = buildPreparedStatement(
						connection,
						"SELECT * FROM [Group] WHERE id = ?;",
						id
				);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					group = new Group();
					group.setId(resultSet.getInt("id"));
					group.setName(resultSet.getString("name"));
					group.setFaculty(FacultyDAO.getFacultyById(connection, resultSet.getInt("facultyId")));
				}
				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				System.out.println("Error In getGroupById() -->" + e.getMessage());
				return group;
			}
		return group;
	}

	public static void create(Connection connection, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO [Group] (name, facultyId) VALUES (?, ?);",
					group.getName(),
					group.getFaculty() != null && group.getFaculty().getId() != 0
							? group.getFaculty().getId()
							: null
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE [Group] SET name = ?, facultyId = ? WHERE id = ?;",
					group.getName(),
					group.getFaculty() != null && group.getFaculty().getId() != 0
							? group.getFaculty().getId()
							: null,

					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM [Group] WHERE id = ?;",
					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
