package com.mieoffline.server.postgres.controllers.base;

import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.FileUploadWithMultipartFormUploadRequestMissingFiles;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.commands.DatabaseFunctionQuery;
import com.mieoffline.http.fileupload.repository.postgres.commands.DatabaseQueryDefinition;
import com.mieoffline.http.fileupload.repository.postgres.commands.FileUploadPartRepositoryReadSimpleFile;
import com.mieoffline.http.fileupload.repository.postgres.commands.FileUploadsReadGivenId;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;


public class GetFileUploadWithMultipartFormUploadRequestMissingFiles implements Function<String, FileUploadWithMultipartFormUploadRequestMissingFiles, Exception> {

    private final IDatabase connectionProducer;
    private final Utils utils;
    private final SQLUtils sqlUtils;
    private final AvroDecoder<Headers> headersAvroHelper;
    final FileUploadPartRepositoryReadSimpleFile fileUploadPartRepositoryReadSimpleFile;

    public GetFileUploadWithMultipartFormUploadRequestMissingFiles(IDatabase connectionProducer, Utils utils, SQLUtils sqlUtils, AvroDecoder<Headers> headersAvroHelper, FileUploadPartRepositoryReadSimpleFile fileUploadPartRepositoryReadSimpleFile) {
        this.connectionProducer = connectionProducer;
        this.utils = utils;
        this.sqlUtils = sqlUtils;
        this.headersAvroHelper = headersAvroHelper;
        this.fileUploadPartRepositoryReadSimpleFile = fileUploadPartRepositoryReadSimpleFile;
    }

    @Override
    public FileUploadWithMultipartFormUploadRequestMissingFiles apply(String s) throws Exception {
        final FileUploadsReadGivenId fileUploadsReadGivenId = new FileUploadsReadGivenId(utils, sqlUtils, headersAvroHelper);
        final DatabaseFunctionQuery<Long, FileUpload> fileUploadQuery;
        try {
            fileUploadQuery = new DatabaseFunctionQuery<>(connectionProducer, fileUploadPartRepositoryReadSimpleFile.apply(null));
        } catch (Throwable throwable) {
            throw new IllegalStateException("Doh", throwable);
        }
        final FileUpload fileUpload = fileUploadQuery.apply(Long.valueOf(s));
        final DatabaseQueryDefinition<Long, MultipartFormUploadRequestMissingFiles> apply;
        try {
            apply = fileUploadsReadGivenId.apply(null);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Doh", throwable);
        }
        final DatabaseFunctionQuery<Long, MultipartFormUploadRequestMissingFiles> objectObjectDatabaseFunctionQuery = new DatabaseFunctionQuery<>(connectionProducer, apply);
        final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles = objectObjectDatabaseFunctionQuery.apply(fileUpload.getFileUploadId());

        return new FileUploadWithMultipartFormUploadRequestMissingFiles.Builder()
                .setFileUpload(fileUpload)
                .setMultipartFormUploadRequestMissingFiles(multipartFormUploadRequestMissingFiles)
                .build();

    }

}