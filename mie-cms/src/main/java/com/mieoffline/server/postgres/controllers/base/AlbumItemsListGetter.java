package com.mieoffline.server.postgres.controllers.base;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AlbumItemsListGetter implements Consumer<HttpServletRequestResponseWrapper, AlbumItemsListGetter.AlbumItemsListGetterException> {
    final Function<String, String, ?> albumItemsListFunction;

    public AlbumItemsListGetter(Function<String, String, ?> albumItemsListFunction) {
        this.albumItemsListFunction = albumItemsListFunction;
    }


    @SuppressWarnings("resource")
	@Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws AlbumItemsListGetterException {
        final String album = httpServletRequestResponseWrapper.getPathRemaining();
        final String json;
        try {
            json = this.albumItemsListFunction.apply(album);
        } catch (Throwable e) {
            throw new AlbumItemsListGetterException("Unable to get items for given album", e);
        }
        final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
        final ServletOutputStream outputStream;
        try {
            outputStream = httpServletResponse.getOutputStream();

            IOUtils.write(json, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new AlbumItemsListGetterException("Unable to return album item list", e);
        }
        httpServletResponse.setStatus(200);
        return null;
    }

    public static class AlbumItemsListGetterException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -2236591801175833429L;

		public AlbumItemsListGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}