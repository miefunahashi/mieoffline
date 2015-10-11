package com.mieoffline.server.services;

import com.mieoffline.site.Value;

import javax.servlet.http.HttpServletResponse;

public class PathWithHttpServleResponse implements Value<PathWithHttpServleResponse.Builder,PathWithHttpServleResponse>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6170109909035611892L;
	private final HttpServletResponse httpServletResponse;
    private final String path;
    private PathWithHttpServleResponse(Builder builder) {
        this.path = builder.path;
        this.httpServletResponse = builder.httpServletResponse;
    }

    public HttpServletResponse getHttpServletResponse() {
        return this.httpServletResponse;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public Builder newBuilder() {
        return null;
    }

    @Override
    public Builder asBuilder() {
        return null;
    }

    public static class Builder implements Value.Builder<PathWithHttpServleResponse,Builder>{
        private HttpServletResponse httpServletResponse;
        private String path;
        public Builder() {

        }
        public Builder(PathWithHttpServleResponse pathWithHttpServleResponse) {
            this.path = pathWithHttpServleResponse.path;
            this.httpServletResponse = pathWithHttpServleResponse.httpServletResponse;
        }

        public Builder setHttpServletResponse(HttpServletResponse httpServletResponse) {
            this.httpServletResponse = httpServletResponse;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;

        }

        @Override
        public PathWithHttpServleResponse build() throws BuilderIncompleteException {
            PathWithHttpServleResponse pathWithHttpServleResponse = new PathWithHttpServleResponse(this);
            if(pathWithHttpServleResponse.path == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("path")
                        .build();
            }
            if(pathWithHttpServleResponse.httpServletResponse == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("httpServletResponse")
                        .build();
            }
            return pathWithHttpServleResponse;

        }
    }
}
