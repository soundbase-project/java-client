package com.soundhive.core.authentication;

@FunctionalInterface
public interface UserProfileConsumer {
    void accept(String username);

}
