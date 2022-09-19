/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test;

import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy2;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.InfrastructureContainer;

import static org.junit.jupiter.api.Assertions.*;

class BootstrapTest {
	ContentAddonBootstrap bootstrap = new ContentAddonBootstrap();
	final Key key = Key.key("test", "dummy_type_registry");
	ResourceKey<ContentAddonDummy> addonDummyResourceKey;
	ResourceKey<ContentAddonDummy2> addonDummy2ResourceKey;

	@BeforeEach
	void before() {
		bootstrap.setContainer(new InfrastructureContainer());
		bootstrap.bootstrapSystem(ContentAddonRegistry.create(key), ContentAddonContainer.create(), DataLookerUpper.create());
		bootstrap.bootstrapContentAddons(getClass().getClassLoader(), "io.toky.tokylib.ca.test.dummies");
		addonDummyResourceKey = bootstrap.getContainer().contentAddonRegistry().getKey(ContentAddonDummy.class);
		addonDummy2ResourceKey = bootstrap.getContainer().contentAddonRegistry().getKey(ContentAddonDummy2.class);
		bootstrap.getContainer().contentAddonContainer().newInstance(addonDummyResourceKey);
		bootstrap.getContainer().contentAddonContainer().newInstance(addonDummy2ResourceKey);
	}

	@AfterEach
	void after() {
		bootstrap = null;
	}

	@Test
	void checkContentAddonRegistryInstance() {
		assertNotNull(bootstrap.getContainer().contentAddonRegistry());
	}

	@Test
	void checkContentAddonContainerInstance() {
		assertNotNull(bootstrap.getContainer().contentAddonContainer());
	}

	@Test
	void checkDataLookerUpperInstance() {
		assertNotNull(bootstrap.getContainer().dataLookerUpper());
	}

	@Test
	void checkContentAddonInstances() {
		assertFalse(bootstrap.getContainer().contentAddonContainer().getHeld(ContentAddonDummy.class).isEmpty());
		assertFalse(bootstrap.getContainer().contentAddonContainer().getHeld(ContentAddonDummy2.class).isEmpty());
	}
}
