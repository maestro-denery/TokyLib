package io.toky.tokylib.ca.lookup.impl;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.holder.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.lookup.TypeMark;

import java.util.Optional;

public class DataLookerUpperImpl extends DataLookerUpper {
    private ContentAddonContainer tih;

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
        final ContentAddonContainer.TIHEntry<T> tihEntry = lookup.standaloneFileOps().map(ContentLookup.StandaloneFileOps::read)
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
    public void setTIH(ContentAddonContainer typeInstanceHolder) {
        this.tih = typeInstanceHolder;
    }

    @Override
    public ContentAddonContainer getTypeHolder() {
        return this.tih;
    }
}
