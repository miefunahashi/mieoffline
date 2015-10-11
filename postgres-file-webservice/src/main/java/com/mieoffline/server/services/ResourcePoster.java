package com.mieoffline.server.services;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.mieoffline.functional.Consumer;
import com.mieoffline.http.fileupload.repository.postgres.PartCached;
import com.mieoffline.http.fileupload.repository.postgres.model.ResourcePosterModel;
import com.mieoffline.site.Value;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ResourcePoster
		implements Consumer<HttpServletRequestResponseWrapper, ResourcePoster.ResourcePosterException> {

	private static final String defaultEncoding = "UTF-8";
	private final Consumer<ResourcePosterModel, ?> resourcePosterModelConsumer;

	public ResourcePoster(Consumer<ResourcePosterModel, ?> resourcePosterModelConsumer) {
		this.resourcePosterModelConsumer = resourcePosterModelConsumer;
	}

	@Override
	public Void apply(HttpServletRequestResponseWrapper httpServletRequestResponseWrapper)
			throws ResourcePosterException {
		final HttpServletRequest httpServletRequest = httpServletRequestResponseWrapper.getHttpServletRequest();
		setEncoding(httpServletRequest);
		final HashMap<String, ImmutableList.Builder<String>> propertiesBuilder = new HashMap<>();
		final ImmutableList.Builder<PartCached> partsToStore = ImmutableList.builder();
		final Collection<Part> parts = getParts(httpServletRequest);
		for (final Part part : parts) {
			final String filename = part.getSubmittedFileName();
			if (filename == null) {
				addPartWithoutFilename(propertiesBuilder, part);
			} else {
				final ImmutableMap.Builder<String, ImmutableList<String>> headersBuilder = ImmutableMap
						.<String, ImmutableList<String>> builder();
				part.getHeaderNames().stream()
						.forEach(s -> headersBuilder.put(s, ImmutableList.copyOf(part.getHeaders(s))));
				final ImmutableMap<String, ImmutableList<String>> fileHeaders = headersBuilder.build();

				final byte[] data;
				try (final InputStream inputStream = part.getInputStream()) {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					IOUtils.copyLarge(inputStream, byteArrayOutputStream, new byte[1024 * 32]);
					data = byteArrayOutputStream.toByteArray();
				} catch (IOException e) {
					throw new ResourcePosterException("Unable to read part", e);
				}
				final PartCached build = new PartCached.Builder().setName(part.getName())
						.setContentType(part.getContentType()).setSize(part.getSize())
						.setSubmittedFileName(part.getSubmittedFileName())
						.setAlternativeFileName(part.getSubmittedFileName()).setHeaders(fileHeaders).setData(data)
						.build();
				partsToStore.add(build);
			}
		}

		final ImmutableMap.Builder<String, ImmutableList<String>> propertiesImmutableBuilder = ImmutableMap.builder();
		final ImmutableMap.Builder<String, String> alternativeFileNamesBuilder = ImmutableMap.builder();
		for (Map.Entry<String, ImmutableList.Builder<String>> entry : propertiesBuilder.entrySet()) {
			if (entry.getKey().startsWith("file.")) {
				alternativeFileNamesBuilder.put(entry.getKey().substring(5), entry.getValue().build().get(0));
			} else {

				propertiesImmutableBuilder.put(entry.getKey(), entry.getValue().build());
			}
		}
		final ImmutableMap<String, String> alternativeFileNames = alternativeFileNamesBuilder.build();

		ImmutableMap<String, ImmutableList<String>> propertiesImmutable = propertiesImmutableBuilder.build();

		final ImmutableList<String> albums1 = propertiesImmutable.get("albums");

		final ImmutableSet<Long> albums;
		if (albums1 == null) {
			albums = ImmutableSet.of();
		} else {
			albums = ImmutableSet.copyOf(Iterables.transform(albums1, new Function<String, Long>() {
				@Override
				public Long apply(String s) {
					return Long.valueOf(s);
				}
			}));
		}
		final ImmutableList<PartCached> build = partsToStore.build();
		final ImmutableList<PartCached> partToStoreRenamed = ImmutableList
				.copyOf(Collections2.transform(build, new Function<PartCached, PartCached>() {
					@Override
					public PartCached apply(PartCached partCached) {
						return partCached.asBuilder()
								.setAlternativeFileName(alternativeFileNames.get(partCached.getAlternativeFileName()))
								.build();
					}
				}));
		final ImmutableMap<String, ImmutableList<String>> requestHeaders = requestHeaders(httpServletRequest);
		final ResourcePosterModel resourcePosterModel;
		try {
			resourcePosterModel = new ResourcePosterModel.Builder().setAlbums(albums)
					.setPartToStoreRenamed(partToStoreRenamed).setPropertiesImmutable(propertiesImmutable)
					.setRequestHeaders(requestHeaders).build();
		} catch (Value.BuilderIncompleteException e) {
			throw new ResourcePosterException("Unable to build Resource poster model", e);
		}
		try {
			this.resourcePosterModelConsumer.apply(resourcePosterModel);
		} catch (Throwable e) {
			throw new ResourcePosterException("Error storing post request", e);
		}
		final HttpServletResponse httpServletResponse = httpServletRequestResponseWrapper.getHttpServletResponse();
		httpServletResponse.setStatus(200);
		return null;
	}

	private void addPartWithoutFilename(HashMap<String, ImmutableList.Builder<String>> propertiesBuilder, Part part) {
		final String name = part.getName();
		final String value = getValueOfPart(part);
		final ImmutableList.Builder<String> listBuilder = propertiesBuilder.get(name);
		if (listBuilder == null) {
			propertiesBuilder.put(name, ImmutableList.<String> builder().add(value));
		} else {
			listBuilder.add(value);
		}
	}

	private String getValueOfPart(Part part) {
		final String value;
		try {
			value = fromInputStream(part.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return value;
	}

	private Collection<Part> getParts(HttpServletRequest httpServletRequest) throws ResourcePosterException {
		final Collection<Part> parts;
		try {
			parts = httpServletRequest.getParts();
		} catch (IOException | ServletException e) {
			throw new ResourcePosterException("Unable to get http request parts", e);
		}
		return parts;
	}

	private void setEncoding(HttpServletRequest httpServletRequest) throws ResourcePosterException {
		try {
			String encoding = httpServletRequest.getCharacterEncoding();
			httpServletRequest.setCharacterEncoding(encoding == null ? defaultEncoding : encoding);
		} catch (UnsupportedEncodingException e) {
			throw new ResourcePosterException("Error encoding request", e);
		}
	}

	private final String fromInputStream(final InputStream inputStream) throws IOException {
		try {
			return IOUtils.toString(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private ImmutableMap<String, ImmutableList<String>> requestHeaders(HttpServletRequest httpServletRequest) {
		final ImmutableMap.Builder<String, ImmutableList<String>> uploadHeadersBuilder = ImmutableMap
				.<String, ImmutableList<String>> builder();
		final Enumeration<String> headerEnumerations = httpServletRequest.getHeaderNames();
		while (headerEnumerations.hasMoreElements()) {
			final String key = headerEnumerations.nextElement();
			final Enumeration<String> values = httpServletRequest.getHeaders(key);
			final ImmutableList.Builder<String> valueListBuilder = ImmutableList.builder();
			while (values.hasMoreElements()) {
				valueListBuilder.add(values.nextElement());
			}
			uploadHeadersBuilder.put(key, valueListBuilder.build());
		}
		return uploadHeadersBuilder.build();
	}

	public static class ResourcePosterException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2415453856180202118L;

		public ResourcePosterException(String s, Throwable e) {
			super(s, e);
		}
	}

}
