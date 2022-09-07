/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.dummies;

import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import io.toky.tokylib.ca.storeload.Mark;
import io.toky.tokylib.ca.storeload.StoreLoadLookup;

public class DataAddonDummyLookup implements StoreLoadLookup<DataAddonDummy, DataAddonDummyLookup.NativeDummy> {

	private final Collection<NativeDummy> nativeDummiesContainer = Lists.newArrayList(
			new NativeDummy("native1")
	);

	@Override
	public Mark<DataAddonDummy, DataAddonDummyLookup.NativeDummy> mark() {
		return Mark.create(nativeD -> {
			var dummy = new DataAddonDummy();
			dummy.setSomeNativeStringData(nativeD.getSomeNativeString());
			return dummy;
		}, dummy -> true, n -> {
			
		});
	}

	@Override
	public Stream<NativeDummy> getNatives() {
		return nativeDummiesContainer.stream();
	}
	
	public static class NativeDummy {
		private final String someNativeString;
		public NativeDummy(String nativeString) {
			this.someNativeString = nativeString;
		} 

		public String getSomeNativeString() {
			return someNativeString;
		}
	}
}
