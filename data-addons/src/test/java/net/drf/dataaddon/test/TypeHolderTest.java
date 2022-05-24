/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import net.drf.dataaddon.test.dummies.DataAddonDummy;
import net.drf.dataaddon.test.registries.DummyHolder;
import net.drf.dataaddon.test.registries.DummyTypeRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.drf.dataaddon.DataAddonBootstrap;
import net.drf.dataaddon.annotation.group.GroupContainer;
import net.drf.dataaddon.holder.eventsourcing.ConcurrentEventTypeHolder;
import net.drf.dataaddon.typeregistry.TypeRegistry;

class TypeHolderTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	ConcurrentEventTypeHolder holder;
	TypeRegistry typeRegistry;

	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("net.drf.dataaddon.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("net.drf.dataaddon.test.dummies");
		typeRegistry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
		holder = dataAddonBootstrap.getRegistry(DummyHolder.class);
	}

	@AfterEach
	void after() {
		dataAddonBootstrap.getContainer().clearAll();
	}

	@Test
	void checkHold() {
		assertDoesNotThrow(() -> holder.hold(new DataAddonDummy()));
	}

	@Test
	void checkHoldEquality() {
		var dummy = new DataAddonDummy();
		holder.hold(dummy);
		assertEquals(dummy, holder.getHeld(DataAddonDummy.class).toArray()[0]);
	}

	@Test
	void checkGetType() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld(DataAddonDummy.class));
	}

	@Test
	void checkGetID() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld(Key.key("test:dummy")));
	}

	@Test
	void checkContains() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertTrue(holder.containsInstance(dummy));
	}

	@Test
	void checkRelease() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release(dummy);
		assertFalse(holder.containsInstance(dummy));
		assertTrue(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseClass() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release(DataAddonDummy.class);
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseID() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release(Key.key("test:dummy"));
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkEventHandlerType() {
		holder.handle(((event, sequence, endOfBatch) -> {
			assertEquals(DataAddonDummy.class, event.getRegistrableType());
		}));
		holder.hold(new DataAddonDummy());
	}

	@Test
	void checkEventHandlerCollection() {
		var dummy = new DataAddonDummy();
		holder.handle(((event, sequence, endOfBatch) -> {
			assertIterableEquals(List.of(dummy), event.getRegistrables());
		}));
		holder.hold(dummy);
	}
}
