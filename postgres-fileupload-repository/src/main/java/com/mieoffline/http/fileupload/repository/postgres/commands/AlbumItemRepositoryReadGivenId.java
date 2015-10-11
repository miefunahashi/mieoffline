package com.mieoffline.http.fileupload.repository.postgres.commands;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.ALBUM_KEY_COLUMN;
import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.FILE_UPLOAD_KEY_COLUMN;
import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.ID_COLUMN;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.markoffline.site.database.model.DatabaseEntity;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.model.AlbumItem;
import com.mieoffline.site.Value;

public class AlbumItemRepositoryReadGivenId implements Producer<DatabaseQueryDefinition<Long, DatabaseEntity<Long, AlbumItem<Long>>>, Throwable> {
    private final SQLUtils sqlUtils;

    public AlbumItemRepositoryReadGivenId(SQLUtils sqlUtils) {
        this.sqlUtils = sqlUtils;
    }


    @Override
    public DatabaseQueryDefinition<Long, DatabaseEntity<Long, AlbumItem<Long>>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, DatabaseEntity<Long, AlbumItem<Long>>>()
                .setFunction(longPreparedStatementWithQueryAndFunction -> {
                    final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                    ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                    try (final ResultSet result = ps.executeQuery()) {
                        if (this.sqlUtils.isNext(result)) {
                            final long idValue = result.getLong(ID_COLUMN);
                            final Long albumKey = result.getLong(ALBUM_KEY_COLUMN);
                            final Long fileUploadWithoutDataKey = result.getLong(FILE_UPLOAD_KEY_COLUMN);
                            try {
                                final DatabaseReference<Long> databaseRef = new DatabaseReference.Builder<Long>().setReference(idValue).build();
                                final DatabaseReference<Long> album = new DatabaseReference.Builder<Long>().setReference(albumKey).build();
                                final DatabaseReference<Long> fileUploadWithoutData = new DatabaseReference.Builder<Long>().setReference(fileUploadWithoutDataKey).build();
                                return new DatabaseEntity.Builder<Long, AlbumItem<Long>>().setObject(
                                        new AlbumItem.Builder<Long>()
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
