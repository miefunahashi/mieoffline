package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.PartCached;
import com.mieoffline.http.fileupload.repository.postgres.model.AlbumItemRepositoryStoreModel;
import com.mieoffline.http.fileupload.repository.postgres.model.FileUploadPartRepositoryStoreModel;
import com.mieoffline.http.fileupload.repository.postgres.model.ResourcePosterModel;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ResourcePosterModelStore implements Consumer<ResourcePosterModel, Throwable> {
    private final DatabaseQueryDefinition<MultipartFormUploadRequestMissingFiles, Long> fileUploadStore;
    private final DatabaseQueryDefinition<FileUploadPartRepositoryStoreModel, Long> fileUploadPartRepositoryStore;
    private final DatabaseQueryDefinition<AlbumItemRepositoryStoreModel, Long> albumItemRepositoryStore;
    private final IDatabase connectionProducer;

    public ResourcePosterModelStore(DatabaseQueryDefinition<MultipartFormUploadRequestMissingFiles, Long> fileUploadStore, DatabaseQueryDefinition<FileUploadPartRepositoryStoreModel, Long> fileUploadPartRepositoryStore, DatabaseQueryDefinition<AlbumItemRepositoryStoreModel, Long> albumItemRepositoryStore, IDatabase connectionProducer) {
        this.fileUploadStore = fileUploadStore;
        this.fileUploadPartRepositoryStore = fileUploadPartRepositoryStore;
        this.albumItemRepositoryStore = albumItemRepositoryStore;
        this.connectionProducer = connectionProducer;
    }


    public ImmutableList<FileUpload> getSimpleFiles(
            Long idMultipartFormUploadRequestMissingFiles,
            final ImmutableList<PartCached> partsToStore) throws ResourcePosterModelStoreException {

        final ImmutableList.Builder<FileUpload> simpleFileBuilder = ImmutableList.builder();
        for (PartCached part : partsToStore) {
            try {
                simpleFileBuilder.add(new FileUpload.Builder()
                        .setFilename(part.getAlternativeFileName())
                        .setContentType(part.getContentType())
                        .setReferenceToFileUploadRequestMissingData(idMultipartFormUploadRequestMissingFiles)
                        .setName(part.getName())
                        .setSize(part.getSize())
                        .setData(part.getData())
                        .setFileHeaders(part.getHeaders())
                        .build());
            } catch (Value.BuilderIncompleteException e) {
                throw new ResourcePosterModelStoreException("Unable to File Upload", e);
            }
        }
        return simpleFileBuilder.build();
    }

    public Long storeHeaders(final Connection connection, ImmutableMap<String, ImmutableList<String>> properties, ImmutableMap<String, ImmutableList<String>> requestHeaders) throws Throwable {
        final MultipartFormUploadRequestMissingFiles build = new MultipartFormUploadRequestMissingFiles.Builder()
                .setUploadHeaders(requestHeaders)
                .setProperties(properties)
                .build();


        try (final PreparedStatement ps = connection.prepareStatement(this.fileUploadStore.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            return this.fileUploadStore.getFunction().apply(new DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction.Builder<MultipartFormUploadRequestMissingFiles>()
                    .setPreparedStatement(ps)
                    .setT(build)
                    .build());
        }

    }

    @Override
    public Void apply(ResourcePosterModel resourcePosterModel) throws Throwable {
        return this.connectionProducer.execute(new Function<Connection, Void, Throwable>() {
            @Override
            public Void apply(Connection connection) throws Throwable {
                final ImmutableSet<Long> albums = resourcePosterModel.getAlbums();
                final ImmutableList<PartCached> partToStoreRenamed = resourcePosterModel.getPartToStoreRenamed();
                final ImmutableMap<String, ImmutableList<String>> propertiesImmutable = resourcePosterModel.getPropertiesImmutable();
                final ImmutableMap<String, ImmutableList<String>> requestHeaders = resourcePosterModel.getRequestHeaders();
                final Long idMultipartFormUploadRequestMissingFiles = storeHeaders(connection, propertiesImmutable, requestHeaders);
                storeParts(getSimpleFiles(idMultipartFormUploadRequestMissingFiles, partToStoreRenamed), idMultipartFormUploadRequestMissingFiles, connection, albums);
                return null;
            }
        });

    }


    public static class ResourcePosterModelStoreException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -2253872128799992671L;

        public ResourcePosterModelStoreException(String s, Exception e) {
            super(s, e);
        }
    }

    public void storeParts(ImmutableList<FileUpload> simpleFiles, Long multipartFileUploadRequestThingy, Connection conn, final ImmutableSet<Long> albumReferences) throws Throwable {

        for (final FileUpload simpleFile : simpleFiles) {
            final FileUploadPartRepositoryStoreModel fileUploadPartRepositoryStoreModel = new FileUploadPartRepositoryStoreModel.Builder()
                    .setDatabaseFile(simpleFile)
                    .setFileUploadRequestId(multipartFileUploadRequestThingy)
                    .build();


            final Long fileUploadKey;
            try (final PreparedStatement ps = conn.prepareStatement(this.fileUploadPartRepositoryStore.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
                fileUploadKey = this.fileUploadPartRepositoryStore.getFunction().apply(new DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction.Builder<FileUploadPartRepositoryStoreModel>()
                        .setPreparedStatement(ps)
                        .setT(fileUploadPartRepositoryStoreModel)
                        .build());
            }

            for (final Long albumKey : albumReferences) {
                final AlbumItemRepositoryStoreModel albumItemRepositoryStoreModel = new AlbumItemRepositoryStoreModel.Builder()
                        .setAlbumItemKey(albumKey)
                        .setFileUploadKey(fileUploadKey)
                        .build();
                try (final PreparedStatement ps = conn.prepareStatement(this.albumItemRepositoryStore.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
                    this.albumItemRepositoryStore.getFunction().apply(new DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction.Builder<AlbumItemRepositoryStoreModel>()
                            .setPreparedStatement(ps)
                            .setT(albumItemRepositoryStoreModel)
                            .build());
                }
            }


        }

    }
}