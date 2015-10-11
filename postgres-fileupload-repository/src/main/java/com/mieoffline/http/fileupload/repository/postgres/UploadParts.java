package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;



public class UploadParts {
    private final String filename;
    private final String contentType;
    private final Boolean isImage;
    private final String name;
    private final String size;
    private final ImmutableMap<String, ImmutableList<String>> fileHeaders;
    private final String referenceToFileUploadRequestMissingData;
    public UploadParts(Builder builder) {
        this.filename = builder.filename;
        if(this.filename == null) {
            throw new IllegalArgumentException();
        }
        this.contentType = builder.contentType;
        if(this.contentType == null) {
            throw new IllegalArgumentException();
        }
        this.name = builder.name;
        if(this.name == null) {
            throw new IllegalArgumentException();
        }
        this.size = builder.size;
        if(this.size == null) {
            throw new IllegalArgumentException();
        }
        this.fileHeaders= builder.fileHeaders;
        if(this.fileHeaders== null) {
            throw new IllegalArgumentException();
        }
        this.referenceToFileUploadRequestMissingData = builder.referenceToFileUploadRequestMissingData;
        if(this.referenceToFileUploadRequestMissingData == null) {
            throw new IllegalArgumentException();
        }
        this.isImage = builder.isImage;
        if(this.isImage == null) {
            throw new IllegalArgumentException();
        }
    }

    public Boolean getIsImage() {
        return this.isImage;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getName() {
        return this.name;
    }

    public String getSize() {
        return this.size;
    }

    public ImmutableMap<String, ImmutableList<String>> getFileHeaders() {
        return this.fileHeaders;
    }

    public String getReferenceToFileUploadRequestMissingData() {
        return this.referenceToFileUploadRequestMissingData;
    }

    public static class Builder {
        private String filename;
        private String contentType;
        private Boolean isImage;
        private String name;
        private String size;
        private ImmutableMap<String, ImmutableList<String>> fileHeaders;
        private String referenceToFileUploadRequestMissingData;
        public Builder() {

        }
        public Builder(UploadParts upload) {
            this.filename =upload.filename;
            this.contentType = upload.contentType;
            this.name = upload.name;
            this.size =upload.size;
            this.fileHeaders = upload.fileHeaders;
            this.referenceToFileUploadRequestMissingData = upload.referenceToFileUploadRequestMissingData;
        }
        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder setIsImage(Boolean isImage) {
            this.isImage = isImage;
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

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder setFileHeaders(ImmutableMap<String, ImmutableList<String>> fileHeaders) {
            this.fileHeaders = fileHeaders;
            return this;
        }

        public Builder setReferenceToFileUploadRequestMissingData(String referenceToFileUploadRequestMissingData) {
            this.referenceToFileUploadRequestMissingData = referenceToFileUploadRequestMissingData;
            return this;
        }

        public UploadParts build() {
            return new UploadParts(this);
        }
    }

}
