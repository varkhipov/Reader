package com.grsu.reader.dao;

import com.grsu.reader.models.Stream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;

public class StreamDAO {
	public static List<Stream> getStreams(Connection connection) {
		List<Stream> streams = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Stream;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				streams.add(new Stream(
						resultSet.getInt("id"),
						resultSet.getString("name")
				));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStreams() -->" + e.getMessage());
			return streams;
		}
		return streams;
	}
	
	public static Stream getStreamById(Connection connection, int id) {
		Stream stream = null;
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Stream WHERE id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				stream = new Stream(
						resultSet.getInt("id"),
						resultSet.getString("name")
				);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStreamById() -->" + e.getMessage());
			return stream;
		}
		return stream;
	}

	public static void create(Connection connection, Stream stream) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO Stream (name) VALUES (?);",
					stream.getName()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection connection, Stream stream) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE Stream SET name = ? WHERE id = ?;",
					stream.getName(),
					stream.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Stream stream) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM Stream WHERE id = ?;",
					stream.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
