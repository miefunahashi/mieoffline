package com.mieoffline.server.services;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Producer;
import com.mieoffline.json.MapToStringJson;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class JsonGetter <T> implements Consumer<HttpServletRequestResponseWrapper,ResourceGetter.ResourceGetterException> {

    final MapToStringJson<T> mapToNodeToString;
    final Producer<T,?> producer;

    public JsonGetter(MapToStringJson<T> mapToNodeToString, Producer<T, ?> producer) {
        this.mapToNodeToString = mapToNodeToString;
        this.producer = producer;
    }


    @Override
    public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper) throws ResourceGetter.ResourceGetterException {
        final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
        httpServletResponse.setStatus(200);
        final T produce;
        try {
            produce = this.producer.apply(null);
            final String asString =this.mapToNodeToString.apply(produce);
            @SuppressWarnings("resource")
			final OutputStream outputStream = httpServletResponse.getOutputStream();
            IOUtils.write(asString,outputStream);
            outputStream.flush();
        } catch (Throwable e) {
            throw new ResourceGetter.ResourceGetterException("Couldn't produce and convert to json",e);
        }
        return null;

    }
}
