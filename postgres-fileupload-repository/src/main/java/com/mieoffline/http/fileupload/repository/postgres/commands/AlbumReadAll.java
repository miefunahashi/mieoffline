package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableSet;
import com.markoffline.site.database.model.DatabaseEntity;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.*;

public class AlbumReadAll implements Producer<DatabaseQueryDefinition<Void, ImmutableSet<DatabaseEntity<Long, Album>>>, Throwable> {

    private static final java.lang.String READ_ALL = String.format(
            String.join(" ",
                    "SELECT",
                    String.join(",", "%s", "%s", "%s"),
                    "FROM",
                    "%s"
            ), ID_COLUMN, NAME_COLUMN, DESCRIPTION_COLUMN, ALBUM_TABLE);


    @Override
    public DatabaseQueryDefinition<Void, ImmutableSet<DatabaseEntity<Long, Album>>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Void, ImmutableSet<DatabaseEntity<Long, Album>>>()
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void>, ImmutableSet<DatabaseEntity<Long, Album>>, Throwable>() {
                    @Override
                    public ImmutableSet<DatabaseEntity<Long, Album>> apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void> voidPreparedStatementWithQueryAndFunction) throws Throwable {
                        try (final ResultSet result = voidPreparedStatementWithQueryAndFunction.getPreparedStatement().executeQuery()) {
                            final ImmutableSet.Builder<DatabaseEntity<Long, Album>> allAlbumsBuilder = ImmutableSet.builder();
                            try {
                                while (result.next()) {

                                    try {
                                        allAlbumsBuilder.add(
                                                new DatabaseEntity.Builder<Long, Album>()
                                                        .setDatabaseReference(new DatabaseReference.Builder<Long>()
                                                                .setReference(result.getLong(ID_COLUMN))
                                                                .build())
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
