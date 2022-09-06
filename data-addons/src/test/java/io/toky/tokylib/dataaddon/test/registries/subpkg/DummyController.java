/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.dataaddon.test.registries.subpkg;

import io.toky.tokylib.dataaddon.annotation.group.Controller;
import io.toky.tokylib.dataaddon.storeload.DefaultStoreLoadController;

@Controller("dummyGroup")
public class DummyController extends DefaultStoreLoadController {}
