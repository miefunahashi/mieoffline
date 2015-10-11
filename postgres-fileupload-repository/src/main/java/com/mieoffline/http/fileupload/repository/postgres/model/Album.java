package com.mieoffline.http.fileupload.repository.postgres.model;

import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class Album implements Value<Album.Builder, Album> {

    private static final long serialVersionUID = -8667474847922941853L;
    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private final String name;
    private final String description;

    private Album(final Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;
        return new EqualsBuilder()
                .append(this.description, album.description)
                .append(this.name, album.name)
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description);
    }

    @Override
    public String toString() {
        return String.format("%s{%s='%s', %s='%s'}",
                Album.class.getName(),
                NAME_FIELD,
                this.name,
                DESCRIPTION_FIELD,
                this.description);
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<Album, Builder> {
        public Builder() {
        }

        public Builder(final Album album) {
            this.name = album.name;
            this.description = album.description;
        }

        private String name;
        private String description;

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        @Override
		public Album build() throws BuilderIncompleteException {
            Album album = new Album(this);
            if (album.name == null) {
                throw BuilderIncompleteException.exception(NAME_FIELD);
            }
            if (this.description == null) {
                throw BuilderIncompleteException.exception(DESCRIPTION_FIELD);
            }
            return album;

        }

        public Builder setDecription(final String description) {
            this.description = description;
            return this;
        }
    }
}
