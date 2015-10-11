package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableList;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.StringMultipartFormUploadRequestMissingFiles;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;

import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadsConstants.*;

public class FileUploadsReadAll implements Producer<DatabaseQueryDefinition<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>>, Throwable> {
    private final AvroDecoder<Headers> headersAvroHelper;
    private final Utils utils;
    private final static String READ_ALL = String.format(
            "SELECT %s," +
                    "%s, " +
                    "%s " +
                    "FROM %s ",
            ID_COLUMN,
            FORM_FIELD_VALUES_COLUMN,
            UPLOAD_HEADERS_COLUMN,
            FILE_UPLOAD_REQUESTS_TABLE_NAME
    );

    public FileUploadsReadAll(AvroDecoder<Headers> headersAvroHelper, Utils utils) {
        this.headersAvroHelper = headersAvroHelper;
        this.utils = utils;
    }


    @Override
    public DatabaseQueryDefinition<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>>()
                .setFunction(voidPreparedStatementWithQueryAndFunction -> {
                    ImmutableList.Builder<StringMultipartFormUploadRequestMissingFiles> multipartFormUploadRequestMissingFilesBuilder =
                            ImmutableList.<StringMultipartFormUploadRequestMissingFiles>builder();
                    try (final ResultSet resultSet = voidPreparedStatementWithQueryAndFunction.getPreparedStatement().executeQuery()) {
                        while (resultSet.next()) {
                            final long aLong = resultSet.getLong(ID_COLUMN);
                            final byte[] bytes = resultSet.getBytes(FORM_FIELD_VALUES_COLUMN);
                            final byte[] bytes1 = resultSet.getBytes(UPLOAD_HEADERS_COLUMN);
                            final Headers objectFromBytes, objectFromBytes1;
                            try {
                                objectFromBytes = this.headersAvroHelper.apply(bytes);
                                objectFromBytes1 = this.headersAvroHelper.apply(bytes1);
                            } catch (AvroDecoder.AvroDecoderException e) {
                                throw new FileUploadsReadAllException("Error reading avro encoded map", e);
                            }
                            try {
                                multipartFormUploadRequestMissingFilesBuilder.add(new StringMultipartFormUploadRequestMissingFiles.Builder()
                                        .setIdentifier(aLong)
                                        .setMultipartFormUploadRequestMissingFiles(new MultipartFormUploadRequestMissingFiles.Builder()
                                                .setProperties(this.utils.headersToMap.apply(objectFromBytes))
                                                .setUploadHeaders(this.utils.headersToMap.apply(objectFromBytes1))
                                                .build()).build());
                            } catch (Value.BuilderIncompleteException e) {
                                throw new FileUploadsReadAllException("Unable to build string multipart form upload request (without metadata", e);
                            }
                        }
                        return multipartFormUploadRequestMissingFilesBuilder.build();
                    }
                })
                .setQuery(READ_ALL)
                .build();
    }


    public static class FileUploadsReadAllException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -8564677508442947743L;

		public FileUploadsReadAllException(String s, Exception e) {
            super(s, e);
        }
    }
}
