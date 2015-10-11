//package com.mieoffline.http.fileupload.repository.postgres;
//
//import com.google.common.collect.ImmutableList;
//import com.mieoffline.http.fileuploadrepository.model.AvroHelper;
//import com.mieoffline.http.fileuploadrepository.model.Headers;
//import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
//import com.mieoffline.site.Value;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.function.Function;
//
//public class FileUploadsRequestQueries {
//
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadsRequestQueries.class);
//
//
//
//    private final AvroHelper<Headers> headersAvroHelper = new AvroHelper<>(Headers.class);
//
//    private final Utils utils;
//
//    public FileUploadsRequestQueries(Utils utils) {
//
//        this.utils = utils;
//    }
//
//
//    public Function<Connection, Long> store(MultipartFormUploadRequestMissingFiles databaseFile) {
//        return new Function<Connection, Long>() {
//            @Override
//            public Long apply(Connection conn) {
//
//
//            }
//        };
//    }
//
//    public MultipartFormUploadRequestMissingFiles readSimpleFile(Long id, Connection conn) throws SQLException, IOException {
//
//    }
//
//    public ImmutableList<StringMultipartFormUploadRequestMissingFiles> readAll(Connection conn) throws FileUploadsRequestQueriesException {
//
//    }
//
//    public void create(Connection conn) throws SQLException {
//        LOGGER.info(CREATE_QUERY);
//        PreparedStatement ps = conn.prepareStatement(CREATE_QUERY);
//        try {
//            ps.execute();
//        } finally {
//            ps.close();
//        }
//
//    }
//
//    public void drop(Connection conn) throws SQLException {
//        LOGGER.info(DELETE_QUERY);
//        PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
//        try {
//            ps.execute();
//        } finally {
//            ps.close();
//        }
//    }
//    public static class FileUploadsRequestQueriesException extends Exception {
//
//        public FileUploadsRequestQueriesException(String s, Exception e) {
//            super(s,e);
//        }
//    }
//}
//
//
