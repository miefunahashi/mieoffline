package com.mieoffline.http.fileupload.repository.postgres.model;
import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;
public class ReadGivenIdModel {
    public final static String READ_GIVEN_ID = String.format(
            "SELECT %s, " +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s," +
                    "%s " +
                    "FROM %s " +
                    "WHERE %s = ?",
            FILE_UPLOAD_ID,
            FILENAME_COLUMN,
            CONTENT_COLUMN,
            CONTENT_TYPE_COLUMN,
            NAME_COLUMN,
            SIZE_COLUMN,
            FILE_HEADERS_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME,
            ID);
        public final static String READ_GIVEN_ID_WITHOUT_DATA = String.format(
                "SELECT %s, " +
                        "%s," +
                        "%s," +
                        "%s," +
                        "%s," +
                        "%s " +
                        "FROM %s " +
                        "WHERE %s = ?",
                FILE_UPLOAD_ID,
                FILENAME_COLUMN,
                CONTENT_TYPE_COLUMN,
                NAME_COLUMN,
                SIZE_COLUMN,
                FILE_HEADERS_COLUMN,
                FILE_UPLOAD_PART_TABLE_NAME,
                ID);
}
