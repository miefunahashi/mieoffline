package com.mieoffline.http.fileupload.repository.postgres.model;
import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;
public class FindWithoutDataByFileUploadIdModel {
    public final static String FIND_WITHOUT_DATA_BY_FILE_UPLOAD_ID = String.format(
            "SELECT %s, " +
                    "%s, " +
                    "%s, " +
                    "%s, " +
                    "%s, " +
                    "%s, " +
                    "%s " +
                    "FROM %s " +
                    "WHERE %s = ?",
            ID,
            FILE_UPLOAD_ID,
            FILENAME_COLUMN,
            CONTENT_TYPE_COLUMN,
            NAME_COLUMN,
            SIZE_COLUMN,
            FILE_HEADERS_COLUMN,
            FILE_UPLOAD_PART_TABLE_NAME,
            FILE_UPLOAD_ID);
}
