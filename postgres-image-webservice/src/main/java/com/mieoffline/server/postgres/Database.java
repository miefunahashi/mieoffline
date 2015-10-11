package com.mieoffline.server.postgres;

import com.jolbox.bonecp.BoneCP;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Function;

import java.sql.Connection;
import java.sql.SQLException;

public class Database implements IDatabase {
    private final BoneCP connectionPool;

    public Database(BoneCP connectionPool) {
        this.connectionPool = connectionPool;
    }


    @Override
    public void close() throws SQLException {
        this.connectionPool.shutdown();
    }


    @Override
    public  Void execute(Function<Connection, Void, Throwable> connectionSQLExceptionConsumer) throws DatabaseConnectionException {
        try (final Connection connection = this.connectionPool.getConnection()) {
            try {
                connectionSQLExceptionConsumer.apply(connection);
                return null;
            } catch (Throwable throwable) {
                throw new DatabaseConnectionException("Error using database connection", throwable);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error getting a database connection", e);
        }
    }


}
