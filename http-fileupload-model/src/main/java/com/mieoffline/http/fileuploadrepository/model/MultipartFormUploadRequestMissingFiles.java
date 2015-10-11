package com.mieoffline.http.fileuploadrepository.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.site.Value;

public class MultipartFormUploadRequestMissingFiles implements Value<MultipartFormUploadRequestMissingFiles.Builder, MultipartFormUploadRequestMissingFiles> {

    private static final long serialVersionUID = -8290172749954239232L;
    private final ImmutableMap<String, ImmutableList<String>> properties;
    private final ImmutableMap<String, ImmutableList<String>> uploadHeaders;

    public MultipartFormUploadRequestMissingFiles(Builder builder) {
        this.properties = builder.properties;
        this.uploadHeaders = builder.uploadHeaders;
    }

    public ImmutableMap<String, ImmutableList<String>> getProperties() {
		return this.properties;
    }

    public ImmutableMap<String, ImmutableList<String>> getUploadHeaders() {
        return this.uploadHeaders;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }


    public static class Builder implements Value.Builder<MultipartFormUploadRequestMissingFiles, Builder> {

        private ImmutableMap<String, ImmutableList<String>> properties;
        private ImmutableMap<String, ImmutableList<String>> uploadHeaders;

        public Builder() {
        }

        public Builder(MultipartFormUploadRequestMissingFiles fileUploadRequest) {

            this.uploadHeaders = fileUploadRequest.uploadHeaders;
            this.properties = fileUploadRequest.properties;

        }

        public Builder setUploadHeaders(ImmutableMap<String, ImmutableList<String>> uploadHeaders) {
            this.uploadHeaders = uploadHeaders;
            return this;
        }


        public Builder setProperties(ImmutableMap<String, ImmutableList<String>> properties) {
            this.properties = properties;
            return this;
        }

        @Override
		public MultipartFormUploadRequestMissingFiles build() throws BuilderIncompleteException {
            final MultipartFormUploadRequestMissingFiles multipartFormUploadRequestMissingFiles = new MultipartFormUploadRequestMissingFiles(this);
            if (multipartFormUploadRequestMissingFiles.properties == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("properties").build();
            }
            if (multipartFormUploadRequestMissingFiles.uploadHeaders == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("uploadHeaders").build();
            }
            return multipartFormUploadRequestMissingFiles;
        }
    }
}
