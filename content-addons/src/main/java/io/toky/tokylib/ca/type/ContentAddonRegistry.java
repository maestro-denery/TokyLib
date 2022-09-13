/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.type.impl.ContentAddonRegistryImpl;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

/**
 * Registry containing Unique DataAddon types where you can register and instantiate "custom" types.
 */
public abstract class ContentAddonRegistry implements Keyed {
	/**
	 * Registers your types for further handling, storing and loading.
	 * @param contentAddonType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 */
	@NotNull
	public abstract <T> ResourceKey<T> register(@NotNull Class<T> contentAddonType);

	/**
	 * Checks if your {@link ContentAddon} implementation already registered.
	 * @param contentAddonType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return true if 
	 */
	public abstract boolean isRegistered(@NotNull Class<?> contentAddonType);

	/**
	 * Obtains unique identifier by its class. 
	 * @param contentAddonType registered non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return unique identifier.
	 */
	@NotNull
	public abstract <T> ResourceKey<T> getKey(@NotNull Class<T> contentAddonType);

	/**
	 * Obtains {@link ContentAddon} class by its unique id.
	 * @param key unique id.
	 * @return {@link ContentAddon} class.
	 */
	@NotNull
	public abstract <T> Class<T> getContentAddonType(@NotNull ResourceKey<T> key);

	@NotNull
	public abstract <T, D> ContentLookup<T, D> getContentLookup(@NotNull ResourceKey<T> identifier);

    @NotNull
    public static ContentAddonRegistry create(@NotNull Key key) {
        return new ContentAddonRegistryImpl(key);
    }
}
