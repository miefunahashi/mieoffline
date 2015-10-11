package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.MieRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseRunnableQuery implements MieRunnable<DatabaseRunnableQuery.DatabaseRunnableQueryException> {
	private final IDatabase connectionProducer;
	private Function<Connection, Void, Throwable> connectionThrowableConsumer;

	public DatabaseRunnableQuery(IDatabase connectionProducer, QueryParamterModel queryParamterModel) {
		this.connectionProducer = connectionProducer;
		this.connectionThrowableConsumer = connection -> {
			if (queryParamterModel.getReturnGeneratedKeys()) {
				try (final PreparedStatement ps = connection.prepareStatement(queryParamterModel.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
					ps.execute();
				}
			} else {
				try (final PreparedStatement ps = connection.prepareStatement(queryParamterModel.getQuery())) {
					ps.execute();
				}
			
			}
			return null;
		};
	}

	@Override
	public Void apply(Void v) throws DatabaseRunnableQueryException {
		try {
			this.connectionProducer.execute(this.connectionThrowableConsumer);
			return null;
		} catch (Throwable e) {
			throw new DatabaseRunnableQueryException("Unable to get connection", e);
		}

	}

	public static class DatabaseRunnableQueryException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7294936329651201100L;

		public DatabaseRunnableQueryException(String s, Throwable e) {
			super(s, e);
		}
	}

}
