/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.storeload;

import java.util.Collection;

import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.holder.TypeHolder;

/**
 * Controller of "store load" mechanism of {@link ContentAddon} instances.
 * <p>
 * Definitions: in our "custom" Registrable type we have two types of data:
 * "custom" and "native".
 * <p>
 * Suppose we have "CustomEntity" class, in this class we have "EntityType entityType" field, and "EntityEmotions entityEmotions".
 * "entityType" exists in minecraft, and we want to have the same field that matches the original Entity's "entityType", it is native data.
 * On the other hand we have "entityEmotions" field which is our "custom" data attached to our "CustomEntity" type, it is custom data.
 * <p>
 * But where we supposed to instantiate "CustomEntity" type if it has native data? This is where {@link StoreLoadLookup} is needed.
 * In your implementation you need to specify {@link StoreLoadLookup} which will look for native "Entity" data,
 * for some reasons distinguish which entities should be "custom" and which shouldn't, make new instances of
 * "CustomEntity", hold them and after that {@link #load(Class)} "custom" data in them.
 * And when your "CustomEntity" dies you store "custom" data and release it in holder.
 * <p>
 * And congrats! You have fully configured "CustomEntity" instances in your holder, so you can handle with them in the way you prefer.
 */
public abstract class StoreLoadController {
	/**
	 * Stores "custom" data from every instance in specified holders by specified type.
	 * @param registrableType type you want to store.
	 */
	public abstract void store(Class<?> registrableType);

	/**
	 * Loads "custom" data in every instance in specified holders by specified type. 
	 * @param registrableType type you want to load.
	 */
	public abstract void load(Class<?> registrableType);

	/**
	 * Does lookup process and holds looked up instances into specified holder. 
	 * @param lookup lookup process to occur.
	 * @param <T> type of registrable in this lookup.
	 * @param <N> native type.
	 */
	public abstract <T, N> void lookup(StoreLoadLookup<T, N> lookup);

	/**
	 * Does lookup process by specified {@link ContentAddon#lookup()}.
	 * @param registrableType {@link ContentAddon} you want to do lookup.
	 * @param <T> exact {@link ContentAddon} type.
	 */
	public abstract <T> void lookup(Class<T> registrableType);

	/**
	 * Same as {@link #lookup(StoreLoadLookup)} but also loads all given DataAddons.
	 * @param lookup lookup process to occur.
	 * @param <T> type of registrable in this lookup.
	 * @param <N> native type.
	 */
	public abstract <T, N> void lookupAndLoad(StoreLoadLookup<T, N> lookup);

	/**
	 * Same as {@link #lookup(Class)} but also loads all given DataAddons.
	 * @param registrableType {@link ContentAddon} you want to do lookup.
	 * @param <T> type of registrable in this lookup.
	 */
	public abstract <T> void lookupAndLoad(Class<T> registrableType);

	/**
	 * Obtain Marks by data addon types associated with them.
	 * @param registrableType {@link ContentAddon} you want to obtain its marks.
	 * @param <T> type of registrable in this lookup.
	 * @return mark associated with this Data Addon type.
	 */
	public abstract <T> Mark<T, ?> getCustomMark(T registrableType);

	/**
	 * Adds holder to this controller.
	 * @param registrableHolder holder you want to add.
	 */
	public abstract void addTypeHolder(TypeHolder registrableHolder);

	/**
	 * Obtains holders added in this {@link StoreLoadController}
	 * @return All added holders.
	 */
	public abstract Collection<TypeHolder> getTypeHolders();
}
