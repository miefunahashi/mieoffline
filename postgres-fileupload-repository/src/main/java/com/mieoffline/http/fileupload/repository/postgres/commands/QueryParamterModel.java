package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.site.Value;

public class QueryParamterModel implements Value<QueryParamterModel.Builder, QueryParamterModel>{

	private static final long serialVersionUID = 5311971091167394127L;
	private final Boolean returnGeneratedKeys;
	private final String query;
	public QueryParamterModel(Builder builder) {
		this.returnGeneratedKeys = builder.returnGeneratedKeys;
		this.query = builder.query;
	}
	
	public Boolean getReturnGeneratedKeys() {
		return this.returnGeneratedKeys;
	}

	public String getQuery() {
		return this.query;
	}

	public static class Builder implements Value.Builder<QueryParamterModel, Builder> {
		private Boolean returnGeneratedKeys;
		private String query;
		public Builder() {
			
		}
		public Builder(QueryParamterModel value) {
			this.returnGeneratedKeys = value.returnGeneratedKeys;
			this.query = value.query;
		}
		public Builder setReturnGeneratedKeys(Boolean returnGeneratedKeys) {
			this.returnGeneratedKeys = returnGeneratedKeys;
			return this;
		}
		public Builder setQuery(String query) {
			this.query = query;
			return this;
		}
		@Override
		public QueryParamterModel build() throws Value.BuilderIncompleteException {
			final QueryParamterModel value = new QueryParamterModel(this);
			if(value.returnGeneratedKeys == null) {
			throw Value.BuilderIncompleteException.exception("returnGeneratedKeys");
			}
			if(value.query == null) {
				throw Value.BuilderIncompleteException.exception("query");
			}
			
			return value;
		}
		
		
	}

	@Override
	public Builder newBuilder() {
		return new Builder();
	}

	@Override
	public Builder asBuilder() {
		return new Builder(this);
	}
}
