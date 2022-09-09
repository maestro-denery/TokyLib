/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.annotation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import io.toky.tokylib.ca.RegistryException;
import io.toky.tokylib.ca.annotation.group.GroupContainer;
import io.toky.tokylib.ca.annotation.group.Registry;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.type.TypeRegistry;

/**
 * Internal Utilities for handling annotations, regular user shouldn't touch it.
 */
public final class AnnotationUtil {
	private AnnotationUtil() {}

	public static void connectGroupsInContainer(GroupContainer repository) {
		final String classRequirements = "Can't create new instance, check class requirements";
		List<? extends TypeRegistry> registries = repository.typeRegistries.values().stream()
				.map(clazz -> {
					try {
						TypeRegistry registry = clazz.getDeclaredConstructor().newInstance();
						repository.hold(registry);
						return registry;
					} catch (ReflectiveOperationException e) {
						throw new RegistryException(classRequirements);
					}
				}).toList();
		List<? extends TypeInstanceHolder> holders = repository.holders.values().stream()
				.map(clazz -> {
					try {
						TypeInstanceHolder typeHolder = clazz.getDeclaredConstructor().newInstance();
						repository.hold(typeHolder);
						return typeHolder;
					} catch (ReflectiveOperationException e) {
						throw new RegistryException(classRequirements);
					}
				}).toList();
		List<? extends Lookuper> controllers = repository.controllers.values().stream()
				.map(clazz -> {
					try {
						Lookuper controller = clazz.getDeclaredConstructor().newInstance();
						repository.hold(controller);
						return controller;
					} catch (ReflectiveOperationException e) {
						throw new RegistryException(classRequirements);
					}
				}).toList();
		registries.forEach(typeRegistry -> holders.forEach(typeRegistry::setTIH));
		holders.forEach(holder -> registries.forEach(holder::setTypeRegistry));
		controllers.forEach(controller -> holders.forEach(controller::addTypeHolder));
	}

	public static void connectDataAddons(GroupContainer repository) {
		repository.data.values().stream().filter(obj -> obj.getClass().isAnnotationPresent(Registry.class) && obj instanceof TypeRegistry)
				.forEach(obj -> repository.dataAddons.values().forEach(dataAddon -> ((TypeRegistry) obj).register(dataAddon)));
	}

	public static void checkAnnotation(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(ContentAddon.class)) throw new RegistryException("Type: " + clazz.getSimpleName() + " has no annotation");
	}

	public static void invokeLoad(Object held) {
		checkAnnotation(held.getClass());
		Arrays.stream(held.getClass().getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(Load.class) && method.getReturnType().equals(void.class))
				.forEach(method -> {
					try {
						method.invoke(held);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new RegistryException("Check if your load method has no parameters.");
					}
				});
	}

	public static void invokeStore(Object held) {
		checkAnnotation(held.getClass());
		Arrays.stream(held.getClass().getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(Store.class) && method.getReturnType().equals(void.class)).forEach(method -> {
					try {
						method.invoke(held);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new RegistryException("Check if your load method has no parameters.");
					}
				});
	}

	public static void invokeNativeData(Object held) {

	}
}
