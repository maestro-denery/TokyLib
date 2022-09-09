/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.registries.subpkg;

import io.toky.tokylib.ca.annotation.group.ContentLookuper;
import io.toky.tokylib.ca.lookup.DefaultStoreLoadController;

@ContentLookuper("dummyGroup")
public class DummyController extends DefaultStoreLoadController {}
