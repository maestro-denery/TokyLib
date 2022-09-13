/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.toky.tokylib.ca.holder.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.test.registries.DummyHolder;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.GroupContainer;
import io.toky.tokylib.ca.test.registries.subpkg.DummyController;

class BootstrapTest {
	ContentAddonBootstrap bootstrap = new ContentAddonBootstrap();
	final Key key = Key.key("test", "dummy_type_registry");

	@BeforeEach
	void before() {
		bootstrap.setContainer(new GroupContainer());
		bootstrap.bootstrapSystem(ContentAddonRegistry.create(key), ContentAddonContainer.create(), DataLookerUpper.create());
		bootstrap.bootstrapContentAddons("io.toky.tokylib.ca.test.dummies");
	}

	@AfterEach
	void after() {
		bootstrap = null;
	}

	@Test
	void checkTypeRegistryInstance() {
		assertNotNull(bootstrap.getRegistry(DummyTypeRegistry.class));
	}

	@Test
	void checkTypeHolderInstance() {
		assertNotNull(bootstrap.getRegistry(DummyHolder.class));
	}

	@Test
	void checkStoreLoadControllerInstance() {
		assertNotNull(bootstrap.getRegistry(DummyController.class));
	}
}
