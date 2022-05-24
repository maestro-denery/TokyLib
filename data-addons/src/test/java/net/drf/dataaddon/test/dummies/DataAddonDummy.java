/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test.dummies;

import net.drf.dataaddon.annotation.DataAddon;
import net.drf.dataaddon.annotation.Load;
import net.drf.dataaddon.annotation.Store;

@DataAddon(
		identifier = "test:dummy",
		groupTag = "dummyGroup",
		nativeClass = DataAddonDummyLookup.NativeDummy.class,
		lookup = DataAddonDummyLookup.class
)
public class DataAddonDummy {
	private String someString;
	private String someNativeStringData;

	public String getSomeString() {
		return someString;
	}

	public String getSomeNativeStringData() {
		return someNativeStringData;
	}

	public void setSomeNativeStringData(String someNativeStringData) {
		this.someNativeStringData = someNativeStringData;
	}

	@Store
	public void store() {
		someString = "store";
	}

	@Load
	public void load() {
		someString = "load";
	}
}
