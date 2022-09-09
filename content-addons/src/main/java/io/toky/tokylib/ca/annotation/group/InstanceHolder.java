/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.annotation.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.toky.tokylib.ca.DataAddonBootstrap;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.lookup.Lookuper;

/**
 * Mark your class extending {@link TypeInstanceHolder} to use it with other infrastructure.
 * @see DataAddonBootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InstanceHolder {
	/**
	 * @return Unique String identifier, needs to be in a format "foo:bar", used to group {@link Lookuper}, {@link io.toky.tokylib.ca.holder.TypeInstanceHolder} and {@link io.toky.tokylib.ca.type.TypeRegistry}, so they will work as a one mechanism.
	 */
	String value();
}
