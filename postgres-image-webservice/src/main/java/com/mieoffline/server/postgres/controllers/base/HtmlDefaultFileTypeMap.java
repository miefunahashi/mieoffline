package com.mieoffline.server.postgres.controllers.base;

import javax.activation.FileTypeMap;

import com.mieoffline.functional.Function;

public class HtmlDefaultFileTypeMap
		implements Function<String, String, HtmlDefaultFileTypeMap.HtmlDefaultFileTypeMapException> {
	final FileTypeMap defaultFileTypeMap = FileTypeMap.getDefaultFileTypeMap();

	@Override
	public String apply(String s) throws HtmlDefaultFileTypeMapException {
		final String[] split = s.split("/");
		final String s1 = split[split.length - 1];
		final String contentType;
		try {
			contentType = this.defaultFileTypeMap.getContentType(s1);
		} catch (Exception exception) {
			throw new HtmlDefaultFileTypeMapException("Had no idea this could happen", exception);
		}
		return s1.indexOf('.') == -1 ? "text/html" : contentType;
	}

	public static class HtmlDefaultFileTypeMapException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1980533269630884544L;

		public HtmlDefaultFileTypeMapException(String s, Exception exception) {
			super(s, exception);
		}
	}
}
