/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.container.impl;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ContentAddonContainerImpl extends ContentAddonContainer {
    protected ContentAddonRegistry contentAddonRegistry;
    protected final Map<ResourceKey<?>, Entry<?>> instances;

    private ContentAddonContainerImpl(Map<ResourceKey<?>, Entry<?>> map) {
        this.instances = Map.copyOf(map);
    }

    public ContentAddonContainerImpl() {
        this.instances = new HashMap<>();
    }

    @Override
    public <T> void hold(@NotNull T instance) {
        final ResourceKey<T> key = contentAddonRegistry.getKey(getClass(instance));
        final Entry<T> entry = createEntryIfAbsent(key);
        entry.add(instance);
        this.instances.put(key, entry);
    }

    @Override
    public <T> void holdAll(@NotNull ResourceKey<T> resourceKey, @NotNull Collection<T> instances) {
        Entry<T> entry = createEntryIfAbsent(resourceKey);
        entry.addAll(instances);
        this.instances.put(resourceKey, entry);
    }

    @Override
    public <T> void holdAll(@NotNull ResourceKey<T> resourceKey, @NotNull Entry<T> entry) {
        this.instances.put(resourceKey, entry);
    }

    @Override
    public <T> T release(@NotNull T instance) {
        Class<T> contentAddonType = getClass(instance);
        this.checkRegistered(contentAddonType);
        ResourceKey<T> key = contentAddonRegistry.getKey(contentAddonType);
        Optional.ofNullable(this.instances.get(key)).map(coll -> coll.remove(instance))
                .orElseThrow(() -> new RegistryException("There is no Entry with such ResourceKey: " + key));
        return instance;
    }

    public <T> @NotNull Optional<Entry<T>> release(@NotNull Class<T> contentAddonType) {
        this.checkRegistered(contentAddonType);
        return release(contentAddonRegistry.getKey(contentAddonType));
    }

    public <T> @NotNull Optional<Entry<T>> release(@NotNull ResourceKey<T> identifier) {
        return Optional.ofNullable((Entry<T>) instances.remove(identifier));
    }

    @Override
    public <T> @NotNull T newInstance(@NotNull ResourceKey<T> identifier) {
        final Class<T> contentAddonType = this.contentAddonRegistry.getContentAddonClass(identifier);
        this.checkRegistered(contentAddonType);
        try {
            final T instance = contentAddonType.getDeclaredConstructor().newInstance();
            this.hold(instance);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RegistryException("Can't create a new instance of the ContentAddon: " + identifier + " please check if your class complies the requirements.");
        }
    }

    @Override
    public <T> boolean containsInstance(@NotNull T instance) {
        Class<T> contentAddonType = getClass(instance);
        ResourceKey<T> key = contentAddonRegistry.getKey(contentAddonType);
        return Optional.ofNullable(this.instances.get(key)).map(coll -> coll.contains(instance))
                .orElseThrow(() -> new RegistryException("There is no Entry with such ResourceKey: " + key));
    }

    @Override
    public <T> @NotNull Entry<T> getHeld(@NotNull Class<T> contentAddonType) {
        return (Entry<T>) this.instances.get(contentAddonRegistry.getKey(contentAddonType));
    }

    @Override
    public <T> @NotNull Entry<T> getHeld(@NotNull ResourceKey<T> identifier) {
        return (Entry<T>) this.instances.get(identifier);
    }

    @Override
    public void merge(@NotNull ContentAddonContainer contentAddonContainer) {
        this.instances.putAll(contentAddonContainer.getInstancesInternalMap());
    }

    @Override
    public void setContentAddonRegistry(@NotNull ContentAddonRegistry contentAddonRegistry) {
        this.contentAddonRegistry = contentAddonRegistry;
    }

    @Override
    public @NotNull ContentAddonRegistry getContentAddonRegistry() {
        return this.contentAddonRegistry;
    }

    @Override
    public void clearHeld() {
        this.instances.clear();
    }

    public <T> void checkRegistered(Class<T> clazz) {
        if (!this.contentAddonRegistry.isRegistered(clazz)) throw new RegistryException("The class you are trying to hold in a ContentAddonContainer hasn't registered yet: " + clazz);
    }

    private <T> Class<T> getClass(T t) {
        return (Class<T>) t.getClass();
    }


    private <T> Entry<T> createEntryIfAbsent(ResourceKey<T> key) {
        return (Entry<T>) instances.computeIfAbsent(key, key1 -> Entry.create(key, new ArrayList<>()));
    }

    @ApiStatus.Internal
    public Map<? extends ResourceKey<?>, ? extends Entry<?>> getInstancesInternalMap() {
        return instances;
    }

    @ApiStatus.Internal
    public static ContentAddonContainerImpl createFromMap(Map<ResourceKey<?>, Entry<?>> map) {
        return new ContentAddonContainerImpl(map);
    }
}
