/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.ca.test.registries;

import io.toky.tokylib.ca.holder.eventsourcing.ConcurrentEventTypeHolder;
import io.toky.tokylib.ca.annotation.group.InstanceHolder;

@InstanceHolder("dummyGroup")
public class DummyHolder extends ConcurrentEventTypeHolder {}
