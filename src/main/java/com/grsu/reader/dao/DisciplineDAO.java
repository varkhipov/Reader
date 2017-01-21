package com.grsu.reader.dao;

import com.grsu.reader.models.Discipline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class DisciplineDAO {
	public static List<Discipline> getDisciplines(Connection connection) {
		List<Discipline> schedules = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Discipline;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				schedules.add(new Discipline(
						resultSet.getInt("id"),
						resultSet.getString("name")
				));
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
					"SELECT * FROM Discipline WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				discipline = new Discipline(
						resultSet.getInt("id"),
						resultSet.getString("name")
				);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getDisciplineById() -->" + e.getMessage());
			return discipline;
		}
		return discipline;
	}

	public static void create(Connection connection, Discipline discipline) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Discipline (name) VALUES (?);",
					discipline.getName()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Discipline discipline) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Discipline SET name = ? WHERE id = ?;",
					discipline.getName(),
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
					"DELETE FROM Discipline WHERE id = ?;",
					discipline.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
