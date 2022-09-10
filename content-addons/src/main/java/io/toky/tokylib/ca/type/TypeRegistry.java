/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.lookup.ContentLookup;
import net.kyori.adventure.key.Keyed;

/**
 * Registry containing Unique DataAddon types where you can register and instantiate "custom" types.
 */
public abstract class TypeRegistry implements Keyed {
	/**
	 * Registers your types for further handling, storing and loading.
	 * @param contentAddonType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 */
	public abstract <T> ResourceKey<T> register(Class<T> contentAddonType);

	/**
	 * Checks if your {@link ContentAddon} implementation already registered.
	 * @param contentAddonType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return true if 
	 */
	public abstract boolean isRegistered(Class<?> contentAddonType);

	/**
	 * Obtains unique identifier by its class. 
	 * @param contentAddonType registered non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return unique identifier.
	 */
	public abstract <T> ResourceKey<T> getKey(Class<T> contentAddonType);

	/**
	 * Obtains {@link ContentAddon} class by its unique id.
	 * @param key unique id.
	 * @return {@link ContentAddon} class.
	 */
	public abstract <T> Class<T> getContentAddonType(ResourceKey<T> key);

	public abstract <T, D> ContentLookup<T, D> getContentLookup(ResourceKey<T> identifier);
}
