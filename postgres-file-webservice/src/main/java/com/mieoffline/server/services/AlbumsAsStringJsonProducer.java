package com.mieoffline.server.services;

import com.google.common.collect.ImmutableSet;
import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.json.MapToStringJson;
import com.mieoffline.json.ObjectMapper;

public class AlbumsAsStringJsonProducer implements Producer<String, AlbumsAsStringJsonProducer.BasicAlbumGetterException> {
    final Function<Void, ImmutableSet<DatabaseEntity<Long, Album>>, ?> readAllAlbumsWithReferences;
    final MapToStringJson<ImmutableSet<DatabaseEntity<Long, Album>>> albumsMapToStringJson;

    public AlbumsAsStringJsonProducer(Function<Void, ImmutableSet<DatabaseEntity<Long, Album>>, ?> readAllAlbumsWithReferences, MapToStringJson<ImmutableSet<DatabaseEntity<Long, Album>>> albumsMapToStringJson) {
        this.readAllAlbumsWithReferences = readAllAlbumsWithReferences;
        this.albumsMapToStringJson = albumsMapToStringJson;
    }


    @Override
    public String apply(Void avoid) throws BasicAlbumGetterException {
        final ImmutableSet<DatabaseEntity<Long, Album>> albumsSet;
        try {
            albumsSet = this.readAllAlbumsWithReferences.apply(null);
        } catch (Throwable e) {
            throw new BasicAlbumGetterException("Could not read albums from database", e);
        }

        try {
            return this.albumsMapToStringJson.apply(albumsSet);
        } catch (ObjectMapper.MappingException e) {
            throw new BasicAlbumGetterException("Could not get Albums from json", e);
        }

    }

    public static class BasicAlbumGetterException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = 7752251791549721031L;

		public BasicAlbumGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}
