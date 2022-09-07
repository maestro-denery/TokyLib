/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.toky.tokylib.ca.test.registries.DummyHolder;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.DataAddonBootstrap;
import io.toky.tokylib.ca.annotation.group.GroupContainer;
import io.toky.tokylib.ca.test.registries.subpkg.DummyController;

class PackageBootstrapTest {
	DataAddonBootstrap bootstrap = new DataAddonBootstrap();

	@BeforeEach
	void before() {
		bootstrap.setContainer(new GroupContainer());
		bootstrap.bootstrapRegistries("io.toky.tokylib.ca.test.registries");
		bootstrap.bootstrapDataAddons("io.toky.tokylib.ca.test.dummies");
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
