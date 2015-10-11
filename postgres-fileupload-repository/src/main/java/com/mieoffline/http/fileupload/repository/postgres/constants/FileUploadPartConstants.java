package com.mieoffline.http.fileupload.repository.postgres.constants;


public class FileUploadPartConstants {
    public final static String FILE_UPLOAD_PART_TABLE_NAME = "database_files";
    public final static String ID = "auto_identity";
    public final static String FILE_UPLOAD_ID = String.format("%s_%s", FileUploadsConstants.FILE_UPLOAD_REQUESTS_TABLE_NAME, FileUploadsConstants.ID_COLUMN);
    public final static String FILENAME_COLUMN = "filename";
    public final static String CONTENT_COLUMN = "content";
    public final static String CONTENT_TYPE_COLUMN = "content_type";
    public final static String NAME_COLUMN = "name";
    public final static String SIZE_COLUMN = "size";
    public final static String FILE_HEADERS_COLUMN = "file_headers";


    public final static String CREATE_QUERY = String.format(
            "CREATE TABLE %s" +
                    "(%s BIGSERIAL, " +
                    " %s BIGINT NOT NULL, " +
                    " %s TEXT NOT NULL, " +
                    " %s BYTEA NOT NULL, " +
                    " %s TEXT , " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s BYTEA, " +
                    " PRIMARY KEY (%s), " +
                    " FOREIGN KEY (%s) REFERENCES %s(%s)" +
                    ");" +
                    "CREATE INDEX idx_%s_%s " +
                    "ON %s(%s);" +
                    "CREATE INDEX idx_%s_%s " +
                    "ON %s(%s);" +
                    "CREATE INDEX idx_%s_%s " +
                    "ON %s(%s);" +
                    "CREATE INDEX idx_%s_%s " +
                    "ON %s(%s);",
            FILE_UPLOAD_PART_TABLE_NAME,
            ID,
            FILE_UPLOAD_ID,
            FILENAME_COLUMN,
            CONTENT_COLUMN,
            CONTENT_TYPE_COLUMN,
            NAME_COLUMN,
            SIZE_COLUMN,
            FILE_HEADERS_COLUMN,
            ID,
            FILE_UPLOAD_ID,
            FileUploadsConstants.FILE_UPLOAD_REQUESTS_TABLE_NAME,
            FileUploadsConstants.ID_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME, FILENAME_COLUMN, FILE_UPLOAD_PART_TABLE_NAME, FILENAME_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME, NAME_COLUMN, FILE_UPLOAD_PART_TABLE_NAME, NAME_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME, SIZE_COLUMN, FILE_UPLOAD_PART_TABLE_NAME, SIZE_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME, FILE_HEADERS_COLUMN, FILE_UPLOAD_PART_TABLE_NAME, FILE_HEADERS_COLUMN
    );
    public final static String DELETE_QUERY = String.format("DROP TABLE %s;", FILE_UPLOAD_PART_TABLE_NAME);


}
