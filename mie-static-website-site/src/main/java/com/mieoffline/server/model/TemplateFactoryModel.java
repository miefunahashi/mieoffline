package com.mieoffline.server.model;

import com.google.common.base.Function;
import com.mieoffline.server.services.HttpServletRequestResponseWrapper;
import com.mieoffline.site.Value;

public class TemplateFactoryModel<T> implements Value<TemplateFactoryModel.Builder<T>, TemplateFactoryModel<T>>{

	private static final long serialVersionUID = -391712962767171374L;
	private final String template;
	private final String contentType;
	private final Function<HttpServletRequestResponseWrapper, T> getTheT;
	private TemplateFactoryModel(Builder<T> builder) {
		this.template = builder.template;
		this.contentType = builder.contentType;
		this.getTheT = builder.getTheT;
	}
	public String getTemplate() {
		return this.template;
	}
	public Function<HttpServletRequestResponseWrapper, T> getGetTheT() {
		return this.getTheT;
	}
	public String getContentType() {
		return this.contentType;
	}
	@Override
	public Builder<T> newBuilder() {
		return new Builder<>();
	}
	@Override
	public Builder<T> asBuilder() {
		return new Builder<>(this);
	}
	public static class Builder<T> implements Value.Builder<TemplateFactoryModel<T>, Builder<T>> {
		public String contentType;
		public String template;
		private Function<HttpServletRequestResponseWrapper, T> getTheT;
		public Builder() {}
		public Builder(TemplateFactoryModel<T> aboutPage) {
			this.template = aboutPage.template;
			this.contentType  = aboutPage.contentType;
			this.getTheT = aboutPage.getTheT;
		}
		public Builder<T> setGetTheT(Function<HttpServletRequestResponseWrapper, T> getTheT) {
			this.getTheT = getTheT;
			return this;
		}
		public Builder<T> setTemplate(String template) {
			this.template = template;
			return this;
		}
		public Builder<T> setContentType(String contentType) {
			this.contentType = contentType;
			return this;
		}
		@Override
		public TemplateFactoryModel<T> build() throws Value.BuilderIncompleteException {
			final TemplateFactoryModel<T> templateFactoryModel = new TemplateFactoryModel<>(this);
			if(templateFactoryModel.template == null) {
				throw Value.BuilderIncompleteException.exception("template");
			}
			if(templateFactoryModel.contentType== null) {
				throw Value.BuilderIncompleteException.exception("contentType");
			}
			if(templateFactoryModel.getTheT == null) {
				throw Value.BuilderIncompleteException.exception("getTheT");
			}
			return templateFactoryModel;
		}
	}
}
