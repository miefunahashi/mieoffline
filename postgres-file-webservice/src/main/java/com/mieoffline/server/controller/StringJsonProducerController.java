package com.mieoffline.server.controller;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Producer;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StringJsonProducerController  implements Consumer<HttpServletRequestResponseWrapper, StringJsonProducerController.StringJsonProducerControllerException> {
    final Producer<String,?> stringProducer;

    public StringJsonProducerController(Producer<String, ?> stringProducer) {
        this.stringProducer = stringProducer;
    }

    @SuppressWarnings("resource")
	@Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws StringJsonProducerControllerException {
        final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();

        final String responseBody;
        try {
            responseBody =this.stringProducer.apply(null);
        } catch (Throwable e) {
            throw new StringJsonProducerControllerException("Could not get content", e);
        }
        final ServletOutputStream outputStream;
        try {
            outputStream = httpServletResponse.getOutputStream();
            IOUtils.write(responseBody, outputStream);
            outputStream.flush();
            
        } catch (IOException e) {
            throw new StringJsonProducerControllerException("Error responding with data", e);
        }
        httpServletResponse.setStatus(200);
        return null;
    }

    public static class StringJsonProducerControllerException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -4133322732779736028L;

		public StringJsonProducerControllerException(String s, Throwable  e) {
            super(s,e);
        }
    }
}
