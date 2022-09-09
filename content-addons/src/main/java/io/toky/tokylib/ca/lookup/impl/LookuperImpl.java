package io.toky.tokylib.ca.lookup.impl;

import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.lookup.TypeMark;
import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LookuperImpl extends Lookuper {
    private TypeInstanceHolder tih;
    private static final Map<Key, ContentLookup<?, ?>> CONTENT_LOOKUPS = new HashMap<>();

    @Override
    public <T, N> void lookup(ContentLookup<T, N> lookup) {
        lookup.lookup().forEach(tnPair -> {
            this.tih.hold(tnPair.getFirst());
        });
    }

    @Override
    public void lookup(Key identifier) {
        this.lookup(LookuperImpl.CONTENT_LOOKUPS.computeIfAbsent(identifier, key -> {
            try {
                return (ContentLookup<?, ?>) this.tih.getTypeRegistry().getContentAddonType(key).getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RegistryException("Check if your content addon / lookup matches requirements", e);
            }
        }));
    }

    @Override
    public void store(Key identifier) {
        CONTENT_LOOKUPS.get(identifier).io();
    }

    @Override
    public void load(Key identifier) {

    }

    @Override
    public <T, N> Optional<TypeMark<T, N>> getCustomMark(Key identifier) {
        return Optional.empty();
    }


    @Override
    public void setTypeHolder(TypeInstanceHolder typeInstanceHolder) {
        this.tih = typeInstanceHolder;
    }

    @Override
    public TypeInstanceHolder getTypeHolder() {
        return this.tih;
    }
}
