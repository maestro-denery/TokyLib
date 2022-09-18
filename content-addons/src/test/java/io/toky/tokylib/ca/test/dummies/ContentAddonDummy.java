/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.dummies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.toky.tokylib.ca.annotation.ContentAddon;

@ContentAddon(
		identifier = "test:dummy",
		lookup = ContentAddonDummyLookup.class
)
public final class ContentAddonDummy {
	private String someString;
	private String someNativeString;

	public static final Codec<ContentAddonDummy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("someString").forGetter(ContentAddonDummy::someString),
			Codec.STRING.fieldOf("someNativeString").forGetter(ContentAddonDummy::someNativeString)
			).apply(instance, (s, s2) -> {
				ContentAddonDummy contentAddonDummy = new ContentAddonDummy();
				contentAddonDummy.setSomeString(s);
				contentAddonDummy.setSomeNativeString(s2);
				return contentAddonDummy;
			}));

	public String someString() {
		return someString;
	}

	public void setSomeString(String someString) {
		this.someString = someString;
	}

	public String someNativeString() {
		return someNativeString;
	}

	public void setSomeNativeString(String someNativeString) {
		this.someNativeString = someNativeString;
	}
}
