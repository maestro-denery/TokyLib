/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.*;

import io.toky.tokylib.ca.DataAddonBootstrap;
import io.toky.tokylib.ca.test.dummies.DataAddonDummy;
import io.toky.tokylib.ca.type.DefaultTypeRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.annotation.group.GroupContainer;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;

class DefaultTypeRegistryTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	DefaultTypeRegistry registry;
	
	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("io.toky.tokylib.ca.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("io.toky.tokylib.ca.test.dummies");
		registry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
	}

	@AfterEach
	void after() {
		registry.clearRegistry();
		dataAddonBootstrap.getContainer().clearAll();
		registry = null;
	}

	@Test
	void checkIsRegistered() {
		assertTrue(registry.isRegistered(DataAddonDummy.class));
	}

	@Test
	void checkNewInstanceClass() {
		assertNotNull(registry.newInstance(DataAddonDummy.class));
	}

	@Test
	void checkNewInstanceID() {
		assertNotNull(registry.newInstance(Key.key("test:dummy")));
	}
	
	@Test
	void checkGetIdentifier() {
		assertEquals(Key.key("test:dummy"), registry.getIdentifier(DataAddonDummy.class));
	}
	
	@Test
	void checkGetClass() {
		assertEquals(DataAddonDummy.class, registry.getContentAddonType(Key.key("test:dummy")));
	}
}
