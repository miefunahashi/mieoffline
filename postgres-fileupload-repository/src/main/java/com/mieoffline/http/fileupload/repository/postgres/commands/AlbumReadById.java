package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.DatabaseEntity;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.*;

public class AlbumReadById implements Producer<DatabaseQueryDefinition<Long, DatabaseEntity<Long, Album>>, Throwable> {

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
    public DatabaseQueryDefinition<Long, DatabaseEntity<Long, Album>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, DatabaseEntity<Long, Album>>()
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long>, DatabaseEntity<Long, Album>, Throwable>() {
                    @Override
                    public DatabaseEntity<Long, Album> apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long> longPreparedStatementWithQueryAndFunction) throws Throwable {
                        final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                        ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                        try (ResultSet result = ps.executeQuery()) {
                            if (result.next()) {

                                try {
                                    return new DatabaseEntity.Builder<Long, Album>()
                                            .setDatabaseReference(
                                                    new DatabaseReference.Builder<Long>().setReference(
                                                            result.getLong(ID_COLUMN)).build())
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
