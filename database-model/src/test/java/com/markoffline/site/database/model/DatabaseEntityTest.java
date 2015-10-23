package com.markoffline.site.database.model;

import com.mieoffline.site.Value;
import org.junit.Test;

import static com.mieoffline.site.ValueTest.assertVEquals;
import static com.mieoffline.site.ValueTest.assertVNotEquals;

public class DatabaseEntityTest {
    private static final Long DATABASE_REFERENCE_EXAMPLE = Long.valueOf("DATABASE_REFERENCE_EXAMPLE".hashCode());
    public static final DatabaseEntity<String> DATABASE_ENTITY_EXAMPLE = databaseEntityExample();
    public static final String OBJECT_EXAMPLE = "OBJECT_EXAMPLE";

    private static DatabaseEntity<String> databaseEntityExample() {
        try {
            return new DatabaseEntity.Builder<String>()
                    .setDatabaseReference(DATABASE_REFERENCE_EXAMPLE)
                    .setObject(OBJECT_EXAMPLE)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testEquality() throws Value.BuilderIncompleteException {
        assertVEquals(DATABASE_ENTITY_EXAMPLE);
    }

    @Test(expected = Value.BuilderIncompleteException.class)
    public void testNullReference() throws Value.BuilderIncompleteException {
        DATABASE_ENTITY_EXAMPLE.asBuilder().setDatabaseReference(null).build();
    }

    @Test(expected = Value.BuilderIncompleteException.class)
    public void testNullObject() throws Value.BuilderIncompleteException {
        DATABASE_ENTITY_EXAMPLE.asBuilder().setObject(null).build();
    }

    @Test
    public void testDifferent() throws Value.BuilderIncompleteException {
        assertVNotEquals(
                DATABASE_ENTITY_EXAMPLE,
                DATABASE_ENTITY_EXAMPLE.asBuilder()
                        .setDatabaseReference(DATABASE_REFERENCE_EXAMPLE + 1L).build());
        assertVNotEquals(
                DATABASE_ENTITY_EXAMPLE,
                DATABASE_ENTITY_EXAMPLE.asBuilder().setObject("OTHER").build());
    }
}