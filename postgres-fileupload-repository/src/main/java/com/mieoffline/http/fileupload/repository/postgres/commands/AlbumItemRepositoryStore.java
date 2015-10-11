package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.AlbumItemRepositoryStoreModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.ID_COLUMN;

public class AlbumItemRepositoryStore
		implements Producer<DatabaseQueryDefinition<AlbumItemRepositoryStoreModel, Long>, Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlbumItemRepositoryStore.class);

	private boolean isNext(ResultSet result) throws AlbumItemRepositoryStoreException {
		try {
			return result.next();
		} catch (SQLException e) {
			throw new AlbumItemRepositoryStoreException("Unable to get next entry in database", e);
		}
	}

	@Override
	public DatabaseQueryDefinition<AlbumItemRepositoryStoreModel, Long> apply(Void aVoid) throws Throwable {
		return new DatabaseQueryDefinition.Builder<AlbumItemRepositoryStoreModel, Long>()
				.setQuery(AlbumItemRepositoryStoreModel.STORE_QUERY)
				.setFunction(albumItemRepositoryStoreModelPreparedStatementWithQueryAndFunction -> {
					final PreparedStatement ps = albumItemRepositoryStoreModelPreparedStatementWithQueryAndFunction
							.getPreparedStatement();
					final AlbumItemRepositoryStoreModel albumItemRepositoryStoreModel = albumItemRepositoryStoreModelPreparedStatementWithQueryAndFunction
							.getT();
					ps.setLong(1, albumItemRepositoryStoreModel.getAlbumItemKey());
					ps.setLong(2, albumItemRepositoryStoreModel.getFileUploadKey());
					final int something = ps.executeUpdate();
					LOGGER.error("" + something);
					try (final ResultSet resultSet = ps.getGeneratedKeys()) {
						if (isNext(resultSet)) {
							final ResultSetMetaData resultSetMetaData;
							resultSetMetaData = resultSet.getMetaData();
							final int columnCount = resultSetMetaData.getColumnCount();
							for (int i = 1; i <= columnCount; i++) {
								final String columnName = resultSetMetaData.getColumnName(i);
								if (ID_COLUMN.equals(columnName)) {
									final String id = resultSet.getString(i);
									return Long.valueOf(id);
								}
							}
							throw new AlbumItemRepositoryStoreException("No column name found");
						}
						throw new AlbumItemRepositoryStoreException("No column name found");
					}
				}).build();
	}

	public static class AlbumItemRepositoryStoreException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7621906156316060902L;

		public AlbumItemRepositoryStoreException(String s, Exception e) {
			super(s, e);
		}

		public AlbumItemRepositoryStoreException(String s) {
			super(s);
		}
	}
}
