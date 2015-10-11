package com.mieoffline.server;

import com.mieoffline.functional.Consumer;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by mark on 09/10/15.
 */
public class RedirectConsumer  implements Consumer<HttpServletRequestResponseWrapper, RedirectConsumer.RedirectConsumerException> {
    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws RedirectConsumerException {
        HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
        httpServletResponse.setHeader("Location",httpServletRequestResponseWrapper.getHttpServletRequest().getRequestURI() + "web/");
        httpServletResponse.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        return null;
    }

    public static class RedirectConsumerException extends Exception {

    }
}
