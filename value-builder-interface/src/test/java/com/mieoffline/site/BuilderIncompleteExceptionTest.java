package com.mieoffline.site;

import org.junit.Test;

import static com.mieoffline.site.ValueTest.assertVEquals;
import static com.mieoffline.site.ValueTest.assertVNotEquals;

public class BuilderIncompleteExceptionTest {
    public static final String EXAMPLE_MISSING_FELD = "EXAMPLE_MISSING_FELD";

    public static final Value.BuilderIncompleteException BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE = builderIncompleteException();

    private static Value.BuilderIncompleteException builderIncompleteException() {
        try {
            return new Value.BuilderIncompleteException.Builder().setMissingField(EXAMPLE_MISSING_FELD).build();
        } catch (Value.BuilderIncompleteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testSame() throws Exception {
        assertVEquals(BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE, BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE.asBuilder().build());
    }

    @Test(expected = Value.BuilderIncompleteException.class)
    public void testNull() throws Value.BuilderIncompleteException {
        BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE.asBuilder().setMissingField(null).build();
    }

    @Test
    public void testOther() throws Value.BuilderIncompleteException {
        assertVNotEquals(BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE, BUILDER_INCOMPLETE_EXCEPTION_EXAMPLE.asBuilder().setMissingField("OTHER").build());
    }


}