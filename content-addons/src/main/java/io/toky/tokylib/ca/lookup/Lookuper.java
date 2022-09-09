/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup;

import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import net.kyori.adventure.key.Key;

import java.util.Optional;

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
 * But where we supposed to instantiate "CustomEntity" type if it has native data? This is where {@link ContentLookup} is needed.
 * In your implementation you need to specify {@link ContentLookup} which will look for native "Entity" data,
 * for some reasons distinguish which entities should be "custom" and which shouldn't, make new instances of
 * "CustomEntity", hold them and after that {@link #load(Key)} "custom" data in them.
 * And when your "CustomEntity" dies you store "custom" data and release it in holder.
 * <p>
 * And congrats! You have fully configured "CustomEntity" instances in your holder, so you can handle with them in the way you prefer.
 */
public abstract class Lookuper {
	/**
	 * Does lookup process and holds looked up instances into specified holder.
	 * @param lookup lookup process to occur.
	 */
	public abstract <T, N> void lookup(ContentLookup<T, N> lookup);

	/**
	 * Does lookup process by specified {@link ContentAddon#lookup()}.
	 */
	public abstract void lookup(Key identifier);

	/**
	 * Stores "custom" data from every instance in specified holders by specified type.
	 */
	public abstract void store(Key identifier);

	/**
	 * Loads "custom" data in every instance in specified holders by specified type.
	 */
	public abstract void load(Key identifier);

	/**
	 * Obtain Marks by data addon types associated with them.
	 * @return mark associated with this Data Addon type.
	 */
	public abstract <T, N> Optional<TypeMark<T, N>> getCustomMark(Key identifier);

	/**
	 * Adds holder to this controller.
	 * @param typeInstanceHolder holder you want to add.
	 */
	public abstract void setTypeHolder(TypeInstanceHolder typeInstanceHolder);

	/**
	 * Obtains holders added in this {@link Lookuper}
	 * @return All added holders.
	 */
	public abstract TypeInstanceHolder getTypeHolder();
}