/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon.storeload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.collect.BiMap;

import com.google.common.collect.HashBiMap;

import net.drf.dataaddon.RegistryException;
import net.drf.dataaddon.annotation.AnnotationUtil;
import net.drf.dataaddon.annotation.DataAddon;
import net.drf.dataaddon.holder.TypeHolder;
import net.drf.dataaddon.mark.Mark;

public class DefaultStoreLoadController extends StoreLoadController {
	protected final Collection<TypeHolder> holders = new ArrayList<>();
	protected final BiMap<Class<?>, Mark<?, ?>> marks = HashBiMap.create();

	@Override
	public void store(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(AnnotationUtil::invokeStore));
	}

	@Override
	public void load(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(AnnotationUtil::invokeLoad));
	}

	@Override
	public <T> void lookupAndLoad(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookupAndLoad(registrableType.getAnnotation(DataAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, N> Mark<T, N> getMark(Class<T> registrableType) {
		return (Mark<T, N>) marks.get(registrableType);
	}

	@Override
	public <T> void lookup(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookup(registrableType.getAnnotation(DataAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	public <T, N> void lookupAndLoad(StoreLoadLookup<T, N> lookup) {
		lookup.lookup().forEach(instantiated -> {
			holders.forEach(holder -> holder.hold(instantiated));
			AnnotationUtil.invokeLoad(instantiated);
		});
	}

	@Override
	public <T, N> void lookup(StoreLoadLookup<T, N> lookup) {
		Stream<T> lookedStream = lookup.lookup();
		lookedStream.forEach(registrable -> holders.forEach(holder -> holder.hold(registrable)));
	}

	@Override
	public void addRegistrableHolder(TypeHolder registrableHolder) {
		holders.add(registrableHolder);
	}

	@Override
	public Collection<TypeHolder> getRegistrableHolders() {
		return holders;
	}
}
