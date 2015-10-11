package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.model.ReadGivenIdModel;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;

public class FileUploadPartRepositoryReadSimpleFile implements Producer<DatabaseQueryDefinition<Long, FileUpload<Long>>, Throwable> {
    private final AvroDecoder<Headers> headersAvroHelper;
    private final Utils utils;
    private final SQLUtils sqlUtils;

    public FileUploadPartRepositoryReadSimpleFile(AvroDecoder<Headers> headersAvroHelper, Utils utils, SQLUtils sqlUtils) {
        this.headersAvroHelper = headersAvroHelper;
        this.utils = utils;
        this.sqlUtils = sqlUtils;
    }

    private FileUpload<Long> getFileUpload(byte[] headers, String fileName, byte[] content, String name, String size, String contentType, DatabaseReference<Long> build, Headers objectFromBytes) throws FileUploadPartRepositoryReadSimpleFileException {
        final FileUpload<Long> fileUpload;
        try {
            fileUpload = new FileUpload.Builder<Long>()
                    .setReferenceToFileUploadRequestMissingData(build)
                    .setFilename(fileName)
                    .setData(content)
                    .setName(name)
                    .setSize(Long.valueOf(size))
                    .setContentType(contentType)
                    .setFileHeaders(headers == null ? null : this.utils.headersToMap.apply(objectFromBytes))
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new FileUploadPartRepositoryReadSimpleFileException("Cannot build file upload", e);
        }
        return fileUpload;
    }

    private Headers getHeaders(byte[] headers) throws FileUploadPartRepositoryReadSimpleFileException {
        final Headers objectFromBytes;
        try {
            objectFromBytes = this.headersAvroHelper.apply(headers);
        } catch (AvroDecoder.AvroDecoderException e) {
            throw new FileUploadPartRepositoryReadSimpleFileException("Unable to get headers map", e);
        }
        return objectFromBytes;
    }

    private DatabaseReference<Long> getLongDatabaseReference(long fileUploadId) throws FileUploadPartRepositoryReadSimpleFileException {
        final DatabaseReference<Long> build;
        try {
            build = new DatabaseReference.Builder<Long>().setReference(fileUploadId).build();
        } catch (Value.BuilderIncompleteException e) {
            throw new FileUploadPartRepositoryReadSimpleFileException("Couldn't build database reference", e);
        }
        return build;
    }

    @Override
    public DatabaseQueryDefinition<Long, FileUpload<Long>> apply(Void aVoid) throws Throwable {
        return new DatabaseQueryDefinition.Builder<Long, FileUpload<Long>>()
                .setFunction(longPreparedStatementWithQueryAndFunction -> {
                    final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
                    ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
                    try (final ResultSet result = ps.executeQuery()) {

                        if (this.sqlUtils.isNext(result)) {
                            final byte[] headers = result.getBytes(FILE_HEADERS_COLUMN);
                            final long fileUploadId = result.getLong(FILE_UPLOAD_ID);
                            final String fileName = result.getString(FILENAME_COLUMN);
                            final byte[] content = result.getBytes(CONTENT_COLUMN);
                            final String name = result.getString(NAME_COLUMN);
                            final String size = result.getString(SIZE_COLUMN);
                            final String contentType = result.getString(CONTENT_TYPE_COLUMN);
                            final DatabaseReference<Long> build = getLongDatabaseReference(fileUploadId);
                            final Headers objectFromBytes = getHeaders(headers);
                            final FileUpload<Long> fileUpload = getFileUpload(headers, fileName, content, name, size, contentType, build, objectFromBytes);
                            return fileUpload;
                        }
						throw new FileUploadPartRepositoryReadSimpleFileException("No result");

                    }
                })
                .setQuery(ReadGivenIdModel.READ_GIVEN_ID)
                .build();
    }


    public static class FileUploadPartRepositoryReadSimpleFileException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -2011532991907421799L;

		public FileUploadPartRepositoryReadSimpleFileException(String s, Exception e) {
            super(s, e);
        }

        public FileUploadPartRepositoryReadSimpleFileException(String s) {
            super(s);
        }
    }
}
