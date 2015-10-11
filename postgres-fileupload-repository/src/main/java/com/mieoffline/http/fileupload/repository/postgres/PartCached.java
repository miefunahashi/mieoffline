package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class PartCached {
    private final String contentType;
    private final String name;
    private final Long size;
    private final ImmutableMap<String,ImmutableList<String>> headers;
    private final String submittedFileName;
    private final byte[] data;
    private final String alternativeFileName;
    public PartCached(Builder builder) {
        this.contentType = builder.contentType;
        this.name = builder.name;
        this.size = builder.size;
        this.headers = builder.headers;
        this.submittedFileName = builder.submittedFileName;
        this.data = builder.data;
        this.alternativeFileName = builder.alternativeFileName;

    }
    public Builder asBuilder() {
        return new Builder(this);
    }

    public String getAlternativeFileName() {
        return this.alternativeFileName;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getName() {
        return this.name;
    }

    public Long getSize() {
        return this.size;
    }

    public ImmutableMap<String, ImmutableList<String>> getHeaders() {
        return this.headers;
    }

    public String getSubmittedFileName() {
        return this.submittedFileName;
    }

    public static class Builder {
        private String contentType;
        private String name;
        private Long size;
        private ImmutableMap<String,ImmutableList<String>> headers;
        private String submittedFileName;
        private byte[] data;
        public String alternativeFileName;

        public Builder() {}
        public Builder(PartCached partCached) {
            this.contentType = partCached.contentType;
            this.name = partCached.name;
            this.size = partCached.size;
            this.headers = partCached.headers;
            this.submittedFileName = partCached.submittedFileName;
            this.data = partCached.data;
            this.alternativeFileName = partCached.alternativeFileName;

        }

        public Builder setAlternativeFileName(String alternativeFileName) {
            this.alternativeFileName = alternativeFileName;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSize(Long size) {
            this.size = size;
            return this;
        }

        public Builder setHeaders(ImmutableMap<String, ImmutableList<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setSubmittedFileName(String submittedFileName) {
            this.submittedFileName = submittedFileName;
            return this;
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public PartCached build() {
            return new PartCached(this);
        }
    }


}
