/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;


import com.google.common.collect.ImmutableMap;
import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.type.TypeRegistry;
import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;

/**
 * Container containing group tags and classes with their instances, it is needed only for {@link ContentAddonBootstrap}
 */
public class GroupContainer {
	private final Map<Key, Class<?>> contentAddons = new HashMap<>();
	private TypeRegistry tr;
	private TypeInstanceHolder tih;
	private Lookuper lookuper;

	public void registerTR(TypeRegistry tr) {
		if (this.tr != null) throw new RuntimeException("TR is already registered in a GroupContainer!");
		this.tr = tr;
	}

	public void registerTIH(TypeInstanceHolder tih) {
		if (this.tih != null) throw new RuntimeException("TIH is already registered in a GroupContainer!");
		this.tih = tih;
	}

	public void registerLookuper(Lookuper lookuper) {
		if (this.lookuper != null) throw new RuntimeException("Lookuper is already registered in a GroupContainer!");
		this.lookuper = lookuper;
	}

	public TypeRegistry typeRegistry() {
		return tr;
	}

	public TypeInstanceHolder tih() {
		return tih;
	}

	public Lookuper lookuper() {
		return lookuper;
	}

	public ImmutableMap<Key, Class<?>> contentAddons() {
		return ImmutableMap.copyOf(contentAddons);
	}

	public void registerContentAddon(Class<?> clazz) {
		contentAddons.put(Key.key(clazz.getAnnotation(ContentAddon.class).groupID()), clazz);
	}
}
