package com.mieoffline.http.fileupload.repository.postgres.model;

import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

public class AlbumItem<DATA_TYPE extends Serializable> implements Serializable, Value<AlbumItem.Builder<DATA_TYPE>, AlbumItem<DATA_TYPE>> {
    private static final long serialVersionUID = 8901331757740123396L;
    private static final String ALBUM_FIELD = "album";
    private static final String FILE_UPLOAD_WITHOUT_DATA_FIELD = "fileUploadWithoutData";
    private final DatabaseReference<DATA_TYPE> fileUploadWithoutData;
    private final DatabaseReference<DATA_TYPE> album;

    public AlbumItem(Builder<DATA_TYPE> builder) {
        this.fileUploadWithoutData = builder.fileUploadWithoutData;
        this.album = builder.album;
    }

    public DatabaseReference<DATA_TYPE> getFileUploadWithoutData() {
        return this.fileUploadWithoutData;
    }

    public DatabaseReference<DATA_TYPE> getAlbum() {
        return this.album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumItem<?> albumItem = (AlbumItem<?>) o;
        return new EqualsBuilder()
                .append(this.album, albumItem.album)
                .append(this.fileUploadWithoutData, albumItem.fileUploadWithoutData)
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fileUploadWithoutData, this.album);
    }

    @Override
    public String toString() {
        return String.format("%s{, %s=%s, %s=%s}", AlbumItem.class.getName(), FILE_UPLOAD_WITHOUT_DATA_FIELD, this.fileUploadWithoutData, ALBUM_FIELD, this.album);
    }

    @Override
    public Builder<DATA_TYPE> newBuilder() {
        return new Builder<>();
    }

    @Override
    public Builder<DATA_TYPE> asBuilder() {
        return new Builder<>(this);
    }

    public static class Builder<DATA_TYPE extends Serializable> implements Value.Builder<AlbumItem<DATA_TYPE>, Builder<DATA_TYPE>> {
        private DatabaseReference<DATA_TYPE> fileUploadWithoutData;
        private DatabaseReference<DATA_TYPE> album;

        public Builder() {
        }

        public Builder(AlbumItem<DATA_TYPE> albumItem) {
            this.fileUploadWithoutData = albumItem.fileUploadWithoutData;
            this.album = albumItem.album;
        }


        public Builder<DATA_TYPE> setFileUploadWithoutData(DatabaseReference<DATA_TYPE> fileUploadWithoutData) {
            this.fileUploadWithoutData = fileUploadWithoutData;
            return this;
        }

        public Builder<DATA_TYPE> setAlbum(DatabaseReference<DATA_TYPE> album) {
            this.album = album;
            return this;
        }

        @Override
		public AlbumItem<DATA_TYPE> build() throws BuilderIncompleteException {
            final AlbumItem<DATA_TYPE> albumItem = new AlbumItem<>(this);
            if (albumItem.album == null) {
                throw BuilderIncompleteException.exception(ALBUM_FIELD);
            }
            if (albumItem.fileUploadWithoutData == null) {
                throw BuilderIncompleteException.exception(FILE_UPLOAD_WITHOUT_DATA_FIELD);
            }
            return albumItem;
        }
    }
}
