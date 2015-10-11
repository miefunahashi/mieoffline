package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.*;

public class AlbumStore implements Producer<DatabaseQueryDefinition<Album, Long>, Throwable> {
	private final static Logger LOGGER = LoggerFactory.getLogger(AlbumStore.class);
	private final static String STORE_QUERY = String.format("INSERT INTO %s(" + "%s," + "%s) VALUES ( ?, ?);",
			ALBUM_TABLE, NAME_COLUMN, DESCRIPTION_COLUMN

	);

	@Override
	public DatabaseQueryDefinition<Album, Long> apply(Void aVoid) throws Throwable {
		return new DatabaseQueryDefinition.Builder<Album, Long>().setFunction(
				new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Album>, Long, Throwable>() {
					@Override
					public Long apply(
							DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Album> albumPreparedStatementWithQueryAndFunction)
									throws Throwable {
						final PreparedStatement ps = albumPreparedStatementWithQueryAndFunction.getPreparedStatement();
						final Album album = albumPreparedStatementWithQueryAndFunction.getT();
						ps.setString(1, album.getName());
						ps.setString(2, album.getDescription());
						final int something = ps.executeUpdate();
						LOGGER.error("" + something);
						try (final ResultSet resultSet = ps.getGeneratedKeys()) {
							if (resultSet.next()) {
								final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
								final int columnCount = resultSetMetaData.getColumnCount();
								for (int i = 1; i <= columnCount; i++) {
									final String columnName = resultSetMetaData.getColumnName(i);
									if (ID_COLUMN.equals(columnName)) {
										return Long.valueOf(resultSet.getString(i));
									}
								}
							}
							throw new IllegalStateException("No column name found");
						}
					}
				}).setQuery(STORE_QUERY).build();
	}

	public static class AlbumStoreException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6459608670760460006L;

		public AlbumStoreException(String s, Exception e) {
			super(s, e);
		}
	}
}
