package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableList;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.model.FindWithoutDataByFileUploadIdModel;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;

public class FileUploadPartRepositoryReadSimpleFiles implements Producer<DatabaseQueryDefinition<Long, ImmutableList<FileUploadWithoutData>>, Throwable> {

    private final Utils utils;
    private final AvroDecoder<Headers> headersAvroHelper;

    public FileUploadPartRepositoryReadSimpleFiles(Utils utils, AvroDecoder<Headers> headersAvroHelper) {

        this.utils = utils;
        this.headersAvroHelper = headersAvroHelper;
    }


    private boolean isNext(ResultSet result) throws FileUploadPartRepositoryReadSimpleFilesException {
        try {
            return result.next();
        } catch (SQLException e) {
            throw new FileUploadPartRepositoryReadSimpleFilesException("Unable to get next entry in database", e);
        }
    }

    private Headers getHeaders(byte[] headers) throws FileUploadPartRepositoryReadSimpleFilesException {
        final Headers objectFromBytes;
        try {
            objectFromBytes = this.headersAvroHelper.apply(headers);
        } catch (AvroDecoder.AvroDecoderException e) {
            throw new FileUploadPartRepositoryReadSimpleFilesException("Error reading avro encoded map", e);
        }
        return objectFromBytes;
    }


    @Override
    public DatabaseQueryDefinition<Long, ImmutableList<FileUploadWithoutData>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, ImmutableList<FileUploadWithoutData>>()
                .setQuery(FindWithoutDataByFileUploadIdModel.FIND_WITHOUT_DATA_BY_FILE_UPLOAD_ID)
                .setFunction(longPreparedStatementWithQueryAndFunction -> {
                    final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();

                    ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                    final ImmutableList.Builder<FileUploadWithoutData> builder = ImmutableList.<FileUploadWithoutData>builder();
                    try (final ResultSet result = ps.executeQuery()) {
                        while (isNext(result)) {
                            final long id = result.getLong(ID);
                            final byte[] headers = result.getBytes(FILE_HEADERS_COLUMN);
                            final String filename = result.getString(FILENAME_COLUMN);
                            final String name = result.getString(NAME_COLUMN);
                            final String size = result.getString(SIZE_COLUMN);
                            final String contentType = result.getString(CONTENT_TYPE_COLUMN);
                            final Headers objectFromBytes = getHeaders(headers);
                            try {
                                builder.add(new FileUploadWithoutData.Builder()
                                        .setReferenceToUpload(id)
                                        .setFilename(filename)
                                        .setName(name)
                                        .setSize(Long.valueOf(size))
                                        .setContentType(contentType)
                                        .setFileHeaders(headers == null ? null : this.utils.headersToMap.apply(objectFromBytes))
                                        .build());
                            } catch (Value.BuilderIncompleteException e) {
                                throw new FileUploadPartRepositoryReadSimpleFilesException("Unable to build File Upload with data", e);
                            }

                        }
                        return builder.build();

                    }
                })
                .build();
    }


    public static class FileUploadPartRepositoryReadSimpleFilesException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -8516330969552443303L;

        public FileUploadPartRepositoryReadSimpleFilesException(String s, Exception e) {
            super(s, e);
        }
    }
}
