package com.mieoffline.site;

import static org.junit.Assert.*;

public class ValueTest {

    public static <V extends Value<B,V>,B extends Value.Builder<V,B>> void assertVEquals(V actual) throws Value.BuilderIncompleteException {
        final V expected = actual.asBuilder().build();
        assertEquals(expected,actual);
 
        assertEquals(expected.hashCode(),actual.hashCode());
        assertEquals(expected.toString(),actual.toString());
 
    }
    public static <V extends Value<B,V>,B extends Value.Builder<V,B>> void assertVEquals(V expected, V actual)  {

        assertEquals(expected,actual);
        if(actual!=null) {
            assertEquals(expected.hashCode(),actual.hashCode());
            assertEquals(expected.toString(),actual.toString());
        }
    }
    public static  <V extends Value<B,V>,B extends Value.Builder<V,B>> void assertVNotEquals(V expected, V actual)  {
        assertNotEquals(expected,actual);
        if(expected!=null && actual !=null) {
            assertNotEquals(expected.hashCode(),actual.hashCode());
            assertNotEquals(expected.toString(),actual.toString());
        }
    }

}