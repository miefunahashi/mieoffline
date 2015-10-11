package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.functional.Function;
import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileupload.repository.postgres.SQLUtils;
import com.mieoffline.http.fileupload.repository.postgres.Utils;
import com.mieoffline.http.fileupload.repository.postgres.model.ReadGivenIdModel;
import com.mieoffline.http.fileuploadrepository.model.AvroDecoder;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.http.fileuploadrepository.model.Headers;
import com.mieoffline.site.Value;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants.*;

public class FileUploadPartRepositoryReadSimpleFileWithoutData
		implements Producer<DatabaseQueryDefinition<Long, FileUploadWithoutData<Long>>, Throwable> {
	private final AvroDecoder<Headers> headersAvroHelper;
	private final Utils utils;
	private final SQLUtils sqlUtils;

	public FileUploadPartRepositoryReadSimpleFileWithoutData(AvroDecoder<Headers> headersAvroHelper, Utils utils,
			SQLUtils sqlUtils) {
		this.headersAvroHelper = headersAvroHelper;
		this.utils = utils;
		this.sqlUtils = sqlUtils;
	}

	private FileUploadWithoutData<Long> getFileUpload(byte[] headers, String fileName, String name, String size,
			String contentType, DatabaseReference<Long> build, Headers objectFromBytes)
					throws FileUploadPartRepositoryReadSimpleFileException {
		final FileUploadWithoutData<Long> fileUpload;
		try {
			fileUpload = new FileUploadWithoutData.Builder<Long>().setReferenceToUpload(build).setFilename(fileName)
					.setName(name).setSize(Long.valueOf(size)).setContentType(contentType)
					.setFileHeaders(headers == null ? null : this.utils.headersToMap.apply(objectFromBytes)).build();
		} catch (Value.BuilderIncompleteException e) {
			throw new FileUploadPartRepositoryReadSimpleFileException("Cannot build file upload", e);
		}
		return fileUpload;
	}

	private Headers getHeaders(byte[] headers) throws FileUploadPartRepositoryReadSimpleFileException {
		final Headers objectFromBytes;
		try {
			objectFromBytes = this.headersAvroHelper.apply(headers);
		} catch (AvroDecoder.AvroDecoderException e) {
			throw new FileUploadPartRepositoryReadSimpleFileException("Unable to get headers map", e);
		}
		return objectFromBytes;
	}

	private DatabaseReference<Long> getLongDatabaseReference(long fileUploadId)
			throws FileUploadPartRepositoryReadSimpleFileException {
		final DatabaseReference<Long> build;
		try {
			build = new DatabaseReference.Builder<Long>().setReference(fileUploadId).build();
		} catch (Value.BuilderIncompleteException e) {
			throw new FileUploadPartRepositoryReadSimpleFileException("Couldn't build database reference", e);
		}
		return build;
	}

	@Override
	public DatabaseQueryDefinition<Long, FileUploadWithoutData<Long>> apply(Void aVoid) throws Throwable {
		return new DatabaseQueryDefinition.Builder<Long, FileUploadWithoutData<Long>>().setFunction(
				new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long>, FileUploadWithoutData<Long>, Throwable>() {
					@Override
					public FileUploadWithoutData<Long> apply(
							DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Long> longPreparedStatementWithQueryAndFunction)
									throws Throwable {
						final PreparedStatement ps = longPreparedStatementWithQueryAndFunction.getPreparedStatement();
						ps.setLong(1, longPreparedStatementWithQueryAndFunction.getT());
						try (final ResultSet result = ps.executeQuery()) {

							if (FileUploadPartRepositoryReadSimpleFileWithoutData.this.sqlUtils.isNext(result)) {
								final byte[] headers = result.getBytes(FILE_HEADERS_COLUMN);
								final long fileUploadId = result.getLong(FILE_UPLOAD_ID);
								final String fileName = result.getString(FILENAME_COLUMN);
								final String name = result.getString(NAME_COLUMN);
								final String size = result.getString(SIZE_COLUMN);
								final String contentType = result.getString(CONTENT_TYPE_COLUMN);
								final DatabaseReference<Long> build = getLongDatabaseReference(fileUploadId);
								final Headers objectFromBytes = getHeaders(headers);
								final FileUploadWithoutData<Long> fileUpload = getFileUpload(headers, fileName, name,
										size, contentType, build, objectFromBytes);
								return fileUpload;
							}
							throw new FileUploadPartRepositoryReadSimpleFileException("No result");

						}
					}
				}).setQuery(ReadGivenIdModel.READ_GIVEN_ID_WITHOUT_DATA).build();
	}

	public static class FileUploadPartRepositoryReadSimpleFileException extends Exception {

		private static final long serialVersionUID = 3752986050514676718L;

		public FileUploadPartRepositoryReadSimpleFileException(String s, Exception e) {
			super(s, e);
		}

		public FileUploadPartRepositoryReadSimpleFileException(String s) {
			super(s);
		}
	}
}
