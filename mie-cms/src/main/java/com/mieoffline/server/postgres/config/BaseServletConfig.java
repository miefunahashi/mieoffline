package com.mieoffline.server.postgres.config;

import com.google.common.collect.ImmutableSortedMap;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.server.postgres.MapFromNodeToJsonString;
import com.mieoffline.server.postgres.controllers.base.*;
import com.mieoffline.server.services.BaseServlet;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.server.services.HttpServletRequestResponseWrapperRouter;

public class BaseServletConfig implements Function<IDatabase, BaseServlet, BaseServletConfig.BaseServletException> {

    @Override
    public BaseServlet apply(IDatabase connectionSQLExceptionProducer) throws BaseServletException {
        final MapFromNodeToJsonString serializer = new MapFromNodeToJsonString();
        final SQLUtils sqlUtils = new SQLUtils();
        final Utils utils = new Utils();
        final AvroDecoder<Headers> headersAvroDecoder = new AvroDecoder<>(Headers.class);

        final AlbumsRootController albums = new AlbumsRootController(connectionSQLExceptionProducer, serializer, sqlUtils, utils, headersAvroDecoder);
        final Raw raw = new Raw(connectionSQLExceptionProducer, serializer, headersAvroDecoder, utils, sqlUtils);
        final HtmlDefaultFileTypeMap htmlDefaultFileTypeMap = new HtmlDefaultFileTypeMap();
        final Consumer<HttpServletRequestResponseWrapper, ?> webController;
        try {
            webController = new Web(htmlDefaultFileTypeMap).apply(null);
        } catch (Web.WebException e) {
            throw new BaseServletException("Exception setting up web controller", e);
        }
        final Consumer<HttpServletRequestResponseWrapper, ?> rawController;
        try {
            rawController = raw.apply(null);
        } catch (Raw.RawException e) {
            throw new BaseServletException("Error setting up raw assets controller", e);
        }
        final Consumer<HttpServletRequestResponseWrapper, ?> albumsController;
        try {
            albumsController = albums.apply(null);
        } catch (AlbumsRootController.AlbumsException e) {
            throw new BaseServletException("Error setting up album controller", e);
        }

        return new BaseServlet(new HttpServletRequestResponseWrapperRouter(
                ImmutableSortedMap.<String, Consumer<HttpServletRequestResponseWrapper, ?>>of(
                        "web", webController,
                        "albums", albumsController,
                        "raw", rawController


                )));
    }

    public static class BaseServletException extends Exception {

		private static final long serialVersionUID = -8958754470046051284L;

		public BaseServletException(String s, Exception e) {
            super(s, e);
        }
    }
}
