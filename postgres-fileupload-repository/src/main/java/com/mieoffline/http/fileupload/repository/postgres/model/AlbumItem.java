package com.mieoffline.http.fileupload.repository.postgres.model;

import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

public class AlbumItem implements Serializable, Value<AlbumItem.Builder, AlbumItem> {
    private static final long serialVersionUID = 8901331757740123396L;
    private static final String ALBUM_FIELD = "album";
    private static final String FILE_UPLOAD_WITHOUT_DATA_FIELD = "fileUploadWithoutData";
    private final Long fileUploadWithoutData;
    private final Long album;

    public AlbumItem(Builder builder) {
        this.fileUploadWithoutData = builder.fileUploadWithoutData;
        this.album = builder.album;
    }

    public Long getFileUploadWithoutData() {
        return this.fileUploadWithoutData;
    }

    public Long getAlbum() {
        return this.album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumItem albumItem = (AlbumItem) o;
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
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<AlbumItem, Builder> {
        private Long fileUploadWithoutData;
        private Long album;

        public Builder() {
        }

        public Builder(AlbumItem albumItem) {
            this.fileUploadWithoutData = albumItem.fileUploadWithoutData;
            this.album = albumItem.album;
        }


        public Builder setFileUploadWithoutData(Long fileUploadWithoutData) {
            this.fileUploadWithoutData = fileUploadWithoutData;
            return this;
        }

        public Builder setAlbum(Long album) {
            this.album = album;
            return this;
        }

        @Override
        public AlbumItem build() throws BuilderIncompleteException {
            final AlbumItem albumItem = new AlbumItem(this);
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
