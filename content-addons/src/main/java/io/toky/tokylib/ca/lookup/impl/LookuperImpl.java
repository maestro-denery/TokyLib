package io.toky.tokylib.ca.lookup.impl;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.lookup.TypeMark;
import net.kyori.adventure.key.Key;

import java.util.Optional;

public class LookuperImpl extends Lookuper {
    private TypeInstanceHolder tih;

    @Override
    public <T, S> void lookup(ContentLookup<T, S> lookup) {
        lookup.lookup().forEach(tnPair -> {
            this.tih.hold(tnPair.getFirst());
        });
    }

    @Override
    public <T> void lookup(ResourceKey<T> identifier) {
        this.lookup(this.tih.getTypeRegistry().getContentLookup(identifier));
    }

    @Override
    public <T> void store(ResourceKey<T> identifier) {
        ContentLookup<T, ?> lookup = this.tih.getTypeRegistry().getContentLookup(identifier);
        lookup.io().write(this.tih.getHeld(identifier));
    }

    @Override
    public <T> void load(ResourceKey<T> identifier) {

    }

    @Override
    public <T, S> Optional<TypeMark<T, S>> getCustomMark(ResourceKey<T> identifier) {
        return Optional.empty();
    }


    @Override
    public void setTIH(TypeInstanceHolder typeInstanceHolder) {
        this.tih = typeInstanceHolder;
    }

    @Override
    public TypeInstanceHolder getTypeHolder() {
        return this.tih;
    }
}
