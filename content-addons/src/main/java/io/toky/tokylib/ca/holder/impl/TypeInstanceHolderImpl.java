package io.toky.tokylib.ca.holder.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.serialization.Codec;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.type.TypeRegistry;
import net.kyori.adventure.key.Key;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TypeInstanceHolderImpl extends TypeInstanceHolder {
    protected TypeRegistry typeRegistry;
    protected final Multimap<Class<?>, Object> instances =
            Multimaps.newMultimap(new ConcurrentHashMap<>(), ConcurrentHashMap::newKeySet);

    @Override
    public <T> void hold(T instance) {

    }

    @Override
    public <T> void release(T instance) {

    }

    @Override
    public <T> T release(Class<T> registrableType) {

    }

    @Override
    public <T> T release(ResourceKey<T> identifier) {

    }

    @Override
    public <T> boolean containsInstance(T registrable) {
        return false;
    }

    @Override
    public <T> Collection<T> getHeld(Class<T> registrableType) {
        return null;
    }

    @Override
    public <T> Collection<T> getHeld(ResourceKey<T> identifier) {
        return null;
    }

    @Override
    public void setTypeRegistry(TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    @Override
    public Codec<TypeInstanceHolder> codec() {
        return null;
    }

    @Override
    public void clearHeld() {

    }
}
