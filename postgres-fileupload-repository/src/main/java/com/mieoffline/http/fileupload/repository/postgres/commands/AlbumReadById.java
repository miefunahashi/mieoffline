package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.*;

public class AlbumReadById implements Producer<DatabaseQueryDefinition<Long, DatabaseEntity<Album>>, Throwable> {

    private final static String READ_GIVEN_ID =
            String.format(
                    String.join(" ",
                            "SELECT",
                            String.join(",", "%s", "%s"),
                            "FROM",
                            "%s",
                            "WHERE",
                            "%s = ?"
                    ), NAME_COLUMN, DESCRIPTION_COLUMN, ALBUM_TABLE, ID_COLUMN);


    @Override
    public DatabaseQueryDefinition<Long, DatabaseEntity<Album>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, DatabaseEntity<Album>>()
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long>, DatabaseEntity<Album>, Throwable>() {
                    @Override
                    public DatabaseEntity<Album> apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long> longPreparedStatementWithQueryAndFunction) throws Throwable {
                        final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                        ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                        try (ResultSet result = ps.executeQuery()) {
                            if (result.next()) {

                                try {
                                    return new DatabaseEntity.Builder<Album>()
                                            .setDatabaseReference(result.getLong(ID_COLUMN))
                                            .setObject(new Album.Builder()
                                                    .setDecription(result.getString(DESCRIPTION_COLUMN))
                                                    .setName(result.getString(NAME_COLUMN))
                                                    .build())
                                            .build();

                                } catch (Value.BuilderIncompleteException e) {
                                    throw new AlbumReadByIdException("Unable to create value", e);
                                }

                            }
                            throw new AlbumReadByIdException("No result found");
                        }
                    }
                })
                .setQuery(READ_GIVEN_ID)
                .build();
    }

    public static class AlbumReadByIdException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 6609043421884863978L;

        public AlbumReadByIdException(String s, Exception e) {
            super(s, e);
        }

        public AlbumReadByIdException(String s) {
            super(s);
        }
    }
}
