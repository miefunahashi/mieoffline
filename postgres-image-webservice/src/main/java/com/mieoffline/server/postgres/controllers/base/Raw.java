package com.mieoffline.server.postgres.controllers.base;

import com.google.common.collect.ImmutableList;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.*;
import com.mieoffline.http.fileupload.repository.postgres.commands.*;
import com.mieoffline.http.fileupload.repository.postgres.converter.FileUploadWithoutDataToUploadParts;
import com.mieoffline.http.fileupload.repository.postgres.json.JsonHelper;
import com.mieoffline.http.fileupload.repository.postgres.json.to.BooleanToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.UploadPartsToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.UploadToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.UploadsToNode;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.AvroEncoder;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.json.Node;
import com.mieoffline.server.ResourceFileGetter;
import com.mieoffline.server.ResourceListGetter;
import com.mieoffline.server.postgres.MapFromNodeToJsonString;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.server.services.ResourceController;
import com.mieoffline.server.services.ResourceGetter;
import com.mieoffline.server.services.ResourcePoster;

public class Raw implements Producer<Consumer<HttpServletRequestResponseWrapper, ?>, Raw.RawException> {
    private final IDatabase iDatabase;
    private final MapFromNodeToJsonString serializer;
    final AvroDecoder<Headers> headersAvroDecoder;
    final Utils utils;
    final SQLUtils sqlUtils;

    public Raw(final IDatabase iDatabase, MapFromNodeToJsonString serializer, AvroDecoder<Headers> headersAvroDecoder, Utils utils, SQLUtils sqlUtils) {
        this.iDatabase = iDatabase;

        this.serializer = serializer;
        this.headersAvroDecoder = headersAvroDecoder;
        this.utils = utils;
        this.sqlUtils = sqlUtils;
    }


    @Override
    public Consumer<HttpServletRequestResponseWrapper, ?> apply(Void avoid) throws RawException {
        final AvroEncoder<Headers> headersAvroEncoder = new AvroEncoder<>(Headers.class);

        final FileUploadsReadAll fileUploadsReadAll = new FileUploadsReadAll(this.headersAvroDecoder, this.utils);


        final BooleanToNode booleanToNode = new BooleanToNode();
        final JsonHelper jsonHelper = new JsonHelper();
        final UploadPartsToNode uploadPartsToNode = new UploadPartsToNode(booleanToNode, jsonHelper);
        final AlbumItemRepositoryStore albumItemRepositoryStore = new AlbumItemRepositoryStore();
        final FileUploadPartRepositoryStore fileUploadPartRepositoryStore = new FileUploadPartRepositoryStore(headersAvroEncoder, this.utils);

        final UploadToNode uploadToNode = new UploadToNode(uploadPartsToNode, jsonHelper);
        final FileUploadStore fileUploadStore = new FileUploadStore(headersAvroEncoder, this.utils);

        final UploadsToNode uploadsToNode = new UploadsToNode(uploadToNode);
        final ResourcePosterModelStore resourcePosterModelConsumer;
        try {
            resourcePosterModelConsumer = new ResourcePosterModelStore(fileUploadStore.apply(null), fileUploadPartRepositoryStore.apply(null), albumItemRepositoryStore.apply(null), this.iDatabase);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Whatever", throwable);
        }

        final FileUploadPartRepositoryReadSimpleFiles fileUploadPartRepositoryReadSimpleFiles = new FileUploadPartRepositoryReadSimpleFiles(this.utils, this.headersAvroDecoder);
        final DatabaseQueryDefinition<Long, ImmutableList<FileUploadWithoutData>> fileUploadPartRepositoryReadQueryDef;
        try {
            fileUploadPartRepositoryReadQueryDef = fileUploadPartRepositoryReadSimpleFiles.apply(null);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Whatever", throwable);
        }
        final DatabaseFunctionQuery<Long, ImmutableList<FileUploadWithoutData>> fileUploadPartRepositoryReadSimpleFilesFunc = new DatabaseFunctionQuery<>(this.iDatabase, fileUploadPartRepositoryReadQueryDef);


        final FileUploadWithoutDataToUploadParts fileUploadWithoutDataToUploadParts = new FileUploadWithoutDataToUploadParts();
        final UploadsProducer uploadsProducer;
        try {
            final DatabaseQueryDefinition<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>> something = fileUploadsReadAll.apply(null);
            final DatabaseFunctionQuery<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>> something2 = new DatabaseFunctionQuery<>(this.iDatabase, something);
            uploadsProducer = new UploadsProducer(fileUploadPartRepositoryReadSimpleFilesFunc, something2, fileUploadWithoutDataToUploadParts);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Whatever", throwable);
        }
        final Producer<String, ?> getUploadsAsJsonString = avoid1 -> {
            final ImmutableList<Upload> produce = uploadsProducer.apply(null);
            final Uploads uploads = new Uploads.Builder().setUploads(produce).build();
            final Node apply = uploadsToNode.apply(uploads);
            return Raw.this.serializer.apply(apply);
        };

        final ResourceListGetter resourceListGetter = new ResourceListGetter(getUploadsAsJsonString);
        final FileUploadPartRepositoryReadSimpleFile fileUploadPartRepositoryReadSimpleFile = new FileUploadPartRepositoryReadSimpleFile(this.headersAvroDecoder, this.utils, this.sqlUtils);
        final ResourceFileGetter resourceFileGetter = new ResourceFileGetter(new GetFileUploadWithMultipartFormUploadRequestMissingFiles(iDatabase, utils, sqlUtils, headersAvroDecoder, fileUploadPartRepositoryReadSimpleFile));
        return new ResourceController(new ResourceGetter(resourceListGetter, resourceFileGetter), new ResourcePoster(resourcePosterModelConsumer));
    }

    public static class RawException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -7290700024841059407L;

        public RawException(String a, Throwable throwable) {
            super(a, throwable);
        }
    }
}