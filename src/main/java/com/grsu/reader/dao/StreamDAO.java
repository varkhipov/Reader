package com.grsu.reader.dao;

import com.grsu.reader.models.Group;
import com.grsu.reader.models.Stream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.reader.utils.DBUtils.buildPreparedStatement;
import static com.grsu.reader.utils.DBUtils.getLastInsertRowId;

public class StreamDAO {

	private static Stream mapFromResultSet(Connection connection, ResultSet resultSet) throws SQLException {
		Stream stream = new Stream();
		stream.setId(resultSet.getInt("id"));
		stream.setName(resultSet.getString("name"));
		stream.setGroups(StreamGroupDAO.getGroupsByStreamId(connection, stream.getId()));
		return stream;
	}
	
	public static List<Stream> getStreams(Connection connection) {
		List<Stream> streams = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT * FROM Stream;"
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				streams.add(mapFromResultSet(connection, resultSet));
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
				stream = mapFromResultSet(connection, resultSet);
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

		//TODO: probaly use same function in StudentDAO#create too
		int newStreamId = getLastInsertRowId(connection);
		if (newStreamId == 0) {
			System.out.println("Error in [com.grsu.reader.dao.StreamDAO.create]. " +
					"No groups added for stream with id 0: " + stream);
		} else {
			for (Group group : stream.getGroups()) {
				StreamGroupDAO.create(connection, newStreamId, group.getId());
			}
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

		StreamGroupDAO.deleteByStreamId(connection, stream.getId());
		for (Group group : stream.getGroups()) {
			StreamGroupDAO.create(connection, stream.getId(), group.getId());
		}
	}

	public static void delete(Connection connection, Stream stream) {
		StreamGroupDAO.deleteByStreamId(connection, stream.getId());

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
