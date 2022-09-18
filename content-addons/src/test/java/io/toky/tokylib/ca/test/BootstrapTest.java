/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.InfrastructureContainer;

class BootstrapTest {
	ContentAddonBootstrap bootstrap = new ContentAddonBootstrap();
	final Key key = Key.key("test", "dummy_type_registry");

	@BeforeEach
	void before() {
		bootstrap.setContainer(new InfrastructureContainer());
		bootstrap.bootstrapSystem(ContentAddonRegistry.create(key), ContentAddonContainer.create(), DataLookerUpper.create());
		bootstrap.bootstrapContentAddons(getClass().getClassLoader(), "io.toky.tokylib.ca.test.dummies");
	}

	@AfterEach
	void after() {
		bootstrap = null;
	}

	@Test
	void checkTypeRegistryInstance() {
		assertNotNull(bootstrap.getContainer().contentAddonRegistry());
	}

	@Test
	void checkTypeHolderInstance() {
		assertNotNull(bootstrap.getContainer().contentAddonContainer());
	}

	@Test
	void checkStoreLoadControllerInstance() {
		assertNotNull(bootstrap.getContainer().dataLookerUpper());
	}
}
