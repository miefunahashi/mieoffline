package com.mieoffline.server;

import java.sql.SQLException;

import com.google.common.collect.ImmutableSortedMap;
import com.jolbox.bonecp.BoneCP;
import com.mieoffline.functional.Consumer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.webservice.controller.ClasspathHttpGetter;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.server.postgres.Database;
import com.mieoffline.server.postgres.MapFromNodeToJsonString;
import com.mieoffline.server.postgres.TomcatService;
import com.mieoffline.server.postgres.controllers.base.BoneCPConnectionPoolSupplier;
import com.mieoffline.server.postgres.controllers.base.HtmlDefaultFileTypeMap;
import com.mieoffline.server.postgres.controllers.base.Raw;
import com.mieoffline.server.services.BaseServlet;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.server.services.HttpServletRequestResponseWrapperRouter;

public class MyApplication {
    public static void main(String args[]) throws Raw.RawException, MyApplicationException {
        final HtmlDefaultFileTypeMap fileTypeMap = new HtmlDefaultFileTypeMap();
        final MapFromNodeToJsonString serializer = new MapFromNodeToJsonString();
        final SQLUtils sqlUtils = new SQLUtils();
        final Utils utils = new Utils();
        final AvroDecoder<Headers> headersAvroDecoder = new AvroDecoder<>(Headers.class);
        try (final BoneCP config = new BoneCPConnectionPoolSupplier().apply(null)) {
            try (final Database connectionSupplier = new Database(config)) {
                final Raw rawService = new Raw(connectionSupplier, serializer, headersAvroDecoder, utils, sqlUtils);
                final ImmutableSortedMap<String, Consumer<HttpServletRequestResponseWrapper, ?>> consumers =
                        ImmutableSortedMap.of(
                                "", new RedirectConsumer(),
                                "web", new ClasspathHttpGetter(fileTypeMap, "site/"),
                                "_raw", rawService.apply(null));
                final Consumer<HttpServletRequestResponseWrapper, ?> consumer = new HttpServletRequestResponseWrapperRouter(
                        consumers);
                final BaseServlet baseServlet = new BaseServlet(consumer);
                startTomcat(baseServlet);
            }
        } catch (BoneCPConnectionPoolSupplier.BoneCPConnectionPoolSupplierException e) {
            throw new MyApplicationException("Could not create bone cp config", e);
        } catch (SQLException e) {
            throw new MyApplicationException("Could not setup database", e);
        }

    }

    private static void startTomcat(BaseServlet apply) throws MyApplicationException {
        try {
            new TomcatService(apply, 9090).apply(null);
        } catch (TomcatService.TomcatServiceException e) {
            throw new MyApplicationException("Error running tomcat", e);
        }
    }

    private static class MyApplicationException extends Exception {
        private static final long serialVersionUID = -6116616652179292725L;

        public MyApplicationException(String s, Exception e) {
            super(s, e);
        }
    }
}