/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon;

import java.io.IOException;
import java.util.List;

import com.google.common.reflect.ClassPath;

import net.drf.dataaddon.annotation.AnnotationUtil;
import net.drf.dataaddon.annotation.DataAddon;
import net.drf.dataaddon.annotation.group.Controller;
import net.drf.dataaddon.annotation.group.GroupContainer;
import net.drf.dataaddon.annotation.group.Holder;
import net.drf.dataaddon.annotation.group.Registry;
import net.drf.dataaddon.holder.TypeHolder;
import net.drf.dataaddon.storeload.StoreLoadController;
import net.drf.dataaddon.typeregistry.TypeRegistry;

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
	 * Configures {@link TypeRegistry}, {@link TypeHolder}, {@link StoreLoadController} and other infrastructure among given classes.
	 * @param classes registries you want to register.
	 */
	public void bootstrapRegistries(Class<?>... classes) {
		for (Class<?> aClass : classes) {
			if (aClass.isAnnotationPresent(Registry.class))
				container.registerTypeRegistry(aClass.asSubclass(TypeRegistry.class));
			else if (aClass.isAnnotationPresent(Holder.class))
				container.registerHolder(aClass.asSubclass(TypeHolder.class));
			else if (aClass.isAnnotationPresent(Controller.class))
				container.registerController(aClass.asSubclass(StoreLoadController.class));
			else throw new RegistryException("Class: " + aClass + " Has no annotation!");
		}

		AnnotationUtil.connectGroupsInContainer(container);
	}

	/**
	 * Configures {@link TypeRegistry}, {@link TypeHolder}, {@link StoreLoadController} and other infrastructure in the package.
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
					.filter(clazz -> clazz.isAnnotationPresent(Holder.class))
					.forEach(clazz -> container.registerHolder(clazz.asSubclass(TypeHolder.class)));

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(Controller.class))
					.forEach(clazz -> container.registerController(clazz.asSubclass(StoreLoadController.class)));
			
			AnnotationUtil.connectGroupsInContainer(container);
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping Registries.");
		}
	}

	/**
	 * Configures {@link DataAddon} implementation and connects it with already existing infrastructure.
	 * @param classes data addons' classes you want to register.
	 */
	public void bootstrapDataAddons(Class<?>... classes) {
		for (Class<?> aClass : classes) {
			container.registerImplementation(aClass);
		}
		AnnotationUtil.connectDataAddons(container);
	}

	/**
	 * Configures {@link DataAddon} implementation and connects it with already existing infrastructure.
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
					.filter(clazz -> clazz.isAnnotationPresent(DataAddon.class))
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

	public <T> DataAddon getDataAddonInfo(Class<T> clazz) {
		if (!container.dataAddons.values().contains(clazz)) throw new RegistryException("This DataAddon doesn't registered in this bootstrap!");
		return clazz.getAnnotation(DataAddon.class);
	}
}
