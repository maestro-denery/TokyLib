/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test.registries.subpkg;

import net.drf.dataaddon.annotation.group.Controller;
import net.drf.dataaddon.storeload.DefaultStoreLoadController;

@Controller("dummyGroup")
public class DummyController extends DefaultStoreLoadController {}
