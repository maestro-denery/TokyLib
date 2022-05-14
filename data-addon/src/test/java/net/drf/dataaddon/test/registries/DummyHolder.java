/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.dataaddon.test.registries;

import net.drf.dataaddon.annotation.group.Holder;
import net.drf.dataaddon.holder.eventsourcing.ConcurrentEventTypeHolder;

@Holder("dummyGroup")
public class DummyHolder extends ConcurrentEventTypeHolder {}
