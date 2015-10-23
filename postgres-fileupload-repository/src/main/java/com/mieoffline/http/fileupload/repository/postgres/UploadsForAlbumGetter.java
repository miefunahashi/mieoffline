package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableSet;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;

public class UploadsForAlbumGetter {
    private final Function<String, ImmutableSet<Long>, ?> albumItemsListAll;
    private final Function<Long, FileUploadWithoutData, ?> fileUploadPartRepositoryReadSimpleFile;

    public UploadsForAlbumGetter(Function<String, ImmutableSet<Long>, ?> albumItemsListAll, Function<Long, FileUploadWithoutData, ?> fileUploadPartRepositoryReadSimpleFile) {
        this.albumItemsListAll = albumItemsListAll;
        this.fileUploadPartRepositoryReadSimpleFile = fileUploadPartRepositoryReadSimpleFile;
    }

    public ImmutableSet<FileUploadWithoutData> apply(final String albumName) throws UploadsForAlbumGetterException {
        final ImmutableSet.Builder<FileUploadWithoutData> fileUploadWithoutDataListBuilder = ImmutableSet.builder();
        final ImmutableSet<Long> apply;
        try {
            apply = this.albumItemsListAll.apply(albumName);
        } catch (Throwable e) {
            throw new UploadsForAlbumGetter.UploadsForAlbumGetterException("Unable to get list of items", e);
        }
        for (Long longDatabaseReference : apply) {

            final FileUploadWithoutData fileUploadLong;
            try {
                fileUploadLong = this.fileUploadPartRepositoryReadSimpleFile.apply(longDatabaseReference);
            } catch (Throwable e) {
                throw new UploadsForAlbumGetterException("Unable to read a file upload", e);
            }
            fileUploadWithoutDataListBuilder.add(fileUploadLong);
        }


        return fileUploadWithoutDataListBuilder.build();
    }

    public static class UploadsForAlbumGetterException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 3372015199287625023L;

        public UploadsForAlbumGetterException(String s, Throwable e) {
            super(s, e);
        }
    }
}
