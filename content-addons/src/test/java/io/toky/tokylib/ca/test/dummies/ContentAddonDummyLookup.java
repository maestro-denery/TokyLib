/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.dummies;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.toky.tokylib.ca.lookup.ReferencedContentLookup;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContentAddonDummyLookup implements ReferencedContentLookup<ContentAddonDummy, ContentAddonDummyLookup.NativeDummy> {

	private final Collection<NativeDummy> nativeDummiesContainer = Lists.newArrayList(
			new NativeDummy("native1")
	);

	@Override
	public Stream<NativeDummy> natives() {
		return null;
	}

	@Override
	public Function<NativeDummy, ContentAddonDummy> fromReference() {
		return null;
	}

	@Override
	public Stream<Pair<ContentAddonDummy, NativeDummy>> lookup() {
		return null;
	}

	@Override
	public <D> io.toky.tokylib.io.IOUnit<ContentAddonDummy, D> io() {
		return null;
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
