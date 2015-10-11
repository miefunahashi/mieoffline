package com.mieoffline.server.services;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.site.Value;

import javax.servlet.http.HttpServletRequest;

public class NewAlbumPost implements Consumer<HttpServletRequestResponseWrapper, NewAlbumPost.NewAlbumPostException> {
    private final Function<Album, ?,?> albumConsumer;

    public NewAlbumPost(Function<Album,?, ?> albumConsumer) {
        this.albumConsumer = albumConsumer;
    }

    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws NewAlbumPostException {
        final HttpServletRequest httpServletRequest = httpServletRequestResponseWrapper.getHttpServletRequest();
        final String name = httpServletRequest.getParameter("name");
        final String description = httpServletRequest.getParameter("description");

        final Album album;
        try {
            album = new Album.Builder()
                    .setName(name)
                    .setDecription(description)
                    .build();
            try {
                this.albumConsumer.apply(album);
            } catch (Throwable e) {
                throw new NewAlbumPostException("Unable to store Album", e);
            }
            httpServletRequestResponseWrapper.getHttpServletResponse().setStatus(200);
        } catch (Value.BuilderIncompleteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class NewAlbumPostException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = 8695539481972845102L;

		public NewAlbumPostException(String s, Throwable e) {
            super(s, e);
        }
    }
}
