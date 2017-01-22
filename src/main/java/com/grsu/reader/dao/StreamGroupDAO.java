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
					"SELECT streamId FROM Stream_Group WHERE groupId = ?;",
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
					"SELECT groupId FROM Stream_Group WHERE streamId = ?;",
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
					"INSERT INTO Stream_Group (streamId, groupId) VALUES (?, ?);",
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
					"UPDATE Stream_Group SET groupId = ? WHERE streamId = ?;",
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
					"UPDATE Stream_Group SET streamId = ? WHERE groupId = ?;",
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
					"DELETE FROM Stream_Group WHERE streamId = ? AND groupId = ?;",
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
					"DELETE FROM Stream_Group WHERE streamId = ?;",
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
					"DELETE FROM Stream_Group WHERE groupId = ?;",
					id
			);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
