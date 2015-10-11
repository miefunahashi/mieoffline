package com.markoffline.site.database.model;

import com.mieoffline.functional.Function;

import java.sql.Connection;

public interface IDatabase extends AutoCloseable {
    Void execute(Function<Connection, Void, Throwable> connectionSQLExceptionConsumer) throws DatabaseConnectionException;

    public static class DatabaseConnectionException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -7864726861568280175L;

		public DatabaseConnectionException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }
}
