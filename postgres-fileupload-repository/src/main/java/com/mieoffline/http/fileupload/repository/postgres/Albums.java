package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableSet;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class Albums implements Value<Albums.Builder,Albums> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8740760744756916455L;
	public static final String ALBUMS_FIELD = "albums";
    private final ImmutableSet<Album> albumSet;
    private Albums(final Builder builder) {
        this.albumSet = builder.albumSet;
    }

    public ImmutableSet<Album> getAlbums() {
        return this.albumSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Albums that = (Albums) o;

        return new EqualsBuilder()
                .append(this.albumSet,that.albumSet)
                .build();
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.albumSet);
    }

    @Override
    public String toString() {
        return String.format("%s{%s=%s}", Albums.class.getName(), ALBUMS_FIELD, this.albumSet);
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<Albums,Builder> {

        public ImmutableSet<Album> albumSet;


        public Builder(Albums albums) {
            this.albumSet = albums.albumSet;
        }

        public Builder() {

        }

        public Builder setAlbums(ImmutableSet<Album> albums) {
            this.albumSet = albums;
            return this;
        }

        @Override
        public Albums build() throws BuilderIncompleteException {
            Albums albums = new Albums(this);
            if(albums.albumSet == null) {
                BuilderIncompleteException.exception(ALBUMS_FIELD);
            }
            return albums;
        }
    }
}
