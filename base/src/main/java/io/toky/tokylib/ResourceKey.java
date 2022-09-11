package io.toky.tokylib;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ResourceKey<T> {
    public static final Key ROOT = Key.key("tokylib", "resource_keyed_root");

    private static final Map<String, ResourceKey<?>> VALUES = new ConcurrentHashMap<>();
    private final Key registryKey;
    private final Key entryKey;

    private ResourceKey(final Key registryKey, final Key entryKey) {
        this.registryKey = registryKey;
        this.entryKey = entryKey;
    }

    public static <T> ResourceKey<T> create(ResourceKey<? extends ResourceKeyed<T>> keyedReference, Key entryKey) {
        return ResourceKey.create(keyedReference.registryKey, entryKey);
    }

    public static <T> ResourceKey<T> create(Keyed keyed, Key entryKey) {
        return ResourceKey.create(keyed.key(), entryKey);
    }

    public static <T> ResourceKey<? extends ResourceKeyed<T>> createResourceKeyedKey(Key key) {
        return ResourceKey.create(ResourceKey.ROOT, key);
    }

    private static <T> ResourceKey<T> create(Key registryKey, Key entryKey) {
        return (ResourceKey<T>) ResourceKey.VALUES.computeIfAbsent(registryKey + ":" + entryKey, s -> new ResourceKey<>(registryKey, entryKey));
    }

    public Key registryKey() {
        return registryKey;
    }

    public Key entryKey() {
        return entryKey;
    }

    @Override
    public String toString() {
        return "ResourceKey[" + registryKey + " / " + entryKey + "]";
    }
}
