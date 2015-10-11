package com.mieoffline.http.fileupload.repository.postgres;

import com.mieoffline.functional.Consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SafePreparedStatementConsumer implements Consumer<Consumer<PreparedStatement, SQLException>, SafePreparedStatementConsumer.SafePreparedStatementFunctionException> {
    private final Consumer<Consumer<Connection, SQLException>, ?> connectionProducer;
    private final String sql;

    public SafePreparedStatementConsumer(Consumer<Consumer<Connection, SQLException>, ?> connectionProducer, String sql) {
        this.connectionProducer = connectionProducer;
        this.sql = sql;
    }


    @Override
    public Void apply(Consumer<PreparedStatement, SQLException> consumer) throws SafePreparedStatementFunctionException {
        final Consumer<Connection, SQLException> consumable1 = new Consumer<Connection, SQLException>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
                try (final PreparedStatement preparedStatement = connection.prepareStatement(SafePreparedStatementConsumer.this.sql)) {
                	consumer.apply(preparedStatement);
                }	
                return null;
            }
        };
        try {
            this.connectionProducer.apply(consumable1);
        } catch (Throwable t) {
            throw new SafePreparedStatementFunctionException("Error executing query", t);
        }

        return null;
    }


    public static class SafePreparedStatementFunctionException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = 5374824155160391011L;

		public SafePreparedStatementFunctionException(String m, Throwable e) {
            super(m, e);
        }
    }
}
