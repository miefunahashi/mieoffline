package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableSet;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Function;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;

public class UploadsForAlbumGetter {
    private final Function<String, ImmutableSet<DatabaseReference<Long>>, ?>  albumItemsListAll;
    private final Function<Long, FileUploadWithoutData<Long>, ?> fileUploadPartRepositoryReadSimpleFile;

    public UploadsForAlbumGetter(Function<String, ImmutableSet<DatabaseReference<Long>>, ?> albumItemsListAll, Function<Long, FileUploadWithoutData<Long>, ?> fileUploadPartRepositoryReadSimpleFile) {
        this.albumItemsListAll = albumItemsListAll;
        this.fileUploadPartRepositoryReadSimpleFile = fileUploadPartRepositoryReadSimpleFile;
    }

    public ImmutableSet<FileUploadWithoutData<Long>> apply(final String albumName) throws UploadsForAlbumGetterException {
        final ImmutableSet.Builder<FileUploadWithoutData<Long>> fileUploadWithoutDataListBuilder = ImmutableSet.builder();
        final ImmutableSet<DatabaseReference<Long>> apply;
        try {
            apply= this.albumItemsListAll.apply(albumName);
        } catch (Throwable e) {
            throw new UploadsForAlbumGetter.UploadsForAlbumGetterException("Unable to get list of items",e);
        }
        for(DatabaseReference<Long> longDatabaseReference : apply) {
            final Long reference = longDatabaseReference.getReference();
            final FileUploadWithoutData<Long> fileUploadLong;
            try {
                fileUploadLong = this.fileUploadPartRepositoryReadSimpleFile.apply(reference);
            } catch (Throwable e) {
                throw new UploadsForAlbumGetterException("Unable to read a file upload",e);
            }
            fileUploadWithoutDataListBuilder.add(fileUploadLong);
        }


        return fileUploadWithoutDataListBuilder.build();
    }
    public static class UploadsForAlbumGetterException extends Exception{

        /**
		 * 
		 */
		private static final long serialVersionUID = 3372015199287625023L;

		public UploadsForAlbumGetterException(String s,Throwable e) {
            super(s,e);
        }
    }
}
