package io.toky.tokylib.ca.lookup.impl;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.lookup.TypeMark;

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
    public void store() {

    }

    @Override
    public void load() {

    }

    @Override
    public <T> void storeStandaloneFile(ResourceKey<T> identifier) {
        final ContentLookup<T, ?> lookup = this.tih.getTypeRegistry().getContentLookup(identifier);
        lookup.standaloneFileOps().map(ops -> ops.write(this.tih.getHeld(identifier)))
                .orElseThrow(() -> new RegistryException("This ContentAddon doesn't support standalone file serialization."));
    }

    @Override
    public <T> void loadStandaloneFile(ResourceKey<T> identifier) {
        final ContentLookup<T, ?> lookup = this.tih.getTypeRegistry().getContentLookup(identifier);
        final TypeInstanceHolder.TIHEntry<T> tihEntry = lookup.standaloneFileOps().map(ContentLookup.StandaloneFileOps::read)
                .orElseThrow(() -> new RegistryException("This ContentAddon doesn't support standalone file serialization."))
                .getOrThrow(false, s -> {
                    throw new RegistryException("Something went wrong while trying to read a standalone TIHEntry file: " + s);
                });
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
