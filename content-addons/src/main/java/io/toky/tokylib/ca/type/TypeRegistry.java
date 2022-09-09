/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type;

import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.annotation.ContentAddon;
import net.kyori.adventure.key.Key;

/**
 * Registry containing Unique DataAddon types where you can register and instantiate "custom" types.
 */
public abstract class TypeRegistry {
	/**
	 * Registers your types for further handling, storing and loading.
	 * @param contentAddonType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 */
	public abstract void register(Class<?> contentAddonType);

	/**
	 * Checks if your {@link ContentAddon} implementation already registered.
	 * @param registrableType non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return true if 
	 */
	public abstract boolean isRegistered(Class<?> registrableType);

	/**
	 * Obtains unique identifier by its class. 
	 * @param registrableType registered non-abstract "custom" type implementing {@link ContentAddon} interface.
	 * @return unique identifier.
	 */
	public abstract Key getIdentifier(Class<?> registrableType);

	/**
	 * Obtains {@link ContentAddon} class by its unique id.
	 * @param identifier unique id.
	 * @return {@link ContentAddon} class.
	 */
	public abstract Class<?> getContentAddonType(Key identifier);
	
	/**
	 * clear everything in this {@link TypeRegistry}
	 */
	public abstract void clearRegistry(); 
}
