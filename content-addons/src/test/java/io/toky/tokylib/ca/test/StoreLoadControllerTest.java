/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.toky.tokylib.ca.DataAddonBootstrap;
import io.toky.tokylib.ca.holder.TypeHolder;
import io.toky.tokylib.ca.test.dummies.DataAddonDummy;
import io.toky.tokylib.ca.test.dummies.DataAddonDummyLookup;
import io.toky.tokylib.ca.typeregistry.TypeRegistry;
import io.toky.tokylib.ca.test.registries.DummyHolder;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.annotation.group.GroupContainer;
import io.toky.tokylib.ca.storeload.StoreLoadController;
import io.toky.tokylib.ca.test.registries.subpkg.DummyController;

class StoreLoadControllerTest {
	TypeRegistry typeRegistry;
	TypeHolder holder;
	StoreLoadController controller;

	@BeforeEach
	void before() {
		DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();

		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("io.toky.tokylib.ca.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("io.toky.tokylib.ca.test.dummies");

		typeRegistry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
		holder = dataAddonBootstrap.getRegistry(DummyHolder.class);
		controller = dataAddonBootstrap.getRegistry(DummyController.class);
	}

	@AfterEach
	void after() {
		holder.clearHeld();
		typeRegistry.clearRegistry();
		typeRegistry = null;
		holder = null;
		controller = null;
	}

	@Test
	void checkStore() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		controller.store(DataAddonDummy.class);
		assertEquals("store", dummy.getSomeString());
		assertEquals("store", dummy1.getSomeString());
	}

	@Test
	void checkLoad() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		controller.load(DataAddonDummy.class);
		assertEquals("load", dummy.getSomeString());
		assertEquals("load", dummy1.getSomeString());
	}

	@Test
	void checkLookup() {
		controller.lookup(new DataAddonDummyLookup());
		controller.load(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoad() {
		controller.lookupAndLoad(new DataAddonDummyLookup());
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupClass() {
		controller.lookup(DataAddonDummy.class);
		controller.load(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoadClass() {
		controller.lookupAndLoad(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkMark() {
		controller.lookupAndLoad(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertNotNull(controller.getCustomMark(dummy));
	}
}
