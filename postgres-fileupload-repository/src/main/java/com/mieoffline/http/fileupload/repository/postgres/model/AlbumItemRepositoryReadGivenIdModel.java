package com.mieoffline.http.fileupload.repository.postgres.model;

import com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants.*;

import static com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants.ALBUM_TABLE;
public class AlbumItemRepositoryReadGivenIdModel {
    public final static String READ_GIVEN_ID = String.join(" ",
            "SELECT",
            String.join(",", FILE_UPLOAD_KEY_COLUMN, ALBUM_KEY_COLUMN),
            "FROM",
            ALBUM_ITEMS_TABLE,
            "WHERE",
            ID_COLUMN,
            "= ?"
    );
    public final static String READ_ALL_ITEMS_FOR_ALBUM_NAME = String.join(" ",
                    "SELECT",
                    String.join(".", ALBUM_ITEMS_TABLE,FILE_UPLOAD_KEY_COLUMN),
                    "FROM",
                    String.join(",",ALBUM_TABLE,ALBUM_ITEMS_TABLE),
                    "WHERE",
                String.join(".", ALBUM_ITEMS_TABLE,ALBUM_KEY_COLUMN),
            "=",
            String.join(".", ALBUM_TABLE, AlbumConstants.ID_COLUMN),
            "and",
            String.join(".", ALBUM_TABLE, AlbumConstants.NAME_COLUMN),
            "=",
            "?"
            );

}
