/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class ContentAddonRegistryImpl extends ContentAddonRegistry {
    protected final Map<Key, ContentLookup<?, ?>> contentLookups = new HashMap<>();
    protected final BiMap<Key, Class<?>> registryBiMap = HashBiMap.create();
    protected final Key key;

    public ContentAddonRegistryImpl(Key key) {
        this.key = key;
    }

    @Override
    public <T> @NotNull ResourceKey<T> register(@NotNull Class<T> contentAddonType) {
        ContentAddonBootstrap.checkAnnotation(contentAddonType);
        final Key id = Key.key(contentAddonType.getAnnotation(ContentAddon.class).identifier());
        this.contentLookups.computeIfAbsent(id, key -> {
            try {
                return contentAddonType.getAnnotation(ContentAddon.class).lookup().getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RegistryException("Check if your content addon / lookup matches requirements", e);
            }
        });
        registryBiMap.put(id, contentAddonType);
        return ResourceKey.create(this, id);
    }

    @Override
    public boolean isRegistered(@NotNull Class<?> contentAddonType) {
        ContentAddonBootstrap.checkAnnotation(contentAddonType);
        return registryBiMap.containsValue(contentAddonType);
    }


    @Override
    public <T> @NotNull ResourceKey<T> getKey(@NotNull Class<T> contentAddonType) {
        checkContains(contentAddonType);
        return ResourceKey.create(this, registryBiMap.inverse().get(contentAddonType));
    }

    @Override
    public <T> @NotNull Class<T> getContentAddonClass(@NotNull ResourceKey<T> identifier) {
        if (registryBiMap.containsKey(identifier.entryKey()))
            return (Class<T>) registryBiMap.get(identifier.entryKey());
        else throw new RegistryException("This TypeRegistry doesn't contain class with such resourcekey: " + identifier);
    }

    @Override
    public <T, D> @NotNull ContentLookup<T, D> getContentLookup(@NotNull ResourceKey<T> identifier) {
        if (registryBiMap.containsKey(identifier.entryKey()))
            return (ContentLookup<T, D>) contentLookups.get(identifier.entryKey());
        else throw new RegistryException("This TypeRegistry doesn't contain class with such resourcekey: " + identifier);
    }

    @Override
    public @NotNull Key key() {
        return this.key;
    }

    public BiMap<Key, Class<?>> getInternalBiMap() {
        return registryBiMap;
    }

    private <T> void checkContains(Class<T> clazz) {
        if (!registryBiMap.containsValue(clazz)) throw new RegistryException(clazz, "TypeRegistry doesn't contain this type, please register.");
    }
}
