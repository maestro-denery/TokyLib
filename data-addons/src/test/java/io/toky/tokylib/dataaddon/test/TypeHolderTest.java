/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.dataaddon.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import io.toky.tokylib.dataaddon.DataAddonBootstrap;
import io.toky.tokylib.dataaddon.holder.eventsourcing.ConcurrentEventTypeHolder;
import io.toky.tokylib.dataaddon.test.dummies.DataAddonDummy;
import io.toky.tokylib.dataaddon.typeregistry.TypeRegistry;
import io.toky.tokylib.dataaddon.test.registries.DummyHolder;
import io.toky.tokylib.dataaddon.test.registries.DummyTypeRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.dataaddon.annotation.group.GroupContainer;

class TypeHolderTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	ConcurrentEventTypeHolder holder;
	TypeRegistry typeRegistry;

	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("io.toky.tokylib.dataaddon.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("io.toky.tokylib.dataaddon.test.dummies");
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
