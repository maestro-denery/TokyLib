package io.toky.tokylib.ca.type.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.type.TypeRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistryImpl extends TypeRegistry {
    protected final Map<Key, ContentLookup<?, ?>> contentLookups = new HashMap<>();
    protected final BiMap<Key, Class<?>> registryBiMap = HashBiMap.create();
    protected final Key key;

    protected TypeRegistryImpl(Key key) {
        this.key = key;
    }

    @Override
    public <T> ResourceKey<T> register(Class<T> contentAddonType) {
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
    public boolean isRegistered(Class<?> contentAddonType) {
        ContentAddonBootstrap.checkAnnotation(contentAddonType);
        return registryBiMap.containsValue(contentAddonType);
    }


    @Override
    public <T> ResourceKey<T> getKey(Class<T> contentAddonType) {
        checkContains(contentAddonType);
        return ResourceKey.create(this, registryBiMap.inverse().get(contentAddonType));
    }

    @Override
    public <T> Class<T> getContentAddonType(ResourceKey<T> identifier) {
        if (registryBiMap.containsKey(identifier.entryKey())) {
            return (Class<T>) registryBiMap.get(identifier.entryKey());
        } else {
            throw new RegistryException("There is no registered type under this Identifier");
        }
    }

    @Override
    public <T, D> ContentLookup<T, D> getContentLookup(ResourceKey<T> identifier) {
        return (ContentLookup<T, D>) contentLookups.get(identifier.entryKey());
    }

    @Override
    public @NotNull Key key() {
        return this.key;
    }

    public BiMap<Key, Class<?>> getInternalBiMap() {
        return registryBiMap;
    }

    private <T> void checkContains(Class<T> clazz) {
        if (!registryBiMap.containsValue(clazz)) throw new RegistryException(clazz, "Registry doesn't contain this type, please register.");
    }
}
