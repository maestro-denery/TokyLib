/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca;

import java.io.IOException;
import java.util.List;

import com.google.common.reflect.ClassPath;

import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import io.toky.tokylib.ca.annotation.ContentAddon;

/**
 * Bootstrap where you initialize all work of your Data Addons and its registries.
 */
public final class ContentAddonBootstrap {
	private InfrastructureContainer container;

	public void setContainer(InfrastructureContainer container) {
		this.container = container;
	}

	public InfrastructureContainer getContainer() {
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

	public void bootstrapSystem(final ContentAddonRegistry contentAddonRegistry, final ContentAddonContainer contentAddonContainer, final DataLookerUpper dataLookerUpper) {
		this.container.registerContentAddonRegistry(contentAddonRegistry);
		this.container.registerContentAddonContainer(contentAddonContainer);
		this.container.registerLookerUpper(dataLookerUpper);
		contentAddonContainer.setContentAddonRegistry(contentAddonRegistry);
		dataLookerUpper.setContentAddonContainer(contentAddonContainer);
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

	public static void connectGroupsInContainer(InfrastructureContainer repository) {
		final ContentAddonContainer tih = repository.contentAddonContainer();
		repository.dataLookerUpper().setContentAddonContainer(tih);
		tih.setContentAddonRegistry(repository.contentAddonRegistry());
	}

	public static void connectContentAddons(InfrastructureContainer repository) {
		final ContentAddonRegistry contentAddonRegistry = repository.contentAddonRegistry();
		for (Class<?> value : repository.contentAddons().values()) {
			contentAddonRegistry.register(value);
		}
	}

	public static void checkAnnotation(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(ContentAddon.class)) throw new RegistryException("Type: " + clazz.getSimpleName() + " has no annotation");
	}
}
