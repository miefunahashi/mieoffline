package com.mieoffline.server.postgres.controllers.base;


import com.mieoffline.functional.Consumer;
import com.mieoffline.server.ResourceFileGetter;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.server.services.PathWithHttpServleResponse;
import com.mieoffline.site.Value;

public class ImageMagicController implements Consumer<HttpServletRequestResponseWrapper, ImageMagicController.ImageMagicControllerException> {
    final ResourceFileGetter resourceFileGetter;

    public ImageMagicController(ResourceFileGetter resourceFileGetter) {
        this.resourceFileGetter = resourceFileGetter;
    }


    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws ImageMagicControllerException {
        final String pathRemaining = httpServletRequestResponseWrapper.getPathRemaining();
        final PathWithHttpServleResponse pathWithHttpServleResponse;
        try {
            pathWithHttpServleResponse = new PathWithHttpServleResponse.Builder().setPath(pathRemaining).setHttpServletResponse(httpServletRequestResponseWrapper.getHttpServletResponse()).build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ImageMagicControllerException("Unable to use path or http response", e);
        }
        try {
            this.resourceFileGetter.apply(pathWithHttpServleResponse);
        } catch (ResourceFileGetter.ResourceFileGetterException e) {
            throw new ImageMagicControllerException("Error writing file to response", e);
        }
        return null;
    }

    public static class ImageMagicControllerException extends Exception {

        public ImageMagicControllerException(String s, Exception e) {
            super(s, e);
        }
    }
}
