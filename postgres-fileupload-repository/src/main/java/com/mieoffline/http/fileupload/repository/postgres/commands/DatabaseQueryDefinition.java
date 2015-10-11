package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Function;
import com.mieoffline.site.Value;

public class DatabaseQueryDefinition<S,T> implements Value<DatabaseQueryDefinition.Builder<S,T>,DatabaseQueryDefinition<S,T>>{
  
	private static final long serialVersionUID = 8558608424294810434L;
	private final String query;
    private final Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<S>, T, Throwable> function;

    public DatabaseQueryDefinition(Builder<S, T> builder) {
        this.query = builder.query;
        this.function = builder.function;
    }

    public String getQuery() {
        return this.query;
    }

    public Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<S>, T, Throwable> getFunction() {
        return this.function;
    }

    @Override
    public Builder<S, T> newBuilder() {
        return new Builder<>();
    }

    @Override
    public Builder<S, T> asBuilder() {
        return new Builder<>(this);
    }
    public static final Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void>, Void, Throwable> NULL_DATABASE_QUERY_FUNCTION = new Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void>, Void, Throwable>() {
        @Override
        public Void apply(DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<Void> sPreparedStatementWithQueryAndFunction) throws Throwable {
            return null;
        }
    };

    public static class Builder<S,T> implements Value.Builder<DatabaseQueryDefinition<S,T>,DatabaseQueryDefinition.Builder<S,T>> {

        public Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<S>, T, Throwable> function;
        public String query;
        public Builder() {

        }
        public Builder(DatabaseQueryDefinition<S, T> stDatabaseQueryDefinition) {
            this.function = stDatabaseQueryDefinition.function;
            this.query = stDatabaseQueryDefinition.query;
        }

        public Builder<S,T> setFunction(Function<DatabaseFunctionQuery.PreparedStatementWithQueryAndFunction<S>, T, Throwable> function) {
            this.function = function;
            return this;
        }

        public Builder<S,T> setQuery(String query) {
            this.query = query;
            return this;
        }

        @Override
        public DatabaseQueryDefinition<S, T> build() throws BuilderIncompleteException {
            return new DatabaseQueryDefinition<>(this);
        }
    }
}
