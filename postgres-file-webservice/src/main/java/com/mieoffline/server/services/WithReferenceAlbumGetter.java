package com.mieoffline.server.services;


import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Producer;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WithReferenceAlbumGetter implements Consumer<HttpServletRequestResponseWrapper, WithReferenceAlbumGetter.WithReferenceAlbumGetterException> {
    final Producer<String, ?> albumsSetAsJsonProducer;
    public WithReferenceAlbumGetter(Producer<String, ?> albumsSetAsJsonProducer) {
        this.albumsSetAsJsonProducer = albumsSetAsJsonProducer;
    }

    @SuppressWarnings("resource")
	@Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws WithReferenceAlbumGetterException {
        final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
        httpServletResponse.setContentType("application/json");
        final String s;
        try {
            s = this.albumsSetAsJsonProducer.apply(null);
        } catch (Throwable e) {
            throw new WithReferenceAlbumGetterException("Could not get the albums", e);
        }
        final ServletOutputStream outputStream;
        try {
            outputStream = httpServletResponse.getOutputStream();
        } catch (IOException e) {
            throw new WithReferenceAlbumGetterException("Cannot get HTTP Response output stream", e);
        }
        try {
            IOUtils.write(s, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new WithReferenceAlbumGetterException("Cannot return album set", e);
        }
        httpServletResponse.setStatus(200);
        return null;
    }

    public class WithReferenceAlbumGetterException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -3041514918302574444L;

		public WithReferenceAlbumGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}
