package com.mieoffline.http.fileupload.repository.postgres.constants;

public class AlbumConstants {
    public final static String ALBUM_TABLE = "album";

    public final static String ID_COLUMN = "auto_identity";
    public final static String NAME_COLUMN = "name";
    public final static String DESCRIPTION_COLUMN = "description";
    public final static String CREATE_QUERY = String.format(
            "CREATE TABLE %s" +
                    "(%s BIGSERIAL," +
                    " %s TEXT NOT NULL, " +
                    " %s TEXT NOT NULL, " +
                    " PRIMARY KEY (%s) " +
                    ");" +
                    "CREATE UNIQUE INDEX idx_%s_%s " +
                    "ON %s(%s);" +
                    "CREATE INDEX idx_%s_%s " +
                    "ON %s(%s);",
            ALBUM_TABLE,
            ID_COLUMN,
            NAME_COLUMN,
            DESCRIPTION_COLUMN,
            ID_COLUMN,
            ALBUM_TABLE, NAME_COLUMN, ALBUM_TABLE, NAME_COLUMN,
            ALBUM_TABLE, DESCRIPTION_COLUMN, ALBUM_TABLE, DESCRIPTION_COLUMN
    );
    public final static String DELETE_QUERY = String.format("DROP TABLE %s;", ALBUM_TABLE);

}
