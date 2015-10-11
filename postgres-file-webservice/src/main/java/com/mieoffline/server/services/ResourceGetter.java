package com.mieoffline.server.services;

import com.mieoffline.functional.Consumer;
import com.mieoffline.server.ResourceFileGetter;
import com.mieoffline.server.ResourceListGetter;
import com.mieoffline.site.Value;

public class ResourceGetter implements Consumer<HttpServletRequestResponseWrapper,ResourceGetter.ResourceGetterException> {

    private final ResourceListGetter resourceListGetter;
    private final ResourceFileGetter resourceFileGetter;

    public ResourceGetter(ResourceListGetter resourceListGetter, ResourceFileGetter resourceFileGetter) {
        this.resourceListGetter = resourceListGetter;
        this.resourceFileGetter = resourceFileGetter;
    }


    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws ResourceGetterException {
        final String pathRemaining = httpServletRequestResponseWrapper.getPathRemaining();
        if (pathRemaining.isEmpty()) {
            try {
                this.resourceListGetter.apply(httpServletRequestResponseWrapper.getHttpServletResponse());
            } catch (ResourceListGetter.ResourceListGetterException e) {
                throw new ResourceGetterException("Unable to return list of resources",e);
            }
        } else {
            PathWithHttpServleResponse pathWithHttpServleResponse = null;
            try {
                pathWithHttpServleResponse = new PathWithHttpServleResponse.Builder().setPath(pathRemaining).setHttpServletResponse(httpServletRequestResponseWrapper.getHttpServletResponse()).build();
            } catch (Value.BuilderIncompleteException e) {
                throw new ResourceGetterException("Unable to use path or http response",e);
            }
            try {
                this.resourceFileGetter.apply(pathWithHttpServleResponse);
            } catch (ResourceFileGetter.ResourceFileGetterException e) {
                throw new ResourceGetterException("Error writing file to response",e);
            }
        }
        return null;
    }


    public static class ResourceGetterException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -7174465960113392552L;

		public ResourceGetterException(String s, Throwable e) {

            super(s,e);
        }
    }

}
