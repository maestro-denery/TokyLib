/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup.impl;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.lookup.ContentMark;
import io.toky.tokylib.ca.type.ContentAddonCodecs;
import io.toky.tokylib.io.IO;

import java.io.File;
import java.util.Optional;

public class DataLookerUpperImpl extends DataLookerUpper {
    private ContentAddonContainer contentAddonContainer;

    @Override
    public <T, S> void lookup(ContentLookup<T, S> lookup) {
        lookup.lookup().forEach(tnPair -> {
            this.contentAddonContainer.hold(tnPair.getFirst());
        });
    }

    @Override
    public <T> void lookup(ResourceKey<T> identifier) {
        this.lookup(this.contentAddonContainer.getContentAddonRegistry().getContentLookup(identifier));
    }

    @Override
    public <D> void store(File dataFile, IO<D> io, DynamicOps<D> ops) {
        DataResult<D> dDataResult = ContentAddonCodecs.contentAddonContainerCodec(this.contentAddonContainer.getContentAddonRegistry()).encodeStart(ops, this.contentAddonContainer);
        dDataResult.map(data -> io.write(data, dataFile))
                .getOrThrow(false, s -> {
                    throw new RegistryException("Couldn't write ContentAddonContainer to a file for some reason! " + s);
                });
    }

    @Override
    public <D> void load(File dataFile, IO<D> io, DynamicOps<D> ops) {
        ContentAddonContainer contentAddonContainerDeserialized = io.read(dataFile).flatMap(data -> ContentAddonCodecs.contentAddonContainerCodec(this.contentAddonContainer.getContentAddonRegistry()).parse(ops, data))
                .getOrThrow(false, s -> {
                    throw new RegistryException("Couldn't write ContentAddonContainer to a file for some reason! " + s);
                });
        this.contentAddonContainer.merge(contentAddonContainerDeserialized);
    }

    @Override
    public <T> void storeStandaloneFile(ResourceKey<T> resourceKey) {
        final ContentLookup<T, ?> lookup = this.contentAddonContainer.getContentAddonRegistry().getContentLookup(resourceKey);
        lookup.standaloneFileOps(resourceKey).map(ops -> ops.write(resourceKey, this.contentAddonContainer.getHeld(resourceKey)))
                .orElseThrow(() -> new RegistryException("This ContentAddon doesn't support standalone file serialization: " + resourceKey));
    }

    @Override
    public <T> void loadStandaloneFile(ResourceKey<T> resourceKey) {
        final ContentLookup<T, ?> lookup = this.contentAddonContainer.getContentAddonRegistry().getContentLookup(resourceKey);
        final ContentAddonContainer.Entry<T> entry = lookup.standaloneFileOps(resourceKey).map(ops -> ops.read(resourceKey))
                .orElseThrow(() -> new RegistryException("This ContentAddon doesn't support standalone file serialization: " + resourceKey))
                .getOrThrow(false, s -> {
                    throw new RegistryException("Something went wrong while trying to read a standalone Entry file: " + s);
                });
        this.contentAddonContainer.holdAll(resourceKey, entry);
    }

    @Override
    public <T, S> Optional<ContentMark<T, S>> getCustomMark(ResourceKey<T> identifier) {
        return Optional.empty();
    }


    @Override
    public void setContentAddonContainer(ContentAddonContainer typeInstanceHolder) {
        this.contentAddonContainer = typeInstanceHolder;
    }

    @Override
    public ContentAddonContainer getContentAddonContainer() {
        return this.contentAddonContainer;
    }
}
