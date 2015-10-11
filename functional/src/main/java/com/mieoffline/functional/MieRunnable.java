package com.mieoffline.functional;
public interface MieRunnable<E extends Throwable>  extends Function<Void,Void,E> {
    public static MieRunnable<Throwable> NULL_RUNNABLE = new MieRunnable<Throwable>() {
        @Override
        public Void apply(Void aVoid) throws Throwable {
            return null;
        }
    };

}
