/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.annotation.AnnotationUtil;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.annotation.ContentAddon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultStoreLoadController extends Lookuper {
	protected final Collection<TypeInstanceHolder> holders = new ArrayList<>();
	// Total type erasure but eh... magic.
	protected final Map<Object, TypeMark<Object, Object>> customTypeMarks = new HashMap<>();
	protected final Map<Object, TypeMark<Object, Object>> nativeTypeMarks = new HashMap<>();
	protected final BiMap<Object, Object> customToNative = HashBiMap.create();

	@Override
	public void store(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(registrable -> {
			final TypeMark<Object, Object> mark = customTypeMarks.get(registrableType);
			if (mark != null) {
				Object n = customToNative.get(registrable);
				if (n != null)
					mark.mark(n);
			}
			AnnotationUtil.invokeStore(registrable);
		}));
	}

	@Override
	public void load(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(AnnotationUtil::invokeLoad));
	}

	@Override
	public <T> void lookupAndLoad(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookupAndLoad(registrableType.getAnnotation(ContentAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> TypeMark<T, ?> getCustomMark(T registrableType) {
		return (TypeMark<T, ?>) customTypeMarks.get(registrableType);
	}

	@Override
	public <T> void lookup(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookup(registrableType.getAnnotation(ContentAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	public <T, N> void lookupAndLoad(ContentLookup<T, N> lookup) {
		lookup.lookup().forEach(instantiated -> {
			putCustomNativeData(instantiated, lookup.mark());
			holders.forEach(holder -> holder.hold(instantiated.getFirst()));
			AnnotationUtil.invokeLoad(instantiated.getFirst());
		});
	}

	@Override
	public <T, N> void lookup(ContentLookup<T, N> lookup) {
		Stream<Pair<T, N>> lookedStream = lookup.lookup();
		lookedStream.forEach(registrable -> {
			putCustomNativeData(registrable, lookup.mark());
			holders.forEach(holder -> holder.hold(registrable.getFirst()));
		});
	}

	@Override
	public void addTypeHolder(TypeInstanceHolder registrableHolder) {
		holders.add(registrableHolder);
	}

	@Override
	public Collection<TypeInstanceHolder> getTypeHolders() {
		return holders;
	}

	// Private methods.

	@SuppressWarnings("unchecked")
	private <T, N> void putCustomNativeData(Pair<T, N> customAndNative, TypeMark<T, N> mark) {
		nativeTypeMarks.put(customAndNative.getSecond(), (TypeMark<Object, Object>) mark);
		customTypeMarks.put(customAndNative.getFirst(), (TypeMark<Object, Object>) mark);
		customToNative.put(customAndNative.getFirst(), customAndNative.getSecond());
	}
}
