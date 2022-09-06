/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.dataaddon.test.registries;

import io.toky.tokylib.dataaddon.holder.eventsourcing.ConcurrentEventTypeHolder;
import io.toky.tokylib.dataaddon.annotation.group.Holder;

@Holder("dummyGroup")
public class DummyHolder extends ConcurrentEventTypeHolder {}
