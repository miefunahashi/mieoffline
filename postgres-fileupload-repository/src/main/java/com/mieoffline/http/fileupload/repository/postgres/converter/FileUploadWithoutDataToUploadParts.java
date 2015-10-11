package com.mieoffline.http.fileupload.repository.postgres.converter;

import com.mieoffline.functional.Function;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;

import java.util.Optional;

public class FileUploadWithoutDataToUploadParts implements Function<FileUploadWithoutData<Long>, UploadParts, Exception> {
    @Override
	public UploadParts apply(FileUploadWithoutData<Long> fileUploadWithoutData) {
        final Optional<String> optionalContentType = fileUploadWithoutData.getContentType();

        return new UploadParts.Builder()
                .setFileHeaders(fileUploadWithoutData.getFileHeaders())
                .setContentType(optionalContentType.orElse(null))
                .setReferenceToFileUploadRequestMissingData(fileUploadWithoutData.getReferenceToUpload().getReference().toString())
                .setFilename(fileUploadWithoutData.getFilename())
                .setName(fileUploadWithoutData.getName().orElse(null))
                .setIsImage(optionalContentType.isPresent() ? isImageTest(optionalContentType.get()) : null)
                .setSize(fileUploadWithoutData.getSize().isPresent() ? Long.toString(fileUploadWithoutData.getSize().get()) : null)
                .build();
    }

    private Boolean isImageTest(String s) {
        return s.toLowerCase().startsWith("image");
    }

}
