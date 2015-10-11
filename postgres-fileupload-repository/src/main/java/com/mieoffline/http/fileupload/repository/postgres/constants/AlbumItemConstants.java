package com.mieoffline.http.fileupload.repository.postgres.constants;

public class AlbumItemConstants {

    public final static String ALBUM_ITEMS_TABLE = "album_item";

    public final static String ID_COLUMN = "auto_identity";
    public final static String FILE_UPLOAD_KEY_COLUMN = "file_upload";
    public final static String ALBUM_KEY_COLUMN = "album";
    public final static String CREATE_QUERY = String.format(
            "CREATE TABLE %s" +
                    "(%s BIGSERIAL," +
                    " %s BIGINT NOT NULL, " +
                    " %s BIGINT NOT NULL, " +
                    " PRIMARY KEY (%s), " +
                    " FOREIGN KEY (%s) REFERENCES %s(%s)," +
                    " FOREIGN KEY (%s) REFERENCES %s(%s)" +

                    ");",
            ALBUM_ITEMS_TABLE,
            ID_COLUMN,
            FILE_UPLOAD_KEY_COLUMN,
            ALBUM_KEY_COLUMN,
            ID_COLUMN,
            FILE_UPLOAD_KEY_COLUMN, FileUploadPartConstants.FILE_UPLOAD_PART_TABLE_NAME, FileUploadPartConstants.ID,
            ALBUM_KEY_COLUMN, AlbumConstants.ALBUM_TABLE, AlbumConstants.ID_COLUMN

    );
    public final static String DELETE_QUERY = String.format("DROP TABLE %s;", ALBUM_ITEMS_TABLE);

}
