package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableSet;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.model.AlbumItemRepositoryReadGivenIdModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.FILE_UPLOAD_KEY_COLUMN;

public class AlbumItemsListAll implements Producer<DatabaseQueryDefinition<String, ImmutableSet<Long>>, Throwable> {

    private final SQLUtils sqlUtils;

    public AlbumItemsListAll(SQLUtils sqlUtils) {
        this.sqlUtils = sqlUtils;
    }


    @Override
    public DatabaseQueryDefinition<String, ImmutableSet<Long>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<String, ImmutableSet<Long>>()
                .setQuery(AlbumItemRepositoryReadGivenIdModel.READ_ALL_ITEMS_FOR_ALBUM_NAME)
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<String>, ImmutableSet<Long>, Throwable>() {
                    @Override
                    public ImmutableSet<Long> apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<String> stringPreparedStatementWithQueryAndFunction) throws Throwable {
                        final PreparedStatement ps = stringPreparedStatementWithQueryAndFunction.getPreparedStatement();
                        final String longConnectionWithModel = stringPreparedStatementWithQueryAndFunction.getT();
                        ps.setString(1, longConnectionWithModel);
                        try (final ResultSet result = ps.executeQuery()) {
                            final ImmutableSet.Builder<Long> databaseReferenceImmutableSet = ImmutableSet.builder();
                            while (AlbumItemsListAll.this.sqlUtils.isNext(result)) {
                                final Long fileUploadWithoutDataKey = result.getLong(FILE_UPLOAD_KEY_COLUMN);
                                databaseReferenceImmutableSet.add(fileUploadWithoutDataKey);
                            }
                            return databaseReferenceImmutableSet.build();
                        }
                    }
                })
                .build();
    }


    public static class AlbumItemsListAllException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 4405956787405687157L;

        public AlbumItemsListAllException(String s, Exception e) {
            super(s, e);
        }

        public AlbumItemsListAllException(String s) {
            super(s);
        }

        public AlbumItemsListAllException(SQLUtils.SQLUtilsException e) {
            super(e);
        }
    }
}
