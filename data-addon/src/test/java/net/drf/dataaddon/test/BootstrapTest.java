/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.drf.dataaddon.test.registries.DummyHolder;
import net.drf.dataaddon.test.registries.DummyTypeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.drf.dataaddon.DataAddonBootstrap;
import net.drf.dataaddon.annotation.group.GroupContainer;
import net.drf.dataaddon.test.registries.subpkg.DummyController;

class BootstrapTest {
	DataAddonBootstrap bootstrap = new DataAddonBootstrap();

	@BeforeEach
	void before() {
		bootstrap.setContainer(new GroupContainer());
		bootstrap.bootstrapRegistries("net.drf.dataaddon.test.registries");
		bootstrap.bootstrapDataAddons("net.drf.dataaddon.test.dummies");
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
