/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.holder.eventsourcing.ConcurrentEventTypeHolder;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy;
import io.toky.tokylib.ca.type.TypeRegistry;
import io.toky.tokylib.ca.test.registries.DummyHolder;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.GroupContainer;

class TypeHolderTest {
	ContentAddonBootstrap dataAddonBootstrap = new ContentAddonBootstrap();
	ConcurrentEventTypeHolder holder;
	TypeRegistry typeRegistry;

	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("io.toky.tokylib.ca.test.registries");
		dataAddonBootstrap.bootstrapContentAddons("io.toky.tokylib.ca.test.dummies");
		typeRegistry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
		holder = dataAddonBootstrap.getRegistry(DummyHolder.class);
	}

	@AfterEach
	void after() {
		dataAddonBootstrap.getContainer().clearAll();
	}

	@Test
	void checkHold() {
		assertDoesNotThrow(() -> holder.hold(new ContentAddonDummy()));
	}

	@Test
	void checkHoldEquality() {
		var dummy = new ContentAddonDummy();
		holder.hold(dummy);
		assertEquals(dummy, holder.getHeld(ContentAddonDummy.class).toArray()[0]);
	}

	@Test
	void checkGetType() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld(ContentAddonDummy.class));
	}

	@Test
	void checkGetID() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld(Key.key("test:dummy")));
	}

	@Test
	void checkContains() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		assertTrue(holder.containsInstance(dummy));
	}

	@Test
	void checkRelease() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(ContentAddonDummy.class);
		holder.release(dummy);
		assertFalse(holder.containsInstance(dummy));
		assertTrue(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseClass() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(ContentAddonDummy.class);
		holder.release(ContentAddonDummy.class);
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseID() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(ContentAddonDummy.class);
		holder.release(Key.key("test:dummy"));
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkEventHandlerType() {
		holder.handle(((event, sequence, endOfBatch) -> {
			assertEquals(ContentAddonDummy.class, event.getRegistrableType());
		}));
		holder.hold(new ContentAddonDummy());
	}

	@Test
	void checkEventHandlerCollection() {
		var dummy = new ContentAddonDummy();
		holder.handle(((event, sequence, endOfBatch) -> {
			assertIterableEquals(List.of(dummy), event.getRegistrables());
		}));
		holder.hold(dummy);
	}
}
