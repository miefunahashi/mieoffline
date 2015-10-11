package com.mieoffline.server;

import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Producer;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResourceListGetter implements Consumer<HttpServletResponse,ResourceListGetter.ResourceListGetterException> {
    private final Producer<String,?> getUploadsAsJsonString;

    public ResourceListGetter(Producer<String, ?> getUploadsAsJsonString) {
        this.getUploadsAsJsonString = getUploadsAsJsonString;
    }

    @SuppressWarnings("resource")
	@Override
    public Void apply(HttpServletResponse httpServletResponse) throws ResourceListGetterException {
        String produce = null;
        try {
            produce = this.getUploadsAsJsonString.apply(null);
        } catch (Throwable e) {
            throw new ResourceListGetterException("Unable to get Uploads as JSON",e);
        }
        try {
            final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            IOUtils.write(produce, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(200);
        return null;
    }

    public static class ResourceListGetterException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1298337203009391567L;

		public ResourceListGetterException(String s, Throwable e) {
            super(s,e);
        }
    }
}
