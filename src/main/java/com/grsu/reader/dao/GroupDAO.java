package com.grsu.reader.dao;

import com.grsu.reader.models.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
				return null;
			}
		return group;
	}
}
