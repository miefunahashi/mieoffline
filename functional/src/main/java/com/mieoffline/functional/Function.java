package com.mieoffline.functional;

public interface Function<INPUT,OUTPUT,EXCEPTION extends Throwable> {
    public OUTPUT apply(INPUT input) throws EXCEPTION;

}
