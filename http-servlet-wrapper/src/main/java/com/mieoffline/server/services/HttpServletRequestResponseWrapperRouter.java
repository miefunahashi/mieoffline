package com.mieoffline.server.services;

import com.google.common.collect.ImmutableSortedMap;
import com.mieoffline.functional.Consumer;
import com.mieoffline.site.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpServletRequestResponseWrapperRouter implements Consumer<HttpServletRequestResponseWrapper, HttpServletRequestResponseWrapperRouter.HttpServletRequestResponseWrapperRouterException> {
    private final ImmutableSortedMap<String, Consumer<HttpServletRequestResponseWrapper, ?>> consumers;
    public static final String PATHTO_SLASH_BEFORE = "%s/%s";

    public HttpServletRequestResponseWrapperRouter(
            final ImmutableSortedMap<String, Consumer<HttpServletRequestResponseWrapper, ?>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws HttpServletRequestResponseWrapperRouterException {
        final String[] splitPathRemaining = httpServletRequestResponseWrapper.getPathRemaining().split("/", 2);
        final Consumer<HttpServletRequestResponseWrapper, ?> httpServletRequestResponseWrapperConsumer;
        final HttpServletRequestResponseWrapper toConsume;
        if (splitPathRemaining.length == 0) {
            httpServletRequestResponseWrapperConsumer = this.consumers.get(EMPTY);
            toConsume = httpServletRequestResponseWrapper;
        } else {
            httpServletRequestResponseWrapperConsumer = this.consumers.get(splitPathRemaining[0]);
            try {
                if (splitPathRemaining.length == 1) {

                    toConsume = httpServletRequestResponseWrapper.asBuilder().setPathTo(formatNewPathTo(httpServletRequestResponseWrapper.getPathTo(), splitPathRemaining[0])).setPathRemaining(EMPTY).build();

                } else {
                    toConsume = httpServletRequestResponseWrapper.asBuilder().setPathTo(formatNewPathTo(httpServletRequestResponseWrapper.getPathTo(), splitPathRemaining[0])).setPathRemaining(splitPathRemaining[1]).build();
                }
            } catch (Value.BuilderIncompleteException e) {
                throw new HttpServletRequestResponseWrapperRouterException("Cannot build an http request/response wrapper",e);
            }
        }
        try {
            httpServletRequestResponseWrapperConsumer.apply(toConsume);
        } catch (Throwable e) {
            throw new HttpServletRequestResponseWrapperRouterException("Error routing throw to consumer for uri: " + httpServletRequestResponseWrapper.getHttpServletRequest().getRequestURI(), e);
        }
        return null;
    }

    private static String formatNewPathTo(final String currentPathTo, final String newPath) {
        return String.format("%s%s/", currentPathTo, newPath);
    }

//    private static String getBefore(String pathTo, String[] beforeAndAfterURIParts) {
//
//        return String.format(PATHTO_SLASH_BEFORE, pathTo, getStringOrEmptyString(beforeAndAfterURIParts[0]));
//    }

//    private static String getStringOrEmptyString(String string) {
//        return string == null ? EMPTY : string;
//    }

    public static class HttpServletRequestResponseWrapperRouterException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -9163102339026027859L;

		public HttpServletRequestResponseWrapperRouterException(String s) {
            super(s);
        }

        public HttpServletRequestResponseWrapperRouterException(String s, Throwable e) {
            super(s,e);
        }
    }
}
