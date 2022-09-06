/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.dataaddon.test;

import io.toky.tokylib.dataaddon.test.dummies.DataAddonDummy;
import io.toky.tokylib.dataaddon.DataAddonBootstrap;
import io.toky.tokylib.dataaddon.annotation.group.GroupContainer;
import io.toky.tokylib.dataaddon.test.registries.DummyHolder;
import io.toky.tokylib.dataaddon.test.registries.DummyTypeRegistry;
import io.toky.tokylib.dataaddon.test.registries.subpkg.DummyController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassesBootstrapTest {
	DataAddonBootstrap bootstrap = new DataAddonBootstrap();

	@BeforeEach
	void before() {
		bootstrap.setContainer(new GroupContainer());
		bootstrap.bootstrapRegistries(DummyHolder.class, DummyTypeRegistry.class, DummyController.class);
		bootstrap.bootstrapDataAddons(DataAddonDummy.class);
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
