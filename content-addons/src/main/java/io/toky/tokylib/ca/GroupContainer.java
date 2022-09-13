/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;


import com.google.common.collect.ImmutableMap;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.holder.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;

/**
 * Container containing group tags and classes with their instances, it is needed only for {@link ContentAddonBootstrap}
 */
public class GroupContainer {
	private final Map<Key, Class<?>> contentAddons = new HashMap<>();
	private ContentAddonRegistry tr;
	private ContentAddonContainer tih;
	private DataLookerUpper dataLookerUpper;

	public void registerTR(ContentAddonRegistry tr) {
		if (this.tr != null) throw new RuntimeException("TR is already registered in a GroupContainer!");
		this.tr = tr;
	}

	public void registerTIH(ContentAddonContainer tih) {
		if (this.tih != null) throw new RuntimeException("TIH is already registered in a GroupContainer!");
		this.tih = tih;
	}

	public void registerLookuper(DataLookerUpper dataLookerUpper) {
		if (this.dataLookerUpper != null) throw new RuntimeException("Lookuper is already registered in a GroupContainer!");
		this.dataLookerUpper = dataLookerUpper;
	}

	public ContentAddonRegistry typeRegistry() {
		return tr;
	}

	public ContentAddonContainer tih() {
		return tih;
	}

	public DataLookerUpper lookuper() {
		return dataLookerUpper;
	}

	public ImmutableMap<Key, Class<?>> contentAddons() {
		return ImmutableMap.copyOf(contentAddons);
	}

	public void registerContentAddon(Class<?> clazz) {
		contentAddons.put(Key.key(clazz.getAnnotation(ContentAddon.class).groupID()), clazz);
	}
}
