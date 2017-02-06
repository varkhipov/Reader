package com.grsu.reader.dao;

import com.grsu.reader.models.Discipline;
import com.grsu.reader.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class DisciplineDAO {
	private static Discipline mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Discipline discipline = new Discipline();
		discipline.setId(resultSet.getInt("id"));
		discipline.setName(resultSet.getString("name"));
		discipline.setDescription(resultSet.getString("description"));

		return discipline;
	}

	public static List<Discipline> getDisciplines(Connection connection) {
		List<Discipline> schedules = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description FROM DISCIPLINE;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				schedules.add(mapFromResultSet(connection, resultSet));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDisciplines() -->" + e.getMessage());
			return schedules;
		}
		return schedules;
	}

	public static Discipline getDisciplineById(Connection connection, int id) {
		Discipline discipline = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT id, name, description FROM DISCIPLINE WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				discipline = mapFromResultSet(connection, resultSet);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDisciplineById() -->" + e.getMessage());
			return discipline;
		}
		return discipline;
	}

	public static int create(Connection connection, Discipline discipline) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO DISCIPLINE (name, description) VALUES (?, ?);",
					discipline.getName(),
					discipline.getDescription()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return DBUtils.getLastInsertRowId(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void update(Connection connection, Discipline discipline) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE DISCIPLINE SET name = ?, description = ? WHERE id = ?;",
					discipline.getName(),
					discipline.getDescription(),
					discipline.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Discipline discipline) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM DISCIPLINE WHERE id = ?;",
					discipline.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
