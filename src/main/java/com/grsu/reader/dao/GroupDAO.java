package com.grsu.reader.dao;

import com.grsu.reader.models.Group;
import com.grsu.reader.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.*;

public class GroupDAO {
	private static Group mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Group group = new Group();
		group.setId(resultSet.getInt("id"));
		group.setName(resultSet.getString("name"));
		group.setDepartment(
				DepartmentDAO.getDepartmentById(connection,
						resultSet.getInt("department_id"))
		);

		return group;
	}

	public static List<Group> getGroups(Connection connection) {
		List<Group> groups = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, department_id FROM [GROUP];"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				groups.add(mapFromResultSet(connection, resultSet));
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
					"SELECT id, name, department_id FROM [GROUP] WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				group = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroupById() -->" + e.getMessage());
			return group;
		}
		return group;
	}

	public static Group getGroupByName(Connection connection, String name) {
		Group group = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, department_id FROM [GROUP] WHERE name = ?;",
					name
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				group = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroupByName() -->" + e.getMessage());
			return group;
		}
		return group;
	}

	public static List<Integer> getGroupsIdsByDepartmentId(Connection connection, int department_id) {
		List<Integer> ids = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id FROM [GROUP] WHERE department_id = ?;",
					department_id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				ids.add(resultSet.getInt(1));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroupsIdsByDepartmentId() -->" + e.getMessage());
			return null;
		}
		return ids;
	}

	public static int create(Connection connection, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO [GROUP] (name, department_id) VALUES (?, ?);",
					group.getName(),
					group.getDepartment() != null && group.getDepartment().getId() != 0
							? group.getDepartment().getId()
							: null
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			System.out.println("Group [ " + group.getName() + " ] successfully added to database.");
			return DBUtils.getLastInsertRowId(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void update(Connection connection, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE [GROUP] SET name = ?, department_id = ? WHERE id = ?;",
					group.getName(),
					group.getDepartment() != null && group.getDepartment().getId() != 0
							? group.getDepartment().getId()
							: null,

					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, int id) {
		StudentGroupDAO.deleteByGroupId(connection, id);
		StreamGroupDAO.deleteByGroupId(connection, id);

		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM [GROUP] WHERE id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByDepartmentId(Connection connection, int department_id) {
		List<Integer> ids = getGroupsIdsByDepartmentId(connection, department_id);
		if (ids != null) {
			for (Integer id : ids) {
				delete(connection, id);
			}
		}
	}
}
