/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.dummies;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.lookup.ReferencedContentLookup;
import io.toky.tokylib.io.JsonIO;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContentAddonDummyLookup implements ReferencedContentLookup<ContentAddonDummy, ContentAddonDummyLookup.NativeDummy> {
	private final File dummyFile = new File("src/test/resources/dummyData.json");

	private final Collection<NativeDummy> nativeDummiesContainer = Lists.newArrayList(
			new NativeDummy("native1")
	);

	@Override
	public Stream<NativeDummy> natives() {
		return nativeDummiesContainer.stream();
	}

	@Override
	public Codec<ContentAddonDummy> codec() {
		return ContentAddonDummy.CODEC;
	}

	@Override
	public Optional<StandaloneFileOps<ContentAddonDummy, ?>> standaloneFileOps(ResourceKey<ContentAddonDummy> resourceKey) {
		return Optional.of(new StandaloneFileOps<>(this, JsonIO.INSTANCE, JsonOps.INSTANCE, dummyFile));
	}

	@Override
	public Function<NativeDummy, ContentAddonDummy> fromReference() {
		return nativeDummy -> {
			final ContentAddonDummy contentAddonDummy = new ContentAddonDummy();
			contentAddonDummy.setSomeNativeString(nativeDummy.getSomeNativeString());
			return contentAddonDummy;
		};
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
