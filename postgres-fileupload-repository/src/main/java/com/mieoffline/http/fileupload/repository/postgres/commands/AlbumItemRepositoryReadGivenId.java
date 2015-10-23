package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.model.AlbumItem;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.*;

public class AlbumItemRepositoryReadGivenId implements Producer<DatabaseQueryDefinition<Long, DatabaseEntity<AlbumItem>>, Throwable> {
    private final SQLUtils sqlUtils;

    public AlbumItemRepositoryReadGivenId(SQLUtils sqlUtils) {
        this.sqlUtils = sqlUtils;
    }


    @Override
    public DatabaseQueryDefinition<Long, DatabaseEntity<AlbumItem>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, DatabaseEntity<AlbumItem>>()
                .setFunction(longPreparedStatementWithQueryAndFunction -> {
                    final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                    ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                    try (final ResultSet result = ps.executeQuery()) {
                        if (this.sqlUtils.isNext(result)) {
                            final long idValue = result.getLong(ID_COLUMN);
                            final Long albumKey = result.getLong(ALBUM_KEY_COLUMN);
                            final Long fileUploadWithoutDataKey = result.getLong(FILE_UPLOAD_KEY_COLUMN);
                            try {
                                final Long databaseRef = idValue;
                                final Long album = albumKey;
                                final Long fileUploadWithoutData = fileUploadWithoutDataKey;
                                return new DatabaseEntity.Builder<AlbumItem>().setObject(
                                        new AlbumItem.Builder()
                                                .setAlbum(album)
                                                .setFileUploadWithoutData(fileUploadWithoutData)
                                                .build())
                                        .setDatabaseReference(databaseRef).build();
                            } catch (Value.BuilderIncompleteException e) {
                                throw new AlbumItemRepositoryReadGivenIdException("Unable to build the album item", e);
                            }
                        }
                        throw new AlbumItemRepositoryReadGivenIdException("Unable to find a result");
                    }
                })
                .build();

    }


    public static class AlbumItemRepositoryReadGivenIdException extends Exception {

        private static final long serialVersionUID = -5827535312163643964L;

        public AlbumItemRepositoryReadGivenIdException(String s, Exception e) {
            super(s, e);
        }

        public AlbumItemRepositoryReadGivenIdException(String s) {
            super(s);
        }

        public AlbumItemRepositoryReadGivenIdException(SQLUtils.SQLUtilsException e) {
            super(e);
        }
    }
}
