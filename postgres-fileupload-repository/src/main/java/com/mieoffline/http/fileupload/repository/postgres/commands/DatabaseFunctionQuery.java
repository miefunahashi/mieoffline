package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.functional.Consumer;
import com.mieoffline.functional.Function;
import com.mieoffline.site.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseFunctionQuery<S, T>
		implements Function<S, T, DatabaseFunctionQuery.DatabaseFunctionQueryException> {
	private final IDatabase connectionProducer;
	private Function<Connection, Function<S, T, Throwable>, Throwable> getPreparedStatement;

	public DatabaseFunctionQuery(IDatabase connectionProducer, DatabaseQueryDefinition<S, T> databaseQueryDefinition) {
		this.connectionProducer = connectionProducer;
		this.getPreparedStatement = connection -> (S s) -> {
			try (final PreparedStatement ps = connection.prepareStatement(databaseQueryDefinition.getQuery())) {
				final PreparedStatementWithQueryAndFunction<S> build = new PreparedStatementWithQueryAndFunction.Builder<S>()
						.setT(s).setPreparedStatement(ps).build();
				return databaseQueryDefinition.getFunction().apply(build);
			}
		};
	}

	@Override
	public T apply(final S v) throws DatabaseFunctionQueryException {
		final AtomicReference<T> goingToBeT = new AtomicReference<>();
		try {
			final Consumer<Connection, Throwable> connectionConsumer = (Connection conn) -> {
				try {
					goingToBeT.set(this.getPreparedStatement.apply(conn).apply(v));
					return null;
				} catch (Throwable throwable) {
					throw new IllegalStateException("It's all gone wrong", throwable);
				}
			};
			this.connectionProducer.execute(connectionConsumer);
			return goingToBeT.get();
		} catch (Throwable e) {
			throw new DatabaseFunctionQueryException("Unable to get connection", e);
		}

	}

	public static class DatabaseFunctionQueryException extends Exception {

		private static final long serialVersionUID = -7513089039026249553L;

		public DatabaseFunctionQueryException(String s, Throwable e) {
			super(s, e);
		}
	}

	public static class PreparedStatementWithQueryAndFunction<U> implements
			Value<PreparedStatementWithQueryAndFunction.Builder<U>, PreparedStatementWithQueryAndFunction<U>> {

		private static final long serialVersionUID = -3565205920225057377L;
		private final PreparedStatement preparedStatement;
		private final U t;

		public PreparedStatementWithQueryAndFunction(Builder<U> tBuilder) {
			this.preparedStatement = tBuilder.preparedStatement;
			this.t = tBuilder.t;
		}

		public PreparedStatement getPreparedStatement() {
			return this.preparedStatement;
		}

		public U getT() {
			return this.t;
		}

		@Override
		public Builder<U> newBuilder() {
			return new Builder<>();
		}

		@Override
		public Builder<U> asBuilder() {
			return new Builder<>(this);
		}

		public static class Builder<V> implements
				Value.Builder<PreparedStatementWithQueryAndFunction<V>, PreparedStatementWithQueryAndFunction.Builder<V>> {
			private PreparedStatement preparedStatement;
			private V t;

			public Builder(PreparedStatementWithQueryAndFunction<V> tPreparedSatementWithParameterModel) {
				this.preparedStatement = tPreparedSatementWithParameterModel.preparedStatement;
				this.t = tPreparedSatementWithParameterModel.t;
			}

			public Builder() {

			}

			public Builder<V> setPreparedStatement(PreparedStatement preparedStatement) {
				this.preparedStatement = preparedStatement;
				return this;
			}

			public Builder<V> setT(V t) {
				this.t = t;
				return this;
			}

			@Override
			public PreparedStatementWithQueryAndFunction<V> build() throws BuilderIncompleteException {
				return new PreparedStatementWithQueryAndFunction<>(this);
			}
		}
	}
}
