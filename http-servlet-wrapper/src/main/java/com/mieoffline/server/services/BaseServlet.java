package com.mieoffline.server.services;

import com.mieoffline.functional.Consumer;
import com.mieoffline.site.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7335196007333521103L;
	private final Consumer<HttpServletRequestResponseWrapper,?> consumer;
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseServlet.class);
    public BaseServlet(final Consumer<HttpServletRequestResponseWrapper,?> consumer) {
        this.consumer = consumer;
    }



    @Override
    protected void doGet(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpServletRequestResponseWrapper httpServletRequestResponseWrapper = getHttpServletRequestResponseWrapperFromRequestURI(req, resp);
        consume(httpServletRequestResponseWrapper);
    }

    @Override
    protected void doPost(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpServletRequestResponseWrapper httpServletRequestResponseWrapper = getHttpServletRequestResponseWrapperFromRequestURI(req, resp);
        consume(httpServletRequestResponseWrapper);
    }

    @Override
    protected void doPut(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpServletRequestResponseWrapper httpServletRequestResponseWrapper = getHttpServletRequestResponseWrapperFromRequestURI(req, resp);
        consume(httpServletRequestResponseWrapper);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpServletRequestResponseWrapper httpServletRequestResponseWrapper = getHttpServletRequestResponseWrapperFromRequestURI(req, resp);
        consume(httpServletRequestResponseWrapper);
    }

    private HttpServletRequestResponseWrapper getHttpServletRequestResponseWrapperFromRequestURI(HttpServletRequest req, HttpServletResponse resp) {
        try {
            return new HttpServletRequestResponseWrapper
                    .Builder()
                    .setPathRemaining(req.getRequestURI().split("/",2)[1])
                    .setPathTo("/")
                    .setHttpServletRequest(req)
                    .setHttpServletResponse(resp)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new RuntimeException(e);
        }

    }

    private void consume(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) {
        try {
            this.consumer.apply(httpServletRequestResponseWrapper);
        } catch (final Throwable e) {
            httpServletRequestResponseWrapper.getHttpServletResponse().setStatus(500);
            LOGGER.error("Error handling request ", e);
            try {
                httpServletRequestResponseWrapper.getHttpServletRequest().getInputStream().close();
            }
            catch (@SuppressWarnings("unused") Exception e2) {
                // ignore
            }
            finally {
                try {
                    httpServletRequestResponseWrapper.getHttpServletResponse().getOutputStream().close();
                }
                catch (@SuppressWarnings("unused") Exception e3) {
                    // ignore
                }


            }

        }
    }

    @Override
    public void destroy() {

    }

}
