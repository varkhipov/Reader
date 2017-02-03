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
import static com.grsu.reader.dao.StreamDAO.getStreamById;
import static com.grsu.reader.dao.GroupDAO.getGroupById;

public class StreamGroupDAO {
	public static List<Stream> getStreamsByGroupId(Connection connection, int id) {
		List<Stream> streams = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT stream_id FROM STREAM_GROUP WHERE group_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				streams.add(getStreamById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getStreamsByGroupId() -->" + e.getMessage());
			return streams;
		}
		return streams;
	}

	public static List<Group> getGroupsByStreamId(Connection connection, int id) {
		List<Group> groups = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"SELECT group_id FROM STREAM_GROUP WHERE stream_id = ?;",
					id
			);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				groups.add(getGroupById(connection, resultSet.getInt(1)));
			}

			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			System.out.println("Error In getGroupsByStreamId() -->" + e.getMessage());
			return groups;
		}
		return groups;
	}

	public static void create(Connection connection, int streamId, int groupId) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (?, ?);",
					streamId,
					groupId
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByStreamId(Connection connection, Stream stream, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STREAM_GROUP SET group_id = ? WHERE stream_id = ?;",
					group.getId(),
					stream.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateByGroupId(Connection connection, Stream stream, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"UPDATE STREAM_GROUP SET stream_id = ? WHERE group_id = ?;",
					stream.getId(),
					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Connection connection, Stream stream, Group group) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STREAM_GROUP WHERE stream_id = ? AND group_id = ?;",
					stream.getId(),
					group.getId()
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByStreamId(Connection connection, int id) {
		try {
			PreparedStatement preparedStatement = buildPreparedStatement(
					connection,
					"DELETE FROM STREAM_GROUP WHERE stream_id = ?;",
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
					"DELETE FROM STREAM_GROUP WHERE group_id = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
