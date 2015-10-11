package com.markoffline.site.database.model;

import com.mieoffline.site.Value;
import org.junit.Test;

import static com.mieoffline.site.ValueTest.assertVEquals;
import static com.mieoffline.site.ValueTest.assertVNotEquals;


public class DatabaseReferenceTest {
    public final static String REFERENCE_EXAMPLE = "REFERENCE_EXAMPLE";
    public final static  DatabaseReference<String>  DATABASE_REFERENCE_EXAMPLE = databaseReferenceExample();
    private static DatabaseReference<String> databaseReferenceExample() {
        try {
            return new DatabaseReference.Builder<String>()
                    .setReference(REFERENCE_EXAMPLE)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testEquals() throws Value.BuilderIncompleteException {
        assertVEquals(DATABASE_REFERENCE_EXAMPLE);
    }
    @Test(expected = Value.BuilderIncompleteException.class)
    public void testNull() throws Value.BuilderIncompleteException {
        DATABASE_REFERENCE_EXAMPLE.asBuilder().setReference(null).build();
    }
    @Test
    public void testDifference() throws Value.BuilderIncompleteException {
        assertVNotEquals(
                DATABASE_REFERENCE_EXAMPLE,
                DATABASE_REFERENCE_EXAMPLE.asBuilder().setReference("OTHER").build());
    }

}