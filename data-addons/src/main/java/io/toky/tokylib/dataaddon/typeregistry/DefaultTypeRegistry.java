/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.dataaddon.typeregistry;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.toky.tokylib.dataaddon.RegistryException;
import io.toky.tokylib.dataaddon.annotation.AnnotationUtil;
import io.toky.tokylib.dataaddon.holder.TypeHolder;
import io.toky.tokylib.dataaddon.annotation.DataAddon;
import net.kyori.adventure.key.Key;

public class DefaultTypeRegistry extends TypeRegistry {
	protected final Collection<TypeHolder> holders = new ArrayList<>();
	protected final BiMap<Key, Class<?>> registryBiMap = HashBiMap.create();

	@Override
	public void addRegistrableHolder(TypeHolder registrableHolder) {
		holders.add(registrableHolder);
	}

	@Override
	public void register(Class<?> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		registryBiMap.put(Key.key(registrableType.getAnnotation(DataAddon.class).identifier()), registrableType);
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
	public Collection<TypeHolder> getRegistrableHolders() {
		return holders;
	}

	@Override
	public <T> T newInstance(Class<T> registrableType) {
		checkContains(registrableType);
		try {
			var registrableInstance = registrableType.getDeclaredConstructor().newInstance();
			holders.forEach(holder -> holder.hold(registrableInstance));
			return registrableInstance;
		} catch (ReflectiveOperationException e) {
			throw new RegistryException(registrableType, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Key identifier) {
		Class<T> registrableType = (Class<T>) registryBiMap.get(identifier);
		return this.newInstance(registrableType);
	}

	@Override
	public Key getIdentifier(Class<?> registrableType) {
		checkContains(registrableType);
		return registryBiMap.inverse().get(registrableType);
	}

	@Override
	public Class<?> getRegistrableType(Key identifier) {
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
