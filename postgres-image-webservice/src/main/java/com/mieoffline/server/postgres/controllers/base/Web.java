package com.mieoffline.server.postgres.controllers.base;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.webservice.controller.ClasspathHttpGetter;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;

public class Web implements Producer<Consumer<HttpServletRequestResponseWrapper,?>,Web.WebException> {

    private final Function<String, String, ?> fileTypeMap;

    public Web(Function<String, String, ?> fileTypeMap) {
        this.fileTypeMap = fileTypeMap;
    }

    @Override
    public Consumer<HttpServletRequestResponseWrapper, ?> apply(Void avoid) throws WebException {

        return new ClasspathHttpGetter(this.fileTypeMap, "website/");
    }
    public static class WebException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8697712379797992351L;

    }
}
