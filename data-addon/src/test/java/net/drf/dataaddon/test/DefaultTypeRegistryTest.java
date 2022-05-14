/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test;

import static org.junit.jupiter.api.Assertions.*;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.drf.dataaddon.DataAddonBootstrap;
import net.drf.dataaddon.annotation.group.GroupContainer;
import net.drf.dataaddon.typeregistry.DefaultTypeRegistry;
import net.drf.dataaddon.test.dummies.DataAddonDummy;
import net.drf.dataaddon.test.registries.DummyTypeRegistry;

class DefaultTypeRegistryTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	DefaultTypeRegistry registry;
	
	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("net.drf.dataaddon.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("net.drf.dataaddon.test.dummies");
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
		assertEquals(DataAddonDummy.class, registry.getRegistrableType(Key.key("test:dummy")));
	}
}
