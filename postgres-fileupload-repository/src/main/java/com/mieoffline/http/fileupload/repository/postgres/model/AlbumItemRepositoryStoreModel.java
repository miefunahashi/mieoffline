package com.mieoffline.http.fileupload.repository.postgres.model;

import com.mieoffline.site.Value;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.*;

public class AlbumItemRepositoryStoreModel implements Value<AlbumItemRepositoryStoreModel.Builder,AlbumItemRepositoryStoreModel>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2470247196521336733L;
	private final Long albumItemKey;
    private final Long fileUploadKey;

    public AlbumItemRepositoryStoreModel(Builder builder) {
        this.albumItemKey = builder.albumItemKey;
        this.fileUploadKey = builder.fileUploadKey;
    }

    public Long getAlbumItemKey() {
        return this.albumItemKey;
    }

    public Long getFileUploadKey() {
        return this.fileUploadKey;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }


    public static class Builder implements Value.Builder<AlbumItemRepositoryStoreModel,Builder> {
        private Long albumItemKey;
        private Long fileUploadKey;

        public Builder(AlbumItemRepositoryStoreModel albumItemRepositoryStoreModel) {
            this.albumItemKey = albumItemRepositoryStoreModel.albumItemKey;
            this.fileUploadKey = albumItemRepositoryStoreModel.fileUploadKey;
        }

        public Builder() {

        }

        public Builder setAlbumItemKey(Long albumItemKey) {
            this.albumItemKey = albumItemKey;
            return this;
        }

        public Builder setFileUploadKey(Long fileUploadKey) {
            this.fileUploadKey = fileUploadKey;
            return this;
        }

        @Override
        public AlbumItemRepositoryStoreModel build() throws BuilderIncompleteException {
            return new AlbumItemRepositoryStoreModel(this);
        }
    }
    public final static String STORE_QUERY = String.format("INSERT INTO %s(" +
                    "%s," +
                    "%s) VALUES ( ?, ?);",
            ALBUM_ITEMS_TABLE,
            ALBUM_KEY_COLUMN,
            FILE_UPLOAD_KEY_COLUMN

    );
}
