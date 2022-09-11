/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.holder;

import io.toky.tokylib.DelegatingResourceKeyedCollection;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.type.TypeRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Class "holding" instances of {@link TypeRegistry},
 * it is needed when you need to handle your "custom" types marked with {@link ContentAddon}.
 */
public abstract class TypeInstanceHolder {

	/**
	 * "Holds" your {@link ContentAddon} instance until {@link #release(Class)} or {@link #clearHeld()}.
	 * After that you can get collection of this type instances using {@link #getHeld(Class)}.
	 * @param instance An instance you want to hold.
	 * @param <T> A type of instance you want to hold.
	 */
	public abstract <T> void hold(@NotNull T instance);

	/**
	 * Same as {@link #hold(Object)}, but holds collection of instances.
	 * @param identifier An ID of @{@link ContentAddon} you want to hold a collection of.
	 * @param instances A collection of instances you want to hold.
	 * @param <T> A type of instances you want to hold.
	 */
	public abstract <T> void holdAll(@NotNull ResourceKey<? extends ResourceKeyed<T>> identifier, @NotNull Collection<T> instances);

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
	public abstract <T> Optional<TIHEntry<T>> release(@NotNull Class<T> contentAddonType);

	/**
	 * Same as {@link #release(Class)} but with String identifier.
	 * @param identifier Identifier of instances you want to release.
	 */
	public abstract <T> Optional<TIHEntry<T>> release(@NotNull ResourceKey<T> identifier);

	/**
	 * Creates a new instance of a specified content addon type and holds it into a {@link TypeInstanceHolder}.
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
	public abstract <T> TIHEntry<T> getHeld(@NotNull Class<T> contentAddonType);

	/**
	 * Same as {@link #getHeld(Class)} but get held instances by unique identifier.
	 * @param identifier unique identifier specified in {@link ContentAddon#identifier()}
	 * @param <T> Exact type of {@link ContentAddon}
	 * @return All instances held by this holder of your specific type.
	 */
	@NotNull
	public abstract <T> TIHEntry<T> getHeld(@NotNull ResourceKey<? extends ResourceKeyed<T>> identifier);

	/**
	 * Adds TypeRegistry checks to this holder, if you are making your implementations you need to specify TypeRegistries for your holder.
	 * @param typeRegistry {@link TypeRegistry} you want to check in this holder.
	 */
	public abstract void setTypeRegistry(@NotNull TypeRegistry typeRegistry);

	/**
	 * Obtains TypeRegistries added in this {@link TypeInstanceHolder}
	 * @return All added TypeRegistries.
	 */
	@NotNull
	public abstract TypeRegistry getTypeRegistry();

	/**
	 * Clears all held instances from this holder.
	 */
	public abstract void clearHeld();

	public static class TIHEntry<T> extends DelegatingResourceKeyedCollection<T> { // Maybe in the future we'll need some functional in it.
		protected TIHEntry(ResourceKey<? extends ResourceKeyed<T>> key, Collection<T> collection) {
			super(key, collection);
		}

		public static <T> TIHEntry<T> create(ResourceKey<? extends ResourceKeyed<T>> key, Collection<T> collection) {
			return new TIHEntry<>(key, collection);
		}
	}
}
