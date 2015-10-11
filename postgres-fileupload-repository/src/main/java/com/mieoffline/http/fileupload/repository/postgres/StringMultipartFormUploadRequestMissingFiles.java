package com.mieoffline.http.fileupload.repository.postgres;

import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class StringMultipartFormUploadRequestMissingFiles implements Value<StringMultipartFormUploadRequestMissingFiles.Builder, StringMultipartFormUploadRequestMissingFiles> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3569926417932966290L;
	public static final String IDENTIFIER_FIELD = "identifier";
    public static final String MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD = "multipartFormUploadRequestMissingFiles";
    private final Long identifier;
    private final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles;

    public StringMultipartFormUploadRequestMissingFiles(Builder builder) {
        this.identifier = builder.identifier;
        this.multipartFormUploadRequestMissingFiles = builder.multipartFormUploadRequestMissingFiles;
    }

    public Long getIdentifier() {
        return this.identifier;
    }

    public MultipartFormUploadRequestMissingFiles getMultipartFormUploadRequestMissingFiles() {
        return this.multipartFormUploadRequestMissingFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringMultipartFormUploadRequestMissingFiles that = (StringMultipartFormUploadRequestMissingFiles) o;
        return new EqualsBuilder()
                .append(this.identifier, that.identifier)
                .append(this.multipartFormUploadRequestMissingFiles, that.multipartFormUploadRequestMissingFiles)
                .build();

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier, this.multipartFormUploadRequestMissingFiles);
    }

    @Override
    public String toString() {
        return String.format("%s{%s=%d, %s=%s}",
                StringMultipartFormUploadRequestMissingFiles.class.getName(),
                IDENTIFIER_FIELD,
                this.identifier,
                MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD,
                this.multipartFormUploadRequestMissingFiles);
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<StringMultipartFormUploadRequestMissingFiles, Builder> {
        private Long identifier;
        private MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles;

        public Builder() {

        }

        public Builder(StringMultipartFormUploadRequestMissingFiles stringMultipartFormUploadRequestMissingFiles) {
            this.identifier = stringMultipartFormUploadRequestMissingFiles.identifier;
            this.multipartFormUploadRequestMissingFiles = stringMultipartFormUploadRequestMissingFiles.multipartFormUploadRequestMissingFiles;
        }

        public Builder setIdentifier(Long identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder setMultipartFormUploadRequestMissingFiles(MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles) {
            this.multipartFormUploadRequestMissingFiles = multipartFormUploadRequestMissingFiles;
            return this;
        }

        @Override
		public StringMultipartFormUploadRequestMissingFiles build() throws Value.BuilderIncompleteException {
            final StringMultipartFormUploadRequestMissingFiles stringMultipartFormUploadRequestMissingFiles = new StringMultipartFormUploadRequestMissingFiles(this);
            if (stringMultipartFormUploadRequestMissingFiles.identifier == null) {
                throw Value.BuilderIncompleteException.exception(IDENTIFIER_FIELD);
            }
            if (stringMultipartFormUploadRequestMissingFiles.multipartFormUploadRequestMissingFiles == null) {
                throw BuilderIncompleteException.exception(MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD);
            }

            return stringMultipartFormUploadRequestMissingFiles;
        }
    }
}
