/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.annotation.AnnotationUtil;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.annotation.ContentAddon;
import net.kyori.adventure.key.Key;

public class DefaultTypeRegistry extends TypeRegistry {
	protected final BiMap<Key, Class<?>> registryBiMap = HashBiMap.create();

	@Override
	public void register(Class<?> contentAddonType) {
		AnnotationUtil.checkAnnotation(contentAddonType);
		registryBiMap.put(Key.key(contentAddonType.getAnnotation(ContentAddon.class).identifier()), contentAddonType);
	}

	@Override
	public boolean isRegistered(Class<?> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		return registryBiMap.containsValue(registrableType);
	}

	@Override
	public void clearRegistry() {
		registryBiMap.clear();
	}

	@Override
	public Key getIdentifier(Class<?> registrableType) {
		checkContains(registrableType);
		return registryBiMap.inverse().get(registrableType);
	}

	@Override
	public Class<?> getContentAddonType(Key identifier) {
		if (registryBiMap.containsKey(identifier)) {
			return registryBiMap.get(identifier);
		} else {
			throw new RegistryException("There is no registered type under this Identifier");
		}
	}

	public BiMap<Key, Class<?>> getInternalBiMap() {
		return registryBiMap;
	}
	
	private <T> void checkContains(Class<T> clazz) {
		if (!registryBiMap.containsValue(clazz)) throw new RegistryException(clazz, "Registry doesn't contain this type, please register.");
	}
}
