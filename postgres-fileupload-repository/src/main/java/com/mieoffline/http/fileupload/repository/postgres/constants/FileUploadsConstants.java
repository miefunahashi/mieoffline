package com.mieoffline.http.fileupload.repository.postgres.constants;

public class FileUploadsConstants {
    public final static String FILE_UPLOAD_REQUESTS_TABLE_NAME = "file_upload_requests";

    public final static String ID_COLUMN = "auto_identity";
    public final static String FORM_FIELD_VALUES_COLUMN = "form_field_values";
    public final static String UPLOAD_HEADERS_COLUMN = "upload_headers";

    public final static String CREATE_QUERY = String.format(
            "CREATE TABLE %s" +
                    "(%s BIGSERIAL," +
                    " %s BYTEA, " +
                    " %s BYTEA, " +
                    " PRIMARY KEY (%s));",
            FILE_UPLOAD_REQUESTS_TABLE_NAME,
            ID_COLUMN,
            FORM_FIELD_VALUES_COLUMN,
            UPLOAD_HEADERS_COLUMN,
            ID_COLUMN);

    public final static String DELETE_QUERY = String.format("DROP TABLE %s;", FILE_UPLOAD_REQUESTS_TABLE_NAME);
}
