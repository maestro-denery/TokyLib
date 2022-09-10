package io.toky.tokylib;

public interface ResourceKeyed<T> {
    ResourceKey<? extends ResourceKeyed<T>> key();
}
