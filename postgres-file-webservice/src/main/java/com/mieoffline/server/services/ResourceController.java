package com.mieoffline.server.services;

import com.mieoffline.functional.Consumer;

public class ResourceController implements Consumer<HttpServletRequestResponseWrapper,ResourceController.ResourceControllerException> {
    final ResourceGetter resourceGetter;
    final ResourcePoster resourcePoster;

    public ResourceController(ResourceGetter resourceGetter, ResourcePoster resourcePoster) {
        this.resourceGetter = resourceGetter;
        this.resourcePoster = resourcePoster;
    }


    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws ResourceControllerException {
        if(httpServletRequestResponseWrapper.getHttpServletRequest().getMethod().equals("GET")){
            try {
                this.resourceGetter.apply(httpServletRequestResponseWrapper);
            } catch (ResourceGetter.ResourceGetterException e) {
                throw new ResourceControllerException("Error getting local resource",e);
            }
        }
        else if (httpServletRequestResponseWrapper.getHttpServletRequest().getMethod().equals("POST")){
            try {
                this.resourcePoster.apply(httpServletRequestResponseWrapper);
            } catch (ResourcePoster.ResourcePosterException e) {
                throw new ResourceControllerException("Error storing data",e);
            }
        }
        else {
            throw new ResourceControllerException("Method not supported");
        }
        return null;
    }

    public static class ResourceControllerException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -8952202387001813591L;

		public ResourceControllerException(String s) {
            super(s);
        }

        public ResourceControllerException(String s, Exception e) {
            super(s,e);
        }
    }
}
