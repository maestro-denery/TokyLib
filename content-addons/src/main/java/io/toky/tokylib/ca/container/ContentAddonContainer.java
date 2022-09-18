/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.container;

import io.toky.tokylib.DelegatingResourceKeyedCollection;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.container.impl.ContentAddonContainerImpl;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Class "holding" instances of {@link ContentAddonRegistry},
 * it is needed when you need to handle your "custom" types marked with {@link ContentAddon}.
 */
public abstract class ContentAddonContainer {

	/**
	 * "Holds" your {@link ContentAddon} instance until {@link #release(Class)} or {@link #clearHeld()}.
	 * After that you can get collection of this type instances using {@link #getHeld(Class)}.
	 * @param instance An instance you want to hold.
	 * @param <T> A type of instance you want to hold.
	 */
	public abstract <T> void hold(@NotNull T instance);

	/**
	 * Same as {@link #hold(Object)}, but holds collection of instances.
	 * @param resourceKey An ID of @{@link ContentAddon} you want to hold a collection of.
	 * @param instances A collection of instances you want to hold.
	 * @param <T> A type of instances you want to hold.
	 */
	public abstract <T> void holdAll(@NotNull ResourceKey<T> resourceKey, @NotNull Collection<T> instances);

	/**
	 * Same as {@link #holdAll(ResourceKey, Collection)}, but holds values of an {@link Entry}.
	 * @param resourceKey An ID of @{@link ContentAddon} you want to hold a collection of.
	 *  @param entry A collection of instances you want to hold.
	 *  @param <T> A type of instances you want to hold.
	 */
	public abstract <T> void holdAll(@NotNull ResourceKey<T> resourceKey, @NotNull Entry<T> entry);

	/**
	 * Removes your {@link ContentAddon} from holding in this holder.
	 * @param instance Instance you want to release.
	 * @param <T> Type of instance you want to release.
	 */
	public abstract <T> T release(@NotNull T instance);

	/**
	 * Removes your {@link ContentAddon} type from holding in this holder.
	 * @param contentAddonType type of instance you want to release.
	 * @param <T> Type of instance you want to release.
	 */
	public abstract <T> Optional<Entry<T>> release(@NotNull Class<T> contentAddonType);

	/**
	 * Same as {@link #release(Class)} but with String identifier.
	 * @param identifier Identifier of instances you want to release.
	 */
	public abstract <T> Optional<Entry<T>> release(@NotNull ResourceKey<T> identifier);

	/**
	 * Creates a new instance of a specified content addon type and holds it into a {@link ContentAddonContainer}.
	 * @param identifier An identifier of a content addon you want to create instance of.
	 * @return A new instance of a content addon
	 * @param <T> A type of content addon.
	 */
	@NotNull
	public abstract <T> T newInstance(@NotNull ResourceKey<T> identifier);

	/**
	 * Checks if this holder contains the following instance.
	 * @param instance {@link ContentAddon} instance you want to check.
	 * @param <T> Exact type of {@link ContentAddon}
	 * @return true if holder contains this instance.
	 */
	public abstract <T> boolean containsInstance(@NotNull T instance);

	/**
	 * Obtains all elements of type you want held by this holder.
	 * @param contentAddonType {@link ContentAddon} Class of Instances you want to get.
	 * @param <T> Exact type of {@link ContentAddon}.
	 * @return All instances held by this holder of your specific type.
	 */
	@NotNull
	public abstract <T> ContentAddonContainer.Entry<T> getHeld(@NotNull Class<T> contentAddonType);

	/**
	 * Same as {@link #getHeld(Class)} but get held instances by unique identifier.
	 * @param identifier unique identifier specified in {@link ContentAddon#identifier()}
	 * @param <T> Exact type of {@link ContentAddon}
	 * @return All instances held by this holder of your specific type.
	 */
	@NotNull
	public abstract <T> ContentAddonContainer.Entry<T> getHeld(@NotNull ResourceKey<T> identifier);

	/**
	 * Merges two {@link ContentAddonContainer} into one.
	 * @param contentAddonContainer A {@link ContentAddonContainer} you want to merge into the current one.
	 */
	public abstract void merge(@NotNull ContentAddonContainer contentAddonContainer);

	/**
	 * Purely internal API, if you implement your own {@link ContentAddonContainer}, you can throw {@link UnsupportedOperationException} in this method.
	 * We should think of a better way to make (de-)serialization hacks,
	 * @return A map representing this {@link ContentAddonContainer}.
	 */
	@ApiStatus.Internal
	public abstract Map<? extends ResourceKey<?>, ? extends Entry<?>> getInstancesInternalMap();

	/**
	 * Adds {@link ContentAddonRegistry} instance, so this instance can obtain various information about {@link ContentAddon} classes / keys etc.
	 * @param contentAddonRegistry {@link ContentAddonRegistry} you want to check in this holder.
	 */
	public abstract void setContentAddonRegistry(@NotNull ContentAddonRegistry contentAddonRegistry);

	/**
	 * Obtains {@link ContentAddonRegistry} added in this {@link ContentAddonContainer}.
	 * @return A {@link ContentAddonRegistry} instance previously set in this {@link ContentAddonContainer}.
	 */
	@NotNull
	public abstract ContentAddonRegistry getContentAddonRegistry();

	/**
	 * Clears all held instances from this holder.
	 */
	public abstract void clearHeld();

	public static ContentAddonContainer create() {
		return new ContentAddonContainerImpl();
	}

	public static class Entry<T> extends DelegatingResourceKeyedCollection<T> { // Maybe in the future we'll need some functional in it.
		protected Entry(ResourceKey<T> key, Collection<T> collection) {
			super(key, collection);
		}

		public static <T> Entry<T> create(ResourceKey<T> key, Collection<T> collection) {
			return new Entry<>(key, collection);
		}
	}
}
