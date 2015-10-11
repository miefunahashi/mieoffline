package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableList;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class Uploads implements Value<Uploads.Builder, Uploads> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8761075275118562879L;
	private final ImmutableList<Upload> uploads;

    private Uploads(Builder builder) {
        this.uploads = builder.uploads;
    }

    public ImmutableList<Upload> getUploads() {
        return this.uploads;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }
    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uploads uploads1 = (Uploads) o;
        return new EqualsBuilder().append(this.uploads, uploads1.uploads).build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uploads);
    }

    public static class Builder implements Value.Builder<Uploads, Builder> {
        private ImmutableList<Upload> uploads;

        public Builder() {

        }

        public Builder(Uploads uploads) {
            this.uploads = uploads.uploads;
        }

        public Builder setUploads(ImmutableList<Upload> uploads) {
            this.uploads = uploads;
            return this;
        }

        @Override
		public Uploads build() throws BuilderIncompleteException {

            Uploads value = new Uploads(this);
            if (value.uploads == null) {
                throw new Value.BuilderIncompleteException.Builder()
                        .setMissingField("Uploads")
                        .build();
            }
            return value;
        }
    }
}
