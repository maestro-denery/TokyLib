/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.annotation;

import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.ContentLookup;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark your class with this annotation if it represents object with additional data over some other object data
 * to connect and use it with {@link ContentAddonRegistry}, {@link ContentAddonContainer}, {@link DataLookerUpper} and other infrastructure.
 * @see ContentAddonBootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ContentAddon {
	/**
	 * @return Unique String identifier, used in {@link ContentAddonRegistry} and {@link ContentAddonContainer}, needs to be in a format "foo:bar".
	 */
	@Pattern("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+")
	String identifier();

	/**
	 * @return A "Lookup" which has a data to perform IO operations, serialization / deserialization, looks for "native" data, makes instances with this type and holds them in a {@link ContentAddonContainer} by a group tag.
	 */
	Class<? extends ContentLookup<?, ?>> lookup();
}
