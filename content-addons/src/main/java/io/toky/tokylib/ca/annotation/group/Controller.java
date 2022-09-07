/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.annotation.group;

import io.toky.tokylib.ca.DataAddonBootstrap;
import io.toky.tokylib.ca.storeload.StoreLoadController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark your class extending {@link StoreLoadController} to use it other infrastructure.
 * @see DataAddonBootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
	/**
	 * @return Unique group tag, needs to mark relation between all other infrastructure.
	 */
	String value();
}
