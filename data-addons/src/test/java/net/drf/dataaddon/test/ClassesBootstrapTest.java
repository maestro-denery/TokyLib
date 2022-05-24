/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon.test;

import net.drf.dataaddon.DataAddonBootstrap;
import net.drf.dataaddon.annotation.group.GroupContainer;
import net.drf.dataaddon.test.dummies.DataAddonDummy;
import net.drf.dataaddon.test.registries.DummyHolder;
import net.drf.dataaddon.test.registries.DummyTypeRegistry;
import net.drf.dataaddon.test.registries.subpkg.DummyController;

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
