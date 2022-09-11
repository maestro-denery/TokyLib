package io.toky.tokylib.ca.holder.impl;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.type.TypeRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TypeInstanceHolderImpl extends TypeInstanceHolder {
    protected TypeRegistry typeRegistry;
    protected final Map<ResourceKey<? extends ResourceKeyed<?>>, TIHEntry<?>> instances = new HashMap<>();

    @Override
    public <T> void hold(@NotNull T instance) {
        final ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey = getResourceKeyedKey(getClass(instance));
        this.instances.put(resourceKeyedKey, getC(resourceKeyedKey));
    }

    @Override
    public <T> void holdAll(@NotNull ResourceKey<? extends ResourceKeyed<T>> identifier, @NotNull Collection<T> instances) {
        this.instances.put(identifier, TIHEntry.create(identifier, instances));
    }

    @Override
    public <T> T release(@NotNull T instance) {
        Class<T> contentAddonType = getClass(instance);
        this.checkRegistered(contentAddonType);
        ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey = getResourceKeyedKey(contentAddonType);
        Optional.ofNullable(this.instances.get(resourceKeyedKey)).map(coll -> coll.remove(instance))
                .orElseThrow(() -> new RegistryException("There is no TIHEntry with such ResourceKey: " + resourceKeyedKey));
        return instance;
    }

    public <T> @NotNull Optional<TIHEntry<T>> release(@NotNull Class<T> contentAddonType) {
        this.checkRegistered(contentAddonType);
        ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey = getResourceKeyedKey(contentAddonType);
        return Optional.ofNullable((TIHEntry<T>) instances.remove(resourceKeyedKey));
    }

    public <T> @NotNull Optional<TIHEntry<T>> release(@NotNull ResourceKey<T> identifier) {
        return this.release(typeRegistry.getContentAddonType(identifier));
    }

    @Override
    public <T> @NotNull T newInstance(@NotNull ResourceKey<T> identifier) {
        final Class<T> contentAddonType = this.typeRegistry.getContentAddonType(identifier);
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
        ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey = getResourceKeyedKey(contentAddonType);
        return Optional.ofNullable(this.instances.get(resourceKeyedKey)).map(coll -> coll.contains(instance))
                .orElseThrow(() -> new RegistryException("There is no TIHEntry with such ResourceKey: " + resourceKeyedKey));
    }

    @Override
    public <T> @NotNull TIHEntry<T> getHeld(@NotNull Class<T> contentAddonType) {
        return (TIHEntry<T>) this.instances.get(getResourceKeyedKey(contentAddonType));
    }

    @Override
    public <T> @NotNull TIHEntry<T> getHeld(@NotNull ResourceKey<? extends ResourceKeyed<T>> identifier) {
        return (TIHEntry<T>) this.instances.get(identifier);
    }

    @Override
    public void setTypeRegistry(@NotNull TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public @NotNull TypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    @Override
    public void clearHeld() {
        this.instances.clear();
    }

    public <T> void checkRegistered(Class<T> clazz) {
        if (this.typeRegistry.isRegistered(clazz)) throw new RegistryException("The class you are trying to hold in a TIH hasn't registered yet: " + clazz);
    }

    private <T> Class<T> getClass(T t) {
        return (Class<T>) t.getClass();
    }

    private <T> ResourceKey<? extends ResourceKeyed<T>> getResourceKeyedKey(Class<T> contentAddonType) {
        return ResourceKey.createResourceKeyedKey(typeRegistry.getKey(contentAddonType).entryKey());
    }

    private <T> TIHEntry<T> getC(ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey) {
        return (TIHEntry<T>) instances.computeIfAbsent(resourceKeyedKey, clazz -> TIHEntry.create(resourceKeyedKey, new ArrayList<>()));
    }

    @ApiStatus.Internal
    public Map<? extends ResourceKey<? extends ResourceKeyed<?>>, ? extends TIHEntry<?>> getInstancesInternalMap() {
        return instances;
    }
}
