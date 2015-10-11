package com.mieoffline.http.fileuploadrepository.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;

public class AvroDecoder<T extends SpecificRecordBase>
		implements com.mieoffline.functional.Function<byte[], T, AvroDecoder.AvroDecoderException> {
	private static final DecoderFactory decoderFactory = DecoderFactory.get();
	private final SpecificDatumReader<T> headersDatumReader;

	public AvroDecoder(Class<T> clazz) {
		this.headersDatumReader = new SpecificDatumReader<>(clazz);
	}

	@Override
	public T apply(byte[] bytes) throws AvroDecoderException {
		final BinaryDecoder decoder = decoderFactory.binaryDecoder(bytes, null);
		try (final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
			final T specificRecord = this.headersDatumReader.read(null, decoder);
			return specificRecord;
		} catch (IOException e) {
			throw new AvroDecoderException("Error decoding avro binary into java object", e);
		}
	}

	public static class AvroDecoderException extends Exception {

		private static final long serialVersionUID = -6722389028220259983L;

		public AvroDecoderException(String s, IOException e) {
			super(s, e);
		}
	}
}
