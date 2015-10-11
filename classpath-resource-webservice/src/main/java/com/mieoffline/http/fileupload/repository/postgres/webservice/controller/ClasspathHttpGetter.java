package com.mieoffline.http.fileupload.repository.postgres.webservice.controller;


import com.google.common.io.Resources;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.apache.commons.lang3.StringUtils.EMPTY;


public class ClasspathHttpGetter implements Consumer<HttpServletRequestResponseWrapper, ClasspathHttpGetter.ClasspathHttpGetterException> {

    public static final String INDEX_HTML = "index.html";
    private final Function<String, String, ?> fileTypeMap;
    private final String pagesPrefix;

    public ClasspathHttpGetter(final Function<String, String, ?> fileTypeMap, String pagesPrefix) {
        this.fileTypeMap = fileTypeMap;
        this.pagesPrefix = pagesPrefix;
    }


    @Override
    public Void apply(final HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws ClasspathHttpGetterException {
        final String pathRemaining = httpServletRequestResponseWrapper.getPathRemaining();
        if (pathRemaining.equals(EMPTY) || pathRemaining.endsWith("/")) {
            String originalLocation = httpServletRequestResponseWrapper.getHttpServletRequest().getRequestURI().toString();
            String redirectLocation = originalLocation.endsWith("/") ? originalLocation + INDEX_HTML : originalLocation + "/" + INDEX_HTML;
            redirect(httpServletRequestResponseWrapper.getHttpServletResponse(), redirectLocation);
        } else {
            final String path = this.pagesPrefix.concat(pathRemaining);
            final URL resource = Resources.getResource(path);
            final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
            try {
                httpServletResponse.setContentType(this.fileTypeMap.apply(path));
            } catch (Throwable e) {
                throw new ClasspathHttpGetterException("Error getting file type", e);
            }
            try {

                @SuppressWarnings("resource")
                final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
                try (final InputStream input = resource.openStream()) {
                    IOUtils.copy(input, outputStream);
                }
                outputStream.flush();
            } catch (IOException e) {
                throw new ClasspathHttpGetterException("Error reading classpath resource", e);
            }
        }
        return null;
    }

    private void redirect(HttpServletResponse httpServletResponse, final String direction) {
        httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        httpServletResponse.setHeader("Location", direction);
    }

    public class ClasspathHttpGetterException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 7348966622316762136L;

        public ClasspathHttpGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}