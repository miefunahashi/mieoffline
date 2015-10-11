package com.mieoffline.http.fileupload.repository.postgres.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mieoffline.http.fileupload.repository.postgres.PartCached;
import com.mieoffline.site.Value;

public class ResourcePosterModel implements Value<ResourcePosterModel.Builder, ResourcePosterModel> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6775932974170569820L;
	private final ImmutableMap<String, ImmutableList<String>> requestHeaders;
    private final ImmutableMap<String, ImmutableList<String>> propertiesImmutable;
    private final ImmutableSet<Long> albums;
    private final ImmutableList<PartCached> partToStoreRenamed;

    public ResourcePosterModel(Builder builder) {
        this.albums = builder.albums;
        this.partToStoreRenamed = builder.partToStoreRenamed;
        this.requestHeaders = builder.requestHeaders;
        this.propertiesImmutable = builder.propertiesImmutable;
    }

    public ImmutableMap<String, ImmutableList<String>> getRequestHeaders() {
        return this.requestHeaders;
    }

    public ImmutableMap<String, ImmutableList<String>> getPropertiesImmutable() {
        return this.propertiesImmutable;
    }

    public ImmutableSet<Long> getAlbums() {
        return this.albums;
    }

    public ImmutableList<PartCached> getPartToStoreRenamed() {
        return this.partToStoreRenamed;
    }

    @Override
    public Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Builder asBuilder() {
        return new Builder(this);
    }

    public static class Builder implements Value.Builder<ResourcePosterModel, Builder> {
        private ImmutableMap<String, ImmutableList<String>> requestHeaders;
        private ImmutableMap<String, ImmutableList<String>> propertiesImmutable;
        private ImmutableSet<Long> albums;
        private ImmutableList<PartCached> partToStoreRenamed;

        public Builder(ResourcePosterModel resourcePosterModel) {
            this.requestHeaders = resourcePosterModel.requestHeaders;
            this.propertiesImmutable = resourcePosterModel.propertiesImmutable;
            this.albums = resourcePosterModel.albums;
            this.partToStoreRenamed = resourcePosterModel.partToStoreRenamed;
        }

        public Builder() {

        }

        public Builder setRequestHeaders(ImmutableMap<String, ImmutableList<String>> requestHeaders) {
            this.requestHeaders = requestHeaders;
            return this;
        }

        public Builder setPropertiesImmutable(ImmutableMap<String, ImmutableList<String>> propertiesImmutable) {
            this.propertiesImmutable = propertiesImmutable;
            return this;
        }

        public Builder setAlbums(ImmutableSet<Long> albums) {
            this.albums = albums;
            return this;
        }

        public Builder setPartToStoreRenamed(ImmutableList<PartCached> partToStoreRenamed) {
            this.partToStoreRenamed = partToStoreRenamed;
            return this;
        }

        @Override
        public ResourcePosterModel build() throws BuilderIncompleteException {
            ResourcePosterModel resourcePosterModel = new ResourcePosterModel(this);
            if(resourcePosterModel.requestHeaders == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("requestHeaders").build();
            }
            if(resourcePosterModel.propertiesImmutable == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("propertiesImmutable").build();
            }
            if(resourcePosterModel.albums == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("albums").build();
            }
            if(resourcePosterModel.partToStoreRenamed == null) {
                throw new BuilderIncompleteException.Builder().setMissingField("partToStoreRenamed").build();
            }
            return resourcePosterModel;
        }
    }

}
