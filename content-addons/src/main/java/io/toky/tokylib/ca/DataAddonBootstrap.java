/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;

import java.io.IOException;
import java.util.List;

import com.google.common.reflect.ClassPath;

import io.toky.tokylib.ca.annotation.AnnotationUtil;
import io.toky.tokylib.ca.annotation.group.ContentLookuper;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.type.TypeRegistry;
import io.toky.tokylib.ca.annotation.ContentAddon;
import io.toky.tokylib.ca.annotation.group.GroupContainer;
import io.toky.tokylib.ca.annotation.group.InstanceHolder;
import io.toky.tokylib.ca.annotation.group.Registry;
import io.toky.tokylib.ca.lookup.Lookuper;

/**
 * Bootstrap where you initialize all work of your Data Addons and its registries.
 */
public final class DataAddonBootstrap {
	private GroupContainer container;

	public void setContainer(GroupContainer container) {
		this.container = container;
	}

	public GroupContainer getContainer() {
		return container;
	}

	/**
	 * Configures {@link TypeRegistry}, {@link TypeInstanceHolder}, {@link Lookuper} and other infrastructure among given classes.
	 * @param classes registries you want to register.
	 */
	public void bootstrapRegistries(Class<?>... classes) {
		for (Class<?> aClass : classes) {
			if (aClass.isAnnotationPresent(Registry.class))
				container.registerTypeRegistry(aClass.asSubclass(TypeRegistry.class));
			else if (aClass.isAnnotationPresent(InstanceHolder.class))
				container.registerHolder(aClass.asSubclass(TypeInstanceHolder.class));
			else if (aClass.isAnnotationPresent(ContentLookuper.class))
				container.registerController(aClass.asSubclass(Lookuper.class));
			else throw new RegistryException("Class: " + aClass + " Has no annotation!");
		}

		AnnotationUtil.connectGroupsInContainer(container);
	}

	/**
	 * Configures {@link TypeRegistry}, {@link TypeInstanceHolder}, {@link Lookuper} and other infrastructure in the package.
	 * @param packageName package containing registries.
	 */
	public void bootstrapRegistries(String packageName) {
		try {
			List<? extends Class<?>> classesInPackage = ClassPath.from(ClassLoader.getSystemClassLoader())
					.getAllClasses()
					.stream()
					.filter(clazz -> clazz.getPackageName().startsWith(packageName))
					.map(ClassPath.ClassInfo::load)
					.toList();

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(Registry.class))
					.forEach(clazz -> container.registerTypeRegistry(clazz.asSubclass(TypeRegistry.class)));

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(InstanceHolder.class))
					.forEach(clazz -> container.registerHolder(clazz.asSubclass(TypeInstanceHolder.class)));

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(ContentLookuper.class))
					.forEach(clazz -> container.registerController(clazz.asSubclass(Lookuper.class)));
			
			AnnotationUtil.connectGroupsInContainer(container);
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping Registries.");
		}
	}

	/**
	 * Configures {@link ContentAddon} implementation and connects it with already existing infrastructure.
	 * @param classes data addons' classes you want to register.
	 */
	public void bootstrapDataAddons(Class<?>... classes) {
		for (Class<?> aClass : classes) {
			container.registerImplementation(aClass);
		}
		AnnotationUtil.connectDataAddons(container);
	}

	/**
	 * Configures {@link ContentAddon} implementation and connects it with already existing infrastructure.
	 * configured with {@link #bootstrapRegistries(String)} and contained in {@link #container}
	 * @param packageName package containing DataAddons.
	 */
	public void bootstrapDataAddons(String packageName) {
		try {
			List<? extends Class<?>> implClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
					.getAllClasses()
					.stream()
					.filter(clazz -> clazz.getPackageName().startsWith(packageName))
					.map(ClassPath.ClassInfo::load)
					.filter(clazz -> clazz.isAnnotationPresent(ContentAddon.class))
					.toList();

			implClasses.forEach(container::registerImplementation);
			AnnotationUtil.connectDataAddons(container);
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping Registries.");
		}
	}

	/**
	 * @return registry instance contained in {@link #container} by its class.
	 */
	public <T> T getRegistry(Class<T> clazz) {
		return (T) container.data.get(clazz);
	}

	public <T> ContentAddon getDataAddonInfo(Class<T> clazz) {
		if (!container.dataAddons.values().contains(clazz)) throw new RegistryException("This DataAddon doesn't registered in this bootstrap!");
		return clazz.getAnnotation(ContentAddon.class);
	}
}
