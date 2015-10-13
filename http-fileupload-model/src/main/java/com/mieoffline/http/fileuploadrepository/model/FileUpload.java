package com.mieoffline.http.fileuploadrepository.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class FileUpload implements Serializable, Value<FileUpload.Builder, FileUpload> {
    public static final String NAME_FIELD = "name";
    private static final long serialVersionUID = 6112291091320356965L;
    public static final String REFERENCE_TO_FILE_UPLOAD_REQUEST_MISSING_DATA_FIELD = "referenceToFileUploadRequestMissingData";
    public static final String FILE_HEADERS_FIELD = "fileHeaders";
    public static final String SIZE_FIELD = "size";
    public static final String CONTENT_TYPE_FIELD = "contentType";
    public static final String FILENAME_FIELD = "filename";
    public static final String DATA_FIELD = "data";
    private final byte[] data;
    private final String filename;
    private final Optional<String> contentType;
    private final Optional<String> name;
    private final Optional<Long> size;
    private final ImmutableMap<String, ImmutableList<String>> fileHeaders;
    private final Long fileUploadId;

    public FileUpload(Builder builder) {
        this.filename = builder.filename;
        this.data = builder.data;
        this.fileUploadId = builder.fileUploadId;
        this.contentType = Optional.ofNullable(builder.contentType);
        this.name = Optional.ofNullable(builder.name);
        this.size = Optional.ofNullable(builder.size);

        this.fileHeaders = builder.fileHeaders;
        if (this.fileHeaders == null) {
            throw new IllegalArgumentException();
        }
    }

    public String getFilename() {
        return this.filename;
    }

    public byte[] getData() {
        return this.data;
    }

    public Optional<String> getContentType() {
        return this.contentType;
    }

    public Optional<String> getName() {
        return this.name;
    }

    public Optional<Long> getSize() {
        return this.size;
    }


    public ImmutableMap<String, ImmutableList<String>> getFileHeaders() {
        return this.fileHeaders;
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

        FileUpload that = (FileUpload) o;

        return new EqualsBuilder()
                .append(this.data, that.data)
                .append(this.filename, that.filename)
                .append(this.contentType, that.contentType)
                .append(this.name, that.name)
                .append(this.size, that.size)
                .append(this.fileHeaders, that.fileHeaders)
                .append(this.fileUploadId, that.fileUploadId)
                .build();


    }

    public Long getFileUploadId() {
        return fileUploadId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data, this.filename, this.contentType, this.name, this.size, this.fileHeaders, this.fileUploadId);

    }

    @Override
    public String toString() {
        return String.format("%s{%s=%s, %s='%s', %s=%s, %s=%s, %s=%s, %s=%s, %s=%s}",
                FileUpload.class.getName(),
                DATA_FIELD, Arrays.toString(this.data),
                FILENAME_FIELD, this.filename,
                CONTENT_TYPE_FIELD, this.contentType,
                NAME_FIELD, this.name,
                SIZE_FIELD, this.size,
                FILE_HEADERS_FIELD, this.fileHeaders,
                REFERENCE_TO_FILE_UPLOAD_REQUEST_MISSING_DATA_FIELD, this.fileUploadId);
    }

    public static class Builder implements Value.Builder<FileUpload, Builder> {
        private Long fileUploadId;
        private String filename;
        private byte[] data;
        private String contentType;
        private String name;
        private Long size;
        private ImmutableMap<String, ImmutableList<String>> fileHeaders;

        public Builder() {
        }


        public Builder(FileUpload databaseFile) {
            this.filename = databaseFile.filename;
            this.data = databaseFile.data;
            this.contentType = databaseFile.contentType.orElse(null);
            this.name = databaseFile.name.orElse(null);
            this.size = databaseFile.size.orElse(null);
            this.fileHeaders = databaseFile.fileHeaders;
            this.fileUploadId = databaseFile.fileUploadId;

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

        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }


        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setFileHeaders(ImmutableMap<String, ImmutableList<String>> fileHeaders) {
            this.fileHeaders = fileHeaders;
            return this;
        }

        public Builder setReferenceToFileUploadRequestMissingData(final Long fileUploadId) {
            this.fileUploadId = fileUploadId;
            return this;
        }

        @Override
        public FileUpload build() throws BuilderIncompleteException {

            final FileUpload fileUpload = new FileUpload(this);
            if (fileUpload.data == null) {
                throw BuilderIncompleteException.exception(DATA_FIELD);
            }
            if (fileUpload.filename == null) {
                throw BuilderIncompleteException.exception(FILENAME_FIELD);
            }
            if (fileUpload.contentType == null) {
                throw BuilderIncompleteException.exception(CONTENT_TYPE_FIELD);
            }
            if (fileUpload.name == null) {
                throw BuilderIncompleteException.exception(NAME_FIELD);
            }
            if (fileUpload.size == null) {
                throw BuilderIncompleteException.exception(SIZE_FIELD);
            }
            if (fileUpload.fileHeaders == null) {
                throw BuilderIncompleteException.exception(FILE_HEADERS_FIELD);
            }
            if (fileUpload.fileUploadId == null) {
                throw BuilderIncompleteException.exception(REFERENCE_TO_FILE_UPLOAD_REQUEST_MISSING_DATA_FIELD);
            }
            return fileUpload;
        }

    }
}
