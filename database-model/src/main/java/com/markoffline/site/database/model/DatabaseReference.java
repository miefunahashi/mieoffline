package com.markoffline.site.database.model;

import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;


import java.io.Serializable;
import java.util.Objects;

public class DatabaseReference<DATA_TYPE extends Serializable> implements Value<DatabaseReference.Builder<DATA_TYPE>, DatabaseReference<DATA_TYPE>> {

   	private static final long serialVersionUID = -3939131960117432889L;
	public static final String REFERENCE_FIELD = "reference";
    private final DATA_TYPE reference;

    public DatabaseReference(Builder<DATA_TYPE> builder) {
        this.reference = builder.reference;
    }

    public DATA_TYPE getReference() {
        return this.reference;
    }

    @Override
    public DatabaseReference.Builder<DATA_TYPE> newBuilder() {
        return new Builder<>();
    }

    @Override
    public DatabaseReference.Builder<DATA_TYPE> asBuilder() {
        return new Builder<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseReference<?> that = (DatabaseReference<?>) o;
        return new EqualsBuilder().append(this.reference, that.reference).build();

    }

    @Override
    public String toString() {
        return String.format("%s{%s=%s}", DatabaseReference.class.getName(), REFERENCE_FIELD, this.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reference);
    }

    public static class Builder<DATA_TYPE extends Serializable> implements Value.Builder<DatabaseReference<DATA_TYPE>, Builder<DATA_TYPE>> {
        private DATA_TYPE reference;

        public Builder(DatabaseReference<DATA_TYPE> databaseReference) {
            this.reference = databaseReference.reference;
        }

        public Builder() {

        }

        public Builder<DATA_TYPE> setReference(DATA_TYPE reference) {
            this.reference = reference;
            return this;
        }

        @Override
        public DatabaseReference<DATA_TYPE> build() throws BuilderIncompleteException {
            final DatabaseReference<DATA_TYPE> data_typeDatabaseReference = new DatabaseReference<>(this);
            if(data_typeDatabaseReference.reference == null) {
                throw BuilderIncompleteException.exception(REFERENCE_FIELD);
            }
            return data_typeDatabaseReference;
        }
    }
}
