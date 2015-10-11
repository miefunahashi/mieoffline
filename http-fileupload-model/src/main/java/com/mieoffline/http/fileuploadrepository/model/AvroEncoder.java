package com.mieoffline.http.fileuploadrepository.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class AvroEncoder<T extends SpecificRecordBase>
		implements com.mieoffline.functional.Function<T, byte[], AvroEncoder.AvroEncoderException> {
	private final SpecificDatumWriter<T> specificDatumWriter;
	private static final EncoderFactory encoderFactory = EncoderFactory.get();

	public AvroEncoder(Class<T> clazz) {
		this.specificDatumWriter = new SpecificDatumWriter<>(clazz);
	}

//	private final static Function<ImmutableMap<String, ImmutableList<String>>, Map<CharSequence, List<CharSequence>>> transformForAvro = new Function<ImmutableMap<String, ImmutableList<String>>, Map<CharSequence, List<CharSequence>>>() {
//		@Override
//		public Map<CharSequence, List<CharSequence>> apply(
//				ImmutableMap<String, ImmutableList<String>> stringImmutableListImmutableMap) {
//
//			final ImmutableMap.Builder<CharSequence, List<CharSequence>> avroMap = ImmutableMap.builder();
//			for (Map.Entry<String, ImmutableList<String>> entry : stringImmutableListImmutableMap.entrySet()) {
//				avroMap.put(entry.getKey(), ImmutableList.<CharSequence> copyOf(entry.getValue()));
//			}
//			return avroMap.build();
//		}
//	};

	@Override
	public byte[] apply(T t) throws AvroEncoderException {
		try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			final BinaryEncoder binaryEncoder = encoderFactory.directBinaryEncoder(byteArrayOutputStream, null);
			this.specificDatumWriter.write(t, binaryEncoder);
			binaryEncoder.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new AvroEncoderException("Could not serialize java object into avro bytes", e);
		}
	}

	public static class AvroEncoderException extends Exception {

		private static final long serialVersionUID = -8700329978557352693L;

		public AvroEncoderException(String s, IOException e) {
			super(s, e);
		}
	}

}
