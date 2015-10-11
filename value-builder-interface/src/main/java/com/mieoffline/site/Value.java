package com.mieoffline.site;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Objects;

public interface Value<B extends Value.Builder<V, B>, V extends Value<B, V>> extends Serializable {

    B newBuilder();

    B asBuilder();

    interface Builder<V extends Value<B, V>, B extends Value.Builder<V, B>> {
        V build() throws BuilderIncompleteException;
    }

    class BuilderIncompleteException extends Exception implements Value<BuilderIncompleteException.Builder, BuilderIncompleteException> {
        /**
		 * 
		 */
		private static final long serialVersionUID = 8684119145525274325L;
		public static final String MISSING_FIELD_FIELD = "missingField";
        private final String missingField;

        private BuilderIncompleteException(String missingField) {
            super("Failed to build value due to missing field: " + missingField);
            this.missingField = missingField;

        }

        public static BuilderIncompleteException exception(final String missingField) throws BuilderIncompleteException {
            return new BuilderIncompleteException.Builder().setMissingField(missingField).build();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BuilderIncompleteException that = (BuilderIncompleteException) o;

            return new EqualsBuilder()
                    .append(this.missingField, that.missingField)
                    .build();

        }

        @Override
        public int hashCode() {
            return Objects.hash(this.missingField);
        }

        @Override
        public String toString() {
            return String.format("%s{%s='%s'}",
                    BuilderIncompleteException.class.getName(), MISSING_FIELD_FIELD, this.missingField);
        }

        @Override
        public Builder newBuilder() {
            return new Builder();
        }

        @Override
        public Builder asBuilder() {
            return new Builder(this);
        }

        public static class Builder
                implements Value.Builder<BuilderIncompleteException, BuilderIncompleteException.Builder> {

            private String missingField;

            public Builder(BuilderIncompleteException e) {
                this.missingField = e.missingField;
            }

            public Builder() {

            }

            public Builder setMissingField(String missingField) {
                this.missingField = missingField;
                return this;
            }

            @Override
            public BuilderIncompleteException build() throws BuilderIncompleteException {
                final String currentMissingField = this.missingField;
                if (currentMissingField == null) {
                    throw new BuilderIncompleteException.Builder()
                            .setMissingField(MISSING_FIELD_FIELD)
                            .build();
                }
                return new BuilderIncompleteException(currentMissingField);

            }
        }
    }
}
