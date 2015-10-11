package com.mieoffline.server.services;

import com.mieoffline.site.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestResponseWrapper implements Value<HttpServletRequestResponseWrapper.Builder,HttpServletRequestResponseWrapper>{


    /**
	 * 
	 */
	private static final long serialVersionUID = 9009950288549616916L;
	private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final String pathTo;
    private final String pathRemaining;
    public HttpServletRequestResponseWrapper(final Builder builder, String pathTo, String pathRemaining) {
        this.pathTo = pathTo;
        this.pathRemaining = pathRemaining;
        this.httpServletRequest = builder.httpServletRequest;
        this.httpServletResponse = builder.httpServletResponse;
    }

    @Override
    public Builder newBuilder() {
        return new Builder() ;
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public String getPathTo() {
        return this.pathTo;
    }

    public String getPathRemaining() {
        return this.pathRemaining;
    }



    public HttpServletRequest getHttpServletRequest() {
        return this.httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return this.httpServletResponse;
    }

    public static class Builder implements Value.Builder<HttpServletRequestResponseWrapper,Builder> {
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private String pathTo;
        private String pathRemaining;

        public Builder(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) {
            this.httpServletRequest = httpServletRequestResponseWrapper.httpServletRequest;
            this.httpServletResponse = httpServletRequestResponseWrapper.httpServletResponse;
            this.pathTo = httpServletRequestResponseWrapper.pathTo;
            this.pathRemaining = httpServletRequestResponseWrapper.pathRemaining;
        }

        public Builder() {

        }

        public Builder setPathTo(String pathTo) {
            this.pathTo = pathTo;
            return this;
        }

        public Builder setPathRemaining(String pathRemaining) {
            this.pathRemaining = pathRemaining;
            return this;
        }

        public Builder setHttpServletRequest(HttpServletRequest httpServletRequest) {
            this.httpServletRequest = httpServletRequest;
            return this;
        }

        public Builder setHttpServletResponse(HttpServletResponse httpServletResponse) {
            this.httpServletResponse = httpServletResponse;
            return this;
        }


        @Override
        public HttpServletRequestResponseWrapper build() throws BuilderIncompleteException {
            return new HttpServletRequestResponseWrapper(this, this.pathTo, this.pathRemaining);
        }
    }
}
