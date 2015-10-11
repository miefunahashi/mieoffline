package com.mieoffline.http.fileuploadrepository.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class FileUploadWithoutData<DATA_TYPE extends Serializable>
		implements Serializable, Value<FileUploadWithoutData.Builder<DATA_TYPE>, FileUploadWithoutData<DATA_TYPE>> {
	private static final long serialVersionUID = -198559493726143963L;
	public static final String REFERENCE_TO_UPLOAD_FIELD = "referenceToUpload";
	public static final String FILENAME_FIELD = "filename";
	public static final String CONTENT_TYPE_FIELD = "contentType";
	public static final String NAME_FIELD = "name";
	public static final String SIZE_FIELD = "size";
	public static final String FILE_HEADERS_FIELD = "fileHeaders";
	private final DatabaseReference<DATA_TYPE> referenceToUpload;
	private final String filename;
	private final Optional<String> contentType;
	private final Optional<String> name;
	private final Optional<Long> size;
	private final ImmutableMap<String, ImmutableList<String>> fileHeaders;

	public FileUploadWithoutData(Builder<DATA_TYPE> builder) {
		this.referenceToUpload = builder.referenceToUpload;
		if (this.referenceToUpload == null) {
			throw new IllegalArgumentException();
		}
		this.filename = builder.filename;
		if (this.filename == null) {
			throw new IllegalArgumentException();
		}
		this.contentType = Optional.ofNullable(builder.contentType);
		this.name = Optional.ofNullable(builder.name);
		this.size = Optional.ofNullable(builder.size);

		this.fileHeaders = builder.fileHeaders;
		if (this.fileHeaders == null) {
			throw new IllegalArgumentException();
		}
	}

	public String getFilename() {
		return this.filename;
	}

	public Optional<String> getContentType() {
		return this.contentType;
	}

	public Optional<String> getName() {
		return this.name;
	}

	public Optional<Long> getSize() {
		return this.size;
	}

	public DatabaseReference<DATA_TYPE> getReferenceToUpload() {
		return this.referenceToUpload;
	}

	public ImmutableMap<String, ImmutableList<String>> getFileHeaders() {
		return this.fileHeaders;
	}

	@Override
	public Builder<DATA_TYPE> newBuilder() {
		return new Builder<>();
	}

	@Override
	public Builder<DATA_TYPE> asBuilder() {
		return new Builder<>(this);
	}

	@Override
	public String toString() {
		return String.format("%s{%s=%s, %s='%s', %s=%s, %s=%s, %s=%s, %s=%s}", FileUploadWithoutData.class.getName(),
				REFERENCE_TO_UPLOAD_FIELD, this.referenceToUpload, FILENAME_FIELD, this.filename, CONTENT_TYPE_FIELD,
				this.contentType, NAME_FIELD, this.name, SIZE_FIELD, this.size, FILE_HEADERS_FIELD, this.fileHeaders);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		FileUploadWithoutData<?> that = (FileUploadWithoutData<?>) o;
		return new EqualsBuilder().append(this.referenceToUpload, that.referenceToUpload)
				.append(this.filename, that.filename).append(this.contentType, that.contentType)
				.append(this.name, that.name).append(this.size, that.size).append(this.fileHeaders, that.fileHeaders)
				.build();

	}

	@Override
	public int hashCode() {
		return Objects.hash(this.referenceToUpload, this.filename, this.contentType, this.name, this.size,
				this.fileHeaders);

	}

	public static class Builder<DATA_TYPE extends Serializable>
			implements Value.Builder<FileUploadWithoutData<DATA_TYPE>, Builder<DATA_TYPE>> {
		private DatabaseReference<DATA_TYPE> referenceToUpload;
		private String filename;
		private String contentType;
		private String name;
		private Long size;
		private ImmutableMap<String, ImmutableList<String>> fileHeaders;

		public Builder() {
		}

		public Builder(FileUploadWithoutData<DATA_TYPE> databaseFile) {
			this.referenceToUpload = databaseFile.referenceToUpload;
			this.filename = databaseFile.filename;
			this.contentType = databaseFile.contentType.orElse(null);
			this.name = databaseFile.name.orElse(null);
			this.size = databaseFile.size.orElse(null);
			this.fileHeaders = databaseFile.fileHeaders;

		}

		public Builder<DATA_TYPE> setReferenceToUpload(DatabaseReference<DATA_TYPE> referenceToUpload) {
			this.referenceToUpload = referenceToUpload;
			return this;
		}

		public Builder<DATA_TYPE> setContentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder<DATA_TYPE> setName(String name) {
			this.name = name;
			return this;
		}

		public Builder<DATA_TYPE> setSize(Long size) {
			this.size = size;
			return this;
		}

		public Builder<DATA_TYPE> setFilename(String filename) {
			this.filename = filename;
			return this;
		}

		public Builder<DATA_TYPE> setFileHeaders(ImmutableMap<String, ImmutableList<String>> fileHeaders) {
			this.fileHeaders = fileHeaders;
			return this;
		}

		@Override
		public FileUploadWithoutData<DATA_TYPE> build() throws BuilderIncompleteException {

			final FileUploadWithoutData<DATA_TYPE> fileUploadWithoutData = new FileUploadWithoutData<>(this);
			if (fileUploadWithoutData.referenceToUpload == null) {
				throw BuilderIncompleteException.exception(REFERENCE_TO_UPLOAD_FIELD);
			}
			if (fileUploadWithoutData.filename == null) {
				throw BuilderIncompleteException.exception(FILENAME_FIELD);
			}
			if (fileUploadWithoutData.contentType == null) {
				throw BuilderIncompleteException.exception(CONTENT_TYPE_FIELD);
			}
			if (fileUploadWithoutData.name == null) {
				throw BuilderIncompleteException.exception(NAME_FIELD);
			}
			if (fileUploadWithoutData.size == null) {
				throw BuilderIncompleteException.exception(SIZE_FIELD);
			}
			if (fileUploadWithoutData.fileHeaders == null) {
				throw BuilderIncompleteException.exception(FILE_HEADERS_FIELD);
			}
			return fileUploadWithoutData;
		}

	}
}
