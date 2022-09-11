/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummyLookup;
import io.toky.tokylib.ca.type.TypeRegistry;
import io.toky.tokylib.ca.test.registries.DummyHolder;
import io.toky.tokylib.ca.test.registries.DummyTypeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.GroupContainer;
import io.toky.tokylib.ca.lookup.Lookuper;
import io.toky.tokylib.ca.test.registries.subpkg.DummyController;

class StoreLoadControllerTest {
	TypeRegistry typeRegistry;
	TypeInstanceHolder holder;
	Lookuper controller;

	@BeforeEach
	void before() {
		ContentAddonBootstrap dataAddonBootstrap = new ContentAddonBootstrap();

		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("io.toky.tokylib.ca.test.registries");
		dataAddonBootstrap.bootstrapContentAddons("io.toky.tokylib.ca.test.dummies");

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
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(ContentAddonDummy.class);
		controller.storeStandaloneFile(ContentAddonDummy.class);
		assertEquals("store", dummy.getSomeString());
		assertEquals("store", dummy1.getSomeString());
	}

	@Test
	void checkLoad() {
		var dummy = typeRegistry.newInstance(ContentAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(ContentAddonDummy.class);
		controller.loadStandaloneFile(ContentAddonDummy.class);
		assertEquals("load", dummy.getSomeString());
		assertEquals("load", dummy1.getSomeString());
	}

	@Test
	void checkLookup() {
		controller.lookup(new ContentAddonDummyLookup());
		controller.loadStandaloneFile(ContentAddonDummy.class);
		ContentAddonDummy dummy = holder.getHeld(ContentAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoad() {
		controller.lookupAndLoad(new ContentAddonDummyLookup());
		ContentAddonDummy dummy = holder.getHeld(ContentAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupClass() {
		controller.lookup(ContentAddonDummy.class);
		controller.loadStandaloneFile(ContentAddonDummy.class);
		ContentAddonDummy dummy = holder.getHeld(ContentAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoadClass() {
		controller.lookupAndLoad(ContentAddonDummy.class);
		ContentAddonDummy dummy = holder.getHeld(ContentAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkMark() {
		controller.lookupAndLoad(ContentAddonDummy.class);
		ContentAddonDummy dummy = holder.getHeld(ContentAddonDummy.class).stream().findFirst().get();
		assertNotNull(controller.getCustomMark(dummy));
	}
}
