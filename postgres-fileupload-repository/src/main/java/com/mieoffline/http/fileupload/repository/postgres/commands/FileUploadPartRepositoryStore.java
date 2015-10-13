package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.model.FileUploadPartRepositoryStoreModel;
import com.mieoffline.http.fileuploadrepository.model.AvroEncoder;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Optional;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.ID;
import static com.mieoffline.http.fileupload.repository.postgres.model.FileUploadPartRepositoryStoreModel.STORE_QUERY;

public class FileUploadPartRepositoryStore
        implements Producer<DatabaseQueryDefinition<FileUploadPartRepositoryStoreModel, Long>, Throwable> {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadPartRepositoryStore.class);
    private final AvroEncoder<Headers> headersAvroHelper;
    private final Utils utils;

    public FileUploadPartRepositoryStore(AvroEncoder<Headers> headersAvroHelper, Utils utils) {
        this.headersAvroHelper = headersAvroHelper;
        this.utils = utils;
    }

    @SuppressWarnings("resource")
    @Override
    public DatabaseQueryDefinition<FileUploadPartRepositoryStoreModel, Long> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<FileUploadPartRepositoryStoreModel, Long>().setQuery(STORE_QUERY)
                .setFunction(fileUploadPartRepositoryStoreModelPreparedStatementWithQueryAndFunction -> {
                    final FileUploadPartRepositoryStoreModel fileUploadPartRepositoryStoreModel = fileUploadPartRepositoryStoreModelPreparedStatementWithQueryAndFunction
                            .getT();
                    final PreparedStatement ps = fileUploadPartRepositoryStoreModelPreparedStatementWithQueryAndFunction
                            .getPreparedStatement();
                    final String nullString = null;
                    final Long fileUploadRequestId = fileUploadPartRepositoryStoreModel.getFileUploadRequestId();
                    final FileUpload databaseFile = fileUploadPartRepositoryStoreModel.getDatabaseFile();
                    final String filename = databaseFile.getFilename();
                    final Optional<String> optionalContentType = databaseFile.getContentType();
                    final Optional<String> optionalName = databaseFile.getName();
                    final Optional<Long> optionalSize = databaseFile.getSize();
                    final byte[] data = databaseFile.getData();
                    final String contentType = optionalContentType.orElse(nullString);
                    final String name = optionalName.orElse(nullString);
                    final Long size = optionalSize.orElse(null);
                    byte[] bytesFromObject = new byte[0];
                    try {
                        bytesFromObject = this.headersAvroHelper
                                .apply(this.utils.mapToHeaders.apply(databaseFile.getFileHeaders()));
                    } catch (AvroEncoder.AvroEncoderException e) {
                        throw new FileUploadPartRepositoryStoreException("Unable to get headers", e);
                    }
                    ps.setLong(1, fileUploadRequestId);
                    ps.setString(2, filename);
                    ps.setBytes(3, data);
                    ps.setString(4, contentType);
                    ps.setString(5, name);
                    ps.setLong(6, size);
                    ps.setBytes(7, bytesFromObject);

                    final int something = ps.executeUpdate();
                    LOGGER.error("" + something);
                    try (final ResultSet resultSet = ps.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            final ResultSetMetaData resultSetMetaData;
                            resultSetMetaData = resultSet.getMetaData();
                            final int columnCount = resultSetMetaData.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                                final String columnName = resultSetMetaData.getColumnName(i);
                                if (ID.equals(columnName)) {
                                    return Long.valueOf(resultSet.getString(i));
                                }
                            }
                            throw new FileUploadPartRepositoryStoreException("No column name found");
                        }
                        throw new FileUploadPartRepositoryStoreException("No column name found");
                    }

                }).build();
    }

    public static class FileUploadPartRepositoryStoreException extends Exception {

        private static final long serialVersionUID = 3803270540761694809L;

        public FileUploadPartRepositoryStoreException(String s, Exception e) {
            super(s, e);
        }

        public FileUploadPartRepositoryStoreException(String s) {
            super(s);
        }
    }

}
