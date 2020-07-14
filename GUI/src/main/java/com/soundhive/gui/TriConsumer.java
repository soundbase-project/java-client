package com.soundhive.gui;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T arg1, U arg2, V arg3);

}
