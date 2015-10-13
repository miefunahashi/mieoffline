package com.mieoffline.http.fileupload.repository.postgres.model;

import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.site.Value;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;

public class FileUploadPartRepositoryStoreModel implements Value<FileUploadPartRepositoryStoreModel.Builder, FileUploadPartRepositoryStoreModel> {

    private static final long serialVersionUID = 7609974540461638215L;
    public final static String STORE_QUERY = String.format("INSERT INTO %s(" +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s) VALUES (?,? , ?, ?, ?, ?, ?);",
            FILE_UPLOAD_PART_TABLE_NAME,
            FILE_UPLOAD_ID,
            FILENAME_COLUMN,
            CONTENT_COLUMN,
            CONTENT_TYPE_COLUMN,
            NAME_COLUMN,
            SIZE_COLUMN,
            FILE_HEADERS_COLUMN);
    private final FileUpload databaseFile;
    private final Long fileUploadRequestId;

    public FileUploadPartRepositoryStoreModel(Builder builder) {
        this.databaseFile = builder.databaseFile;
        this.fileUploadRequestId = builder.fileUploadRequestId;
    }

    public FileUpload getDatabaseFile() {
        return this.databaseFile;
    }

    public Long getFileUploadRequestId() {
        return this.fileUploadRequestId;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<FileUploadPartRepositoryStoreModel, Builder> {
        private FileUpload databaseFile;
        private Long fileUploadRequestId;

        public Builder(FileUploadPartRepositoryStoreModel fileUploadPartRepositoryStoreModel) {
            this.databaseFile = fileUploadPartRepositoryStoreModel.databaseFile;
            this.fileUploadRequestId = fileUploadPartRepositoryStoreModel.fileUploadRequestId;
        }

        public Builder() {

        }

        public Builder setDatabaseFile(FileUpload databaseFile) {
            this.databaseFile = databaseFile;
            return this;
        }

        public Builder setFileUploadRequestId(Long fileUploadRequestId) {
            this.fileUploadRequestId = fileUploadRequestId;
            return this;
        }

        @Override
        public FileUploadPartRepositoryStoreModel build() throws BuilderIncompleteException {
            FileUploadPartRepositoryStoreModel fileUploadPartRepositoryStoreModel = new FileUploadPartRepositoryStoreModel(this);
            if (fileUploadPartRepositoryStoreModel.databaseFile == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("databaseFile").build();
            }
            if (fileUploadPartRepositoryStoreModel.fileUploadRequestId == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("fileUploadRequestId").build();
            }
            return fileUploadPartRepositoryStoreModel;
        }
    }
}
