package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableSet;
import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.*;

public class AlbumReadAll implements Producer<DatabaseQueryDefinition<Void, ImmutableSet<DatabaseEntity<Album>>>, Throwable> {

    private static final java.lang.String READ_ALL = String.format(
            String.join(" ",
                    "SELECT",
                    String.join(",", "%s", "%s", "%s"),
                    "FROM",
                    "%s"
            ), ID_COLUMN, NAME_COLUMN, DESCRIPTION_COLUMN, ALBUM_TABLE);


    @Override
    public DatabaseQueryDefinition<Void, ImmutableSet<DatabaseEntity<Album>>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Void, ImmutableSet<DatabaseEntity<Album>>>()
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void>, ImmutableSet<DatabaseEntity<Album>>, Throwable>() {
                    @Override
                    public ImmutableSet<DatabaseEntity<Album>> apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void> voidPreparedStatementWithQueryAndFunction) throws Throwable {
                        try (final ResultSet result = voidPreparedStatementWithQueryAndFunction.getPreparedStatement().executeQuery()) {
                            final ImmutableSet.Builder<DatabaseEntity<Album>> allAlbumsBuilder = ImmutableSet.builder();
                            try {
                                while (result.next()) {

                                    try {
                                        allAlbumsBuilder.add(
                                                new DatabaseEntity.Builder<Album>()
                                                        .setDatabaseReference(result.getLong(ID_COLUMN))
                                                        .setObject(new Album.Builder()
                                                                .setDecription(result.getString(DESCRIPTION_COLUMN))
                                                                .setName(result.getString(NAME_COLUMN))
                                                                .build())
                                                        .build()
                                        );
                                    } catch (Value.BuilderIncompleteException e) {
                                        throw new AlbumReadAllException("Unable to build something", e);
                                    }
                                }
                            } catch (SQLException e) {
                                throw new AlbumReadAllException("Error reading a result", e);
                            }
                            return allAlbumsBuilder.build();

                        }
                    }
                })
                .setQuery(READ_ALL).build();
    }


    public static class AlbumReadAllException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 3044535988608330489L;

        public AlbumReadAllException(String s, Exception e) {
            super(s, e);
        }
    }

}
