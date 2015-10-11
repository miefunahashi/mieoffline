package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableList;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.commands.DatabaseFunctionQuery;
import com.mieoffline.http.fileupload.repository.postgres.converter.FileUploadWithoutDataToUploadParts;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.http.fileuploadrepository.model.MultipartFormUploadRequestMissingFiles;
import com.mieoffline.site.Value;

public class UploadsProducer implements Producer<ImmutableList<Upload>, UploadsProducer.UploadsAsJsonStringProducerException> {

    private final Function<Long, ImmutableList<FileUploadWithoutData<Long>>, ?> fileUploadPartRepositoryReadSimpleFiles;
    private final DatabaseFunctionQuery<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>> fileUploadsReadAll2;
    private final FileUploadWithoutDataToUploadParts fileUploadWithoutDataToUploadParts;

    public UploadsProducer(Function<Long, ImmutableList<FileUploadWithoutData<Long>>, ?> fileUploadPartRepositoryReadSimpleFiles, DatabaseFunctionQuery<Void, ImmutableList<StringMultipartFormUploadRequestMissingFiles>> fileUploadsReadAll2, FileUploadWithoutDataToUploadParts fileUploadWithoutDataToUploadParts) {
        this.fileUploadPartRepositoryReadSimpleFiles = fileUploadPartRepositoryReadSimpleFiles;
        this.fileUploadsReadAll2 = fileUploadsReadAll2;
        this.fileUploadWithoutDataToUploadParts = fileUploadWithoutDataToUploadParts;
    }

    @Override
    public ImmutableList<Upload> apply(Void avoid) throws UploadsAsJsonStringProducerException {
        final ImmutableList.Builder<Upload> uploadsImmutableList = ImmutableList.<Upload>builder();
        final ImmutableList<StringMultipartFormUploadRequestMissingFiles> multipartFormUploadRequestMissingFileses;
        try {
            multipartFormUploadRequestMissingFileses = this.fileUploadsReadAll2.apply(null);

        } catch (Throwable e) {
            throw new UploadsAsJsonStringProducerException("Unable to read string multipart form upload request metadata from database", e);
        }
        for (StringMultipartFormUploadRequestMissingFiles stringMultipartFormUploadRequestMissingFiles : multipartFormUploadRequestMissingFileses) {
            ImmutableList.Builder<UploadParts> uploadBuilder = ImmutableList.<UploadParts>builder();
            try {
                for (final FileUploadWithoutData<Long> fileUploadWithoutData : this.fileUploadPartRepositoryReadSimpleFiles.apply(stringMultipartFormUploadRequestMissingFiles.getIdentifier())) {
                    uploadBuilder.add(this.fileUploadWithoutDataToUploadParts.apply(fileUploadWithoutData));
                }
            } catch (Throwable e) {
                throw new UploadsAsJsonStringProducerException("Unable to get file upload part repository", e);
            }
            final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles = stringMultipartFormUploadRequestMissingFiles.getMultipartFormUploadRequestMissingFiles();
            try {
                uploadsImmutableList.add(new Upload.Builder()
                        .setUploads(uploadBuilder.build())
                        .setHttpHeaders(multipartFormUploadRequestMissingFiles.getUploadHeaders())
                        .setProperties(multipartFormUploadRequestMissingFiles.getProperties())

                        .build());
            } catch (Value.BuilderIncompleteException e) {
                throw new UploadsAsJsonStringProducerException("Unable to build uploads", e);
            }
        }
        return uploadsImmutableList.build();
    }


    public static class UploadsAsJsonStringProducerException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -4822819273603809537L;

		public UploadsAsJsonStringProducerException(String s, Throwable e) {
            super(s, e);
        }
    }
}
