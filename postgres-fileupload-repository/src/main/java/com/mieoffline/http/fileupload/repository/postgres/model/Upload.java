package com.mieoffline.http.fileupload.repository.postgres.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.site.Value;

public class Upload implements Value<Upload.Builder, Upload> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5843381096601742866L;
	private final ImmutableMap<String, ImmutableList<String>> httpHeaders, properties;
    private final ImmutableList<UploadParts> parts;

    public Upload(Builder builder) {
        this.httpHeaders = builder.httpHeaders;
        if (this.httpHeaders == null) {
            throw new IllegalArgumentException();
        }
        this.properties = builder.properties;
        if (this.properties == null) {
            throw new IllegalArgumentException();
        }
        this.parts = builder.uploads;
        if (this.parts == null) {
            throw new IllegalArgumentException();
        }
    }

    public ImmutableMap<String, ImmutableList<String>> getHttpHeaders() {
        return this.httpHeaders;
    }

    public ImmutableMap<String, ImmutableList<String>> getProperties() {
        return this.properties;
    }

    public ImmutableList<UploadParts> getParts() {
        return this.parts;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<Upload, Builder> {
        private ImmutableList<UploadParts> uploads;
        private ImmutableMap<String, ImmutableList<String>> httpHeaders, properties;

        public Builder() {

        }

        public Builder(Upload uploads) {
            this.uploads = uploads.parts;
            this.httpHeaders = uploads.httpHeaders;
            this.properties = uploads.properties;
        }

        public Builder setUploads(ImmutableList<UploadParts> uploads) {
            this.uploads = uploads;
            return this;
        }

        public Builder setHttpHeaders(ImmutableMap<String, ImmutableList<String>> httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        public Builder setProperties(ImmutableMap<String, ImmutableList<String>> properties) {
            this.properties = properties;
            return this;
        }

        @Override
		public Upload build() throws BuilderIncompleteException {
            Upload upload = new Upload(this);
            if (upload.httpHeaders == null) {
                throw new Value.BuilderIncompleteException.Builder().setMissingField("httpHeaders").build();
            }
            if (upload.properties == null) {
                throw new Value.BuilderIncompleteException.Builder().setMissingField("properties").build();
            }
            if (upload.parts == null) {
                throw new Value.BuilderIncompleteException.Builder().setMissingField("parts").build();
            }
            return upload;
        }
    }


}
