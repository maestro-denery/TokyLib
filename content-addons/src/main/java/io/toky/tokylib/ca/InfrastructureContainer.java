/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;

import com.google.common.collect.ImmutableMap;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class containing instances of {@link ContentAddonRegistry}, {@link ContentAddonContainer}, {@link DataLookerUpper}
 * and collection of {@link ContentAddon} annotated classes. It is needed mainly for the {@link ContentAddonBootstrap} class.
 */
public class InfrastructureContainer {
	private final Map<Key, Class<?>> contentAddons = new HashMap<>();
	private ContentAddonRegistry contentAddonRegistry;
	private ContentAddonContainer contentAddonContainer;
	private DataLookerUpper dataLookerUpper;

	public void registerContentAddonRegistry(ContentAddonRegistry tr) {
		if (this.contentAddonRegistry != null) throw new RuntimeException("ContentAddonRegistry is already registered in this GroupContainer!");
		this.contentAddonRegistry = tr;
	}

	public void registerContentAddonContainer(ContentAddonContainer tih) {
		if (this.contentAddonContainer != null) throw new RuntimeException("ContentAddonContainer is already registered in this GroupContainer!");
		this.contentAddonContainer = tih;
	}

	public void registerLookerUpper(DataLookerUpper dataLookerUpper) {
		if (this.dataLookerUpper != null) throw new RuntimeException("DataLookerUpper is already registered in this GroupContainer!");
		this.dataLookerUpper = dataLookerUpper;
	}

	public ContentAddonRegistry contentAddonRegistry() {
		return contentAddonRegistry;
	}

	public ContentAddonContainer contentAddonContainer() {
		return contentAddonContainer;
	}

	public DataLookerUpper dataLookerUpper() {
		return dataLookerUpper;
	}

	public ImmutableMap<Key, Class<?>> contentAddons() {
		return ImmutableMap.copyOf(contentAddons);
	}

	public void registerContentAddon(Class<?> clazz) {
		contentAddons.put(Key.key(clazz.getAnnotation(ContentAddon.class).identifier()), clazz);
	}
}
