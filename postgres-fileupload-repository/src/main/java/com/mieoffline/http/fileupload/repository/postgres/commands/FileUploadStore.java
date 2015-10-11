package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileuploadrepository.model.AvroEncoder;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadsConstants.*;

public class FileUploadStore implements Producer<DatabaseQueryDefinition<MultipartFormUploadRequestMissingFiles, Long>, Throwable> {
    private final AvroEncoder<Headers> headersAvroHelper;
    private final Utils utils;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadStore.class);
    private final Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<MultipartFormUploadRequestMissingFiles>, Long, Throwable> fileUploadStoreFunction = new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<MultipartFormUploadRequestMissingFiles>, Long, Throwable>() {
        @SuppressWarnings("resource")
		@Override
        public Long apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<MultipartFormUploadRequestMissingFiles> multipartFormUploadRequestMissingFilesPreparedStatementWithQueryAndFunction) throws Throwable {
            final MultipartFormUploadRequestMissingFiles databaseFile = multipartFormUploadRequestMissingFilesPreparedStatementWithQueryAndFunction.getT();
            final PreparedStatement ps = multipartFormUploadRequestMissingFilesPreparedStatementWithQueryAndFunction.getPreparedStatement();
            final ImmutableMap<String, ImmutableList<String>> properties = databaseFile.getProperties();
            final ImmutableMap<String, ImmutableList<String>> uploadHeaders = databaseFile.getUploadHeaders();
            final Headers propertiesHeaders = FileUploadStore.this.utils.mapToHeaders.apply(properties);
            final Headers uploadHeadersObj = FileUploadStore.this.utils.mapToHeaders.apply(uploadHeaders);
            final byte[] propertiesBytes;
            final byte[] uploadHeaderBytes;
            try {
                propertiesBytes = FileUploadStore.this.headersAvroHelper.apply(propertiesHeaders);
                uploadHeaderBytes = FileUploadStore.this.headersAvroHelper.apply(uploadHeadersObj);
            } catch (AvroEncoder.AvroEncoderException e) {
                throw new FileUploadStoreException("Error with avro map", e);
            }
            ps.setBytes(1, propertiesBytes);
            ps.setBytes(2, uploadHeaderBytes);
            final int something = ps.executeUpdate();
            LOGGER.debug("execute update found "+ something);
            try (final ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    final int columnCount = resultSetMetaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        final String columnName = resultSetMetaData.getColumnName(i);
                        if (ID_COLUMN.equals(columnName)) {
                            return Long.valueOf(resultSet.getString(i));
                        }
                    }
                }
				throw new IllegalStateException("No column name found");

            }
        }
    };
    private final static String STORE_QUERY = String.format("INSERT INTO %s(" +
                    "%s," +
                    "%s) VALUES (?, ?);",
            FILE_UPLOAD_REQUESTS_TABLE_NAME,
            FORM_FIELD_VALUES_COLUMN,
            UPLOAD_HEADERS_COLUMN

    );

    public FileUploadStore(AvroEncoder<Headers> headersAvroHelper, Utils utils) {
        this.headersAvroHelper = headersAvroHelper;
        this.utils = utils;
    }


    @Override
    public DatabaseQueryDefinition<MultipartFormUploadRequestMissingFiles, Long> apply(Void aVoid) throws Throwable {
        
        return new DatabaseQueryDefinition.Builder<MultipartFormUploadRequestMissingFiles, Long>()
                .setQuery(STORE_QUERY)
                .setFunction(this.fileUploadStoreFunction)
                .build();
    }


    public static class FileUploadStoreException extends Exception {

		private static final long serialVersionUID = 388613097185416393L;

		public FileUploadStoreException(String s, Exception e) {
            super(s, e);
        }
    }
}
