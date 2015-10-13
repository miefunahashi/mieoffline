package com.mieoffline.server;

import com.google.common.collect.ImmutableList;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.FileUploadWithMultipartFormUploadRequestMissingFiles;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.server.services.PathWithHttpServleResponse;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ResourceFileGetter implements Consumer<PathWithHttpServleResponse, ResourceFileGetter.ResourceFileGetterException> {
    private final Function<String, FileUploadWithMultipartFormUploadRequestMissingFiles, ?> getFileUploadWithMultipartFormUploadRequestMissingFiles;

    public ResourceFileGetter(Function<String, FileUploadWithMultipartFormUploadRequestMissingFiles, ?> getFileUploadWithMultipartFormUploadRequestMissingFiles) {
        this.getFileUploadWithMultipartFormUploadRequestMissingFiles = getFileUploadWithMultipartFormUploadRequestMissingFiles;
    }

    @Override
    public Void apply(PathWithHttpServleResponse pathWithHttpServleResponse) throws ResourceFileGetterException {
        final String pathRemaining = pathWithHttpServleResponse.getPath();
        final HttpServletResponse httpServletResponse = pathWithHttpServleResponse.getHttpServletResponse();
        final FileUpload simpleFile;
        final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles;
        final FileUploadWithMultipartFormUploadRequestMissingFiles fileUploadWithMultipartFormUploadRequestMissingFiles;
        try {
            fileUploadWithMultipartFormUploadRequestMissingFiles = this.getFileUploadWithMultipartFormUploadRequestMissingFiles.apply(pathRemaining);
        } catch (Throwable e) {
            throw new ResourceFileGetterException("Unable to get file with metadata", e);
        }
        simpleFile = fileUploadWithMultipartFormUploadRequestMissingFiles.getFileUpload();
        multipartFormUploadRequestMissingFiles = fileUploadWithMultipartFormUploadRequestMissingFiles.getMultipartFormUploadRequestMissingFiles();
        httpServletResponse.addHeader("filename", simpleFile.getFilename());
        if (simpleFile.getName().isPresent()) {
            httpServletResponse.addHeader("name", simpleFile.getName().get());
        }
        if (simpleFile.getContentType().isPresent()) {
            httpServletResponse.addHeader("Content-Type", simpleFile.getContentType().get());
        }

        for (Map.Entry<String, ImmutableList<String>> entry : multipartFormUploadRequestMissingFiles.getProperties().entrySet()) {
            for (String s : entry.getValue()) {
                httpServletResponse.addHeader("form_" + entry.getKey(), s);
            }
        }
        for (Map.Entry<String, ImmutableList<String>> entry : simpleFile.getFileHeaders().entrySet()) {
            for (String s : entry.getValue()) {
                httpServletResponse.addHeader("part_" + entry.getKey(), s);
            }
        }

        for (Map.Entry<String, ImmutableList<String>> entry : multipartFormUploadRequestMissingFiles.getUploadHeaders().entrySet()) {
            for (String s : entry.getValue()) {
                httpServletResponse.addHeader("upload_" + entry.getKey(), s);
            }
        }
        try {
            @SuppressWarnings("resource")
            final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            IOUtils.write(simpleFile.getData(), outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new ResourceFileGetterException("Unable to write response", e);
        }
        return null;
    }

    public static class ResourceFileGetterException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -7297748037220125864L;

        public ResourceFileGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}
