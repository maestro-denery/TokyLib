/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;

import java.io.IOException;
import java.util.List;

import com.google.common.reflect.ClassPath;

import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.type.TypeRegistry;
import io.toky.tokylib.ca.annotation.ContentAddon;

/**
 * Bootstrap where you initialize all work of your Data Addons and its registries.
 */
public final class ContentAddonBootstrap {
	private GroupContainer container;

	public void setContainer(GroupContainer container) {
		this.container = container;
	}

	public GroupContainer getContainer() {
		return container;
	}

	/**
	 * Configures {@link ContentAddon} implementation and connects it with already existing infrastructure.
	 * @param classes data addons' classes you want to register.
	 */
	public void bootstrapContentAddons(Class<?>... classes) {
		for (Class<?> aClass : classes) {
			container.registerContentAddon(aClass);
		}
		ContentAddonBootstrap.connectContentAddons(container);
	}

	public void bootstrapSystem(final TypeRegistry typeRegistry, final TypeInstanceHolder tih, final Lookuper lookuper) {
		this.container.registerTR(typeRegistry);
		this.container.registerTIH(tih);
		this.container.registerLookuper(lookuper);
	}

	/**
	 * Configures {@link ContentAddon} implementation and connects it with an already existing infrastructure.
	 * @param packageName package containing ContentAddons.
	 */
	public void bootstrapContentAddons(ClassLoader classLoader, String packageName) {
		try {
			List<? extends Class<?>> caClasses = ClassPath.from(classLoader)
					.getAllClasses()
					.stream()
					.filter(clazz -> clazz.getPackageName().startsWith(packageName))
					.map(ClassPath.ClassInfo::load)
					.filter(clazz -> clazz.isAnnotationPresent(ContentAddon.class))
					.toList();

			caClasses.forEach(container::registerContentAddon);
			ContentAddonBootstrap.connectContentAddons(container);
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping ContentAddons.");
		}
	}

	public static void connectGroupsInContainer(GroupContainer repository) {
		final TypeInstanceHolder tih = repository.tih();
		repository.lookuper().setTIH(tih);
		tih.setTypeRegistry(repository.typeRegistry());
	}

	public static void connectContentAddons(GroupContainer repository) {
		final TypeRegistry typeRegistry = repository.typeRegistry();
		for (Class<?> value : repository.contentAddons().values()) {
			typeRegistry.register(value);
		}
	}

	public static void checkAnnotation(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(ContentAddon.class)) throw new RegistryException("Type: " + clazz.getSimpleName() + " has no annotation");
	}

	public <T> ContentAddon getDataAddonInfo(Class<T> clazz) {
		if (!container.contentAddons().values().contains(clazz)) throw new RegistryException("This DataAddon doesn't registered in this bootstrap!");
		return clazz.getAnnotation(ContentAddon.class);
	}
}