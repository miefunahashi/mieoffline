package com.markoffline.site.database.model;

import com.mieoffline.site.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

public class DatabaseEntity<KEY_TYPE extends Serializable, OBJECT_TYPE extends Serializable> implements Value<DatabaseEntity.Builder<KEY_TYPE, OBJECT_TYPE>, DatabaseEntity<KEY_TYPE, OBJECT_TYPE>> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2500323350409580295L;
	public static final String DATABASE_REFERENCE_FIELD = "databaseReference";
    public static final String OBJECT_FIELD = "object";
    private final DatabaseReference<KEY_TYPE> databaseReference;
    private final OBJECT_TYPE object;

    public DatabaseEntity(Builder<KEY_TYPE, OBJECT_TYPE> builder) {
        this.databaseReference = builder.databaseReference;
        this.object = builder.object;
    }

    public DatabaseReference<KEY_TYPE> getDatabaseReference() {
        return this.databaseReference;
    }

    public OBJECT_TYPE getObject() {
        return this.object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseEntity<?, ?> that = (DatabaseEntity<?, ?>) o;
        return new EqualsBuilder()
                .append(this.databaseReference, that.databaseReference)
                .append(this.object, that.object)
                .build();

    }


    @Override
    public int hashCode() {
        return Objects.hash(this.databaseReference, this.object);
    }

    @Override
    public String toString() {
        return String.format("%s{%s=%s, %s=%s}", DatabaseEntity.class.getName(), DATABASE_REFERENCE_FIELD, this.databaseReference, OBJECT_FIELD, this.object);
    }

    @Override
    public Builder<KEY_TYPE, OBJECT_TYPE> newBuilder() {
        return new Builder<>();
    }

    @Override
    public Builder<KEY_TYPE, OBJECT_TYPE> asBuilder() {
        return new Builder<>(this);
    }

    public static class Builder<KEY_TYPE extends Serializable, OBJECT_TYPE extends Serializable> implements Value.Builder<DatabaseEntity<KEY_TYPE, OBJECT_TYPE>, DatabaseEntity.Builder<KEY_TYPE, OBJECT_TYPE>> {
        private DatabaseReference<KEY_TYPE> databaseReference;
        private OBJECT_TYPE object;

        public Builder(DatabaseEntity<KEY_TYPE, OBJECT_TYPE> databaseEntity) {
            this.databaseReference = databaseEntity.databaseReference;
            this.object = databaseEntity.object;
        }

        public Builder() {

        }

        public Builder<KEY_TYPE, OBJECT_TYPE> setDatabaseReference(DatabaseReference<KEY_TYPE> databaseReference) {
            this.databaseReference = databaseReference;
            return this;
        }

        public Builder<KEY_TYPE, OBJECT_TYPE> setObject(OBJECT_TYPE object) {
            this.object = object;
            return this;
        }

        @Override
        public DatabaseEntity<KEY_TYPE, OBJECT_TYPE> build() throws BuilderIncompleteException {
            final DatabaseEntity<KEY_TYPE, OBJECT_TYPE> databaseEntity = new DatabaseEntity<>(this);
            if (databaseEntity.databaseReference == null) {
                throw BuilderIncompleteException.exception(DATABASE_REFERENCE_FIELD);
            }
            if (databaseEntity.object == null) {
                throw BuilderIncompleteException.exception(OBJECT_FIELD);
            }
            return databaseEntity;

        }
    }

}
