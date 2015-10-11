package com.mieoffline.http.fileupload.repository.postgres.commands;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadsConstants.*;
public class FileUploadsReadGivenId implements Producer<DatabaseQueryDefinition<Long,MultipartFormUploadRequestMissingFiles>,Throwable> {
    private final Utils utils;
    private final SQLUtils sqlUtils;
    private final AvroDecoder<Headers> headersAvroHelper;
    private final static String READ_GIVEN_ID = String.format(
            "SELECT %s," +
                    "%s " +
                    "FROM %s " +
                    "WHERE %s = ?",
            FORM_FIELD_VALUES_COLUMN,
            UPLOAD_HEADERS_COLUMN,
            FILE_UPLOAD_REQUESTS_TABLE_NAME,
            ID_COLUMN);

    public FileUploadsReadGivenId(Utils utils, SQLUtils sqlUtils, AvroDecoder<Headers> headersAvroHelper) {
        this.utils = utils;
        this.sqlUtils = sqlUtils;
        this.headersAvroHelper = headersAvroHelper;
    }

    @Override
    public DatabaseQueryDefinition<Long, MultipartFormUploadRequestMissingFiles> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, MultipartFormUploadRequestMissingFiles>()
                .setFunction(new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long>, MultipartFormUploadRequestMissingFiles, Throwable>() {
                    @Override
                    public MultipartFormUploadRequestMissingFiles apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long> longPreparedStatementWithQueryAndFunction) throws Throwable {
                        final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                        final Long id = longPreparedStatementWithQueryAndFunction.getT();
                        ps.setLong(1, id);
                        try (final ResultSet result  = ps.executeQuery()) {
                            if (FileUploadsReadGivenId.this.sqlUtils.isNext(result)) {
                                final byte[] formFieldBytes= result.getBytes(FORM_FIELD_VALUES_COLUMN);
                                final byte[] uploadHeaderBytes= result.getBytes(UPLOAD_HEADERS_COLUMN);

                                final Headers formFieldHeaders;
                                final Headers uploadsHeaders;
                                try {
                                    formFieldHeaders = FileUploadsReadGivenId.this.headersAvroHelper.apply(formFieldBytes);
                                    uploadsHeaders = FileUploadsReadGivenId.this.headersAvroHelper.apply(uploadHeaderBytes);
                                } catch (AvroDecoder.AvroDecoderException e) {
                                    throw new FileUploadsReadGivenIdException("Error deserializing headers", e);
                                }
                                final ImmutableMap<String, ImmutableList<String>> formFieldHeadersMap = FileUploadsReadGivenId.this.utils.headersToMap.apply(formFieldHeaders);
                                final ImmutableMap<String, ImmutableList<String>> uploadHearsMap = FileUploadsReadGivenId.this.utils.headersToMap.apply(uploadsHeaders);
                                try {
                                    return new MultipartFormUploadRequestMissingFiles.Builder()
                                            .setProperties(formFieldHeadersMap)
                                            .setUploadHeaders(uploadHearsMap)
                                            .build();
                                } catch (Value.BuilderIncompleteException e) {
                                    throw new FileUploadsReadGivenIdException("Unable to build multipart form upload request missing files",e);
                                }

                            }
							throw new FileUploadsReadGivenIdException("Missing Result");
                        }
                    }
                })
                .setQuery(READ_GIVEN_ID)
                .build();
    }


    public static class FileUploadsReadGivenIdException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -1445212899001562893L;

		public FileUploadsReadGivenIdException(String s, Exception e) {
            super(s,e);
        }

        public FileUploadsReadGivenIdException(String s) {
            super(s);
        }
    }
}
