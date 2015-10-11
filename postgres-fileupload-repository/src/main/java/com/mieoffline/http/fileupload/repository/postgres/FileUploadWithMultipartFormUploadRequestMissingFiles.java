package com.mieoffline.http.fileupload.repository.postgres;

import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

public class FileUploadWithMultipartFormUploadRequestMissingFiles<T extends Serializable> implements Value<FileUploadWithMultipartFormUploadRequestMissingFiles.Builder<T>,FileUploadWithMultipartFormUploadRequestMissingFiles<T>> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8146157868815913189L;
	public static final String MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD = "multipartFormUploadRequestMissingFiles";
    public static final String FILE_UPLOAD_FIELD = "fileUpload";
    private final FileUpload<T> fileUpload;
    private final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles;

    public FileUploadWithMultipartFormUploadRequestMissingFiles(Builder<T> builder) {
        this.fileUpload = builder.fileUpload;
        this.multipartFormUploadRequestMissingFiles = builder.multipartFormUploadRequestMissingFiles;
    }

    public FileUpload<T> getFileUpload() {
        return this.fileUpload;
    }

    public MultipartFormUploadRequestMissingFiles getMultipartFormUploadRequestMissingFiles() {
        return this.multipartFormUploadRequestMissingFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileUploadWithMultipartFormUploadRequestMissingFiles<?> that = (FileUploadWithMultipartFormUploadRequestMissingFiles<?>) o;
        return new EqualsBuilder()
                .append(this.fileUpload, that.fileUpload)
                .append(this.multipartFormUploadRequestMissingFiles,that.multipartFormUploadRequestMissingFiles)
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fileUpload,this.multipartFormUploadRequestMissingFiles);
    }

    @Override
    public String toString() {
        return String.format("%s{%s=%s, %s=%s}", FileUploadWithMultipartFormUploadRequestMissingFiles.class.getName(), FILE_UPLOAD_FIELD, this.fileUpload, MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD, this.multipartFormUploadRequestMissingFiles);
    }

    @Override
    public Builder<T> newBuilder() {
        return new Builder<>();
    }

    @Override
    public Builder<T> asBuilder() {
        return new Builder<>(this);
    }

    public static class Builder<T extends Serializable> implements Value.Builder<FileUploadWithMultipartFormUploadRequestMissingFiles<T>,FileUploadWithMultipartFormUploadRequestMissingFiles.Builder<T>> {
        private FileUpload<T> fileUpload;
        private  MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles;
        public Builder() {

        }
        public Builder(FileUploadWithMultipartFormUploadRequestMissingFiles<T> fileUploadWithMultipartFormUploadRequestMissingFiles) {
            this.fileUpload = fileUploadWithMultipartFormUploadRequestMissingFiles.fileUpload;
            this.multipartFormUploadRequestMissingFiles = fileUploadWithMultipartFormUploadRequestMissingFiles.multipartFormUploadRequestMissingFiles;
        }

        public Builder<T> setFileUpload(FileUpload<T> fileUpload) {
            this.fileUpload = fileUpload;
            return this;
        }

        public Builder<T> setMultipartFormUploadRequestMissingFiles(MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles) {
            this.multipartFormUploadRequestMissingFiles = multipartFormUploadRequestMissingFiles;
            return this;
        }

        @Override
        public FileUploadWithMultipartFormUploadRequestMissingFiles<T> build() throws BuilderIncompleteException {
            FileUploadWithMultipartFormUploadRequestMissingFiles<T> fileUploadWithMultipartFormUploadRequestMissingFiles = new FileUploadWithMultipartFormUploadRequestMissingFiles<>(this);
            if(fileUploadWithMultipartFormUploadRequestMissingFiles.fileUpload == null) {
                BuilderIncompleteException.exception(FILE_UPLOAD_FIELD);
            }
            if(fileUploadWithMultipartFormUploadRequestMissingFiles.multipartFormUploadRequestMissingFiles == null) {
                BuilderIncompleteException.exception(MULTIPART_FORM_UPLOAD_REQUEST_MISSING_FILES_FIELD);
            }
            return fileUploadWithMultipartFormUploadRequestMissingFiles;
        }
    }
}
