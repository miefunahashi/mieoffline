package com.mieoffline.server.postgres.controllers.base;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.markoffline.site.database.model.DatabaseEntity;
import com.markoffline.site.database.model.DatabaseReference;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.Albums;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.UploadsForAlbumGetter;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.commands.AlbumItemsListAll;
import com.mieoffline.http.fileupload.repository.postgres.commands.AlbumReadAll;
import com.mieoffline.http.fileupload.repository.postgres.commands.AlbumStore;
import com.mieoffline.http.fileupload.repository.postgres.commands.DatabaseFunctionQuery;
import com.mieoffline.http.fileupload.repository.postgres.commands.FileUploadPartRepositoryReadSimpleFileWithoutData;
import com.mieoffline.http.fileupload.repository.postgres.json.to.AlbumToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.AlbumsToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.DatabaseEntityToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.FileUploadWithoutDataToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.ImmutableSetToNode;
import com.mieoffline.http.fileupload.repository.postgres.json.to.LongToMap;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.json.MapFromNodeToString;
import com.mieoffline.json.MapToStringJson;
import com.mieoffline.json.Node;
import com.mieoffline.server.postgres.MapFromNodeToJsonString;
import com.mieoffline.server.services.AlbumsAsStringJsonProducer;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.server.services.HttpServletRequestResponseWrapperRouter;
import com.mieoffline.server.services.JsonGetter;
import com.mieoffline.server.services.NewAlbumPost;
import com.mieoffline.server.services.WithReferenceAlbumGetter;

public class AlbumsRootController implements Producer<Consumer<HttpServletRequestResponseWrapper, ?>, AlbumsRootController.AlbumsException> {
    private final MapFromNodeToJsonString serializer;
    private final SQLUtils sqlUtils;
    private final Utils utils ;
    private final AvroDecoder<Headers> headersAvroHelper;
    private final IDatabase iDatabase;

    public AlbumsRootController(IDatabase iDatabase, MapFromNodeToJsonString serializer, SQLUtils sqlUtils, Utils utils, AvroDecoder<Headers> headersAvroHelper) {
        this.serializer = serializer;
        this.sqlUtils = sqlUtils;
        this.utils = utils;
        this.headersAvroHelper = headersAvroHelper;
        this.iDatabase = iDatabase;
    }

    @Override
    public Consumer<HttpServletRequestResponseWrapper, ?> apply(Void v) throws AlbumsException {
        final MapFromNodeToString mapToNodeToString = new MapFromNodeToString(this.serializer);


        final AlbumToNode albumToNode = new AlbumToNode();

        final AlbumsToNode albumsToNode = new AlbumsToNode(albumToNode);
        final MapToStringJson<Albums> apply = mapToNodeToString.apply(albumsToNode);

        final AlbumStore albumStore = new AlbumStore();

        final LongToMap longToMap = new LongToMap();
        final DatabaseFunctionQuery<Void, ImmutableSet<DatabaseEntity<Long, Album>>> voidImmutableSetDatabaseFunctionQuery;
        try {
            voidImmutableSetDatabaseFunctionQuery = new DatabaseFunctionQuery<>(this.iDatabase, new AlbumReadAll().apply(null));
        } catch (Throwable throwable) {
            throw new AlbumsException("BLA",throwable);
        }
        final Producer<Albums, ?> albumsProducer = new Producer<com.mieoffline.http.fileupload.repository.postgres.Albums, Exception>() {
            @Override
            public Albums apply(Void avoid) throws Exception {
                final ImmutableSet<DatabaseEntity<Long, Album>> produce = voidImmutableSetDatabaseFunctionQuery.apply(null);
                final ImmutableSet.Builder<Album> albumBuilder = ImmutableSet.builder();
                for (DatabaseEntity<Long, Album> albumDatabaseEntity : produce) {
                    albumBuilder.add(albumDatabaseEntity.getObject());
                }
                return new com.mieoffline.http.fileupload.repository.postgres.Albums.Builder()
                        .setAlbums(albumBuilder.build())
                        .build();
            }
        };
        final DatabaseEntityToNode<Long, Album> longAlbumDatabaseEntityToNode = new DatabaseEntityToNode<>(longToMap, albumToNode);

        final ImmutableSetToNode<DatabaseEntity<Long, Album>> databaseEntityImmutableSetFromNode = new ImmutableSetToNode<>("albums", longAlbumDatabaseEntityToNode);

        final MapToStringJson<ImmutableSet<DatabaseEntity<Long, Album>>> albumsMapToStringJson = mapToNodeToString.apply(databaseEntityImmutableSetFromNode);


        final AlbumsAsStringJsonProducer albumAsStringJsonProducer = new AlbumsAsStringJsonProducer(voidImmutableSetDatabaseFunctionQuery, albumsMapToStringJson);

        final WithReferenceAlbumGetter withReferenceAlbumGetter = new WithReferenceAlbumGetter(albumAsStringJsonProducer);
        final DatabaseFunctionQuery<Album, Long> albumLongDatabaseFunctionQuery;
        try {
            albumLongDatabaseFunctionQuery= new DatabaseFunctionQuery<>(this.iDatabase, albumStore.apply(null));
        } catch (Throwable throwable) {
         throw new AlbumsException("N",throwable);
        }

        final AlbumItemsListAll albumItemsListAll1 = new AlbumItemsListAll(this.sqlUtils);
        final DatabaseFunctionQuery<String, ImmutableSet<DatabaseReference<Long>>> stringImmutableSetDatabaseFunctionQuery;
        try {
             stringImmutableSetDatabaseFunctionQuery = new DatabaseFunctionQuery<>(this.iDatabase, albumItemsListAll1.apply(null));
        } catch (Throwable throwable) {
            throw new AlbumsException("Error with database function",throwable);
        }

        final FileUploadPartRepositoryReadSimpleFileWithoutData fileUploadPartRepositoryReadSimpleFileWithoutData = new FileUploadPartRepositoryReadSimpleFileWithoutData(this.headersAvroHelper, this.utils, this.sqlUtils);

        final DatabaseFunctionQuery<Long,FileUploadWithoutData<Long>> fileUploadPartRepositoryReadSimpleFileWithoutDataQuery;
        try {
            fileUploadPartRepositoryReadSimpleFileWithoutDataQuery = new DatabaseFunctionQuery<>(this.iDatabase,fileUploadPartRepositoryReadSimpleFileWithoutData.apply(null));
        } catch (Throwable throwable) {
            throw new AlbumsException("Error with database function",throwable);
        }
        final UploadsForAlbumGetter uploadsForAlbumGetter = new UploadsForAlbumGetter(stringImmutableSetDatabaseFunctionQuery, fileUploadPartRepositoryReadSimpleFileWithoutDataQuery);
        final FileUploadWithoutDataToNode fileUploadWithoutDataToNode = new FileUploadWithoutDataToNode();
        final Function<String, String, ?> getAlbumItemsAsStringFromAlbumName = input -> {
            final ImmutableSet<FileUploadWithoutData<Long>> apply1 = uploadsForAlbumGetter.apply(input);

            final ImmutableSetToNode<FileUploadWithoutData<Long>> immutableSetToNode = new ImmutableSetToNode<>("albumItems", fileUploadWithoutDataToNode);
            final Node apply2 = immutableSetToNode.apply(apply1);

            return this.serializer.apply(apply2);
        };
        final AlbumItemsListGetter albumItemsListGetter = new AlbumItemsListGetter(getAlbumItemsAsStringFromAlbumName);
        ImmutableSortedMap.Builder<String, Consumer<HttpServletRequestResponseWrapper,?>> map = ImmutableSortedMap.naturalOrder();
        map.put(  "", new NewAlbumPost(albumLongDatabaseFunctionQuery));
        map.put(  "withReferences", withReferenceAlbumGetter);
        map.put(  "basic", new JsonGetter<>(apply, albumsProducer));
        map.put(  "items", albumItemsListGetter);
        
        return new HttpServletRequestResponseWrapperRouter(
                map.build()
        );
    }

    public static class AlbumsException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = 2719004990198807348L;

		public AlbumsException(String s, Throwable throwable) {
            super(s,throwable);
        }
    }
}