package com.mieoffline.server.model;

import com.mieoffline.site.Value;

public class AboutPage implements Value<AboutPage.Builder, AboutPage>, BasePage{

	private static final long serialVersionUID = -391712962767171374L;
	private final Header header;
	private AboutPage(Builder builder) {
		this.header = builder.header;
	}
	@Override
	public Header getHeader() {
		return this.header;
	}
	@Override
	public Builder newBuilder() {
		return new Builder();
	}
	@Override
	public Builder asBuilder() {
		return new Builder(this);
	}
	public static class Builder implements Value.Builder<AboutPage, Builder> {
		public Header header;
		public Builder() {}
		public Builder(AboutPage aboutPage) {
			this.header = aboutPage.header;
		}
		public Builder setHeader(Header header) {
			this.header = header;
			return this;
		}
		@Override
		public AboutPage build() throws Value.BuilderIncompleteException {
			final AboutPage aboutPage = new AboutPage(this);
			if(aboutPage.header == null) {
				throw Value.BuilderIncompleteException.exception("header");
			}
			return aboutPage;
		}
	}
}
