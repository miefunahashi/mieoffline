package com.mieoffline.functional;

public interface Producer<PRODUCT,EXCEPTION extends Throwable> extends Function<Void,PRODUCT,EXCEPTION>{
}
