package com.grsu.reader.dao;

import com.grsu.reader.models.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.grsu.reader.utils.DBUtils.*;

public class GroupDAO {
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
