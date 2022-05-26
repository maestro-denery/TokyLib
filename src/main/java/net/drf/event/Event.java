/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.event;

import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * A main class of DRF-Core event system. Event system is mainly inspired by a Fabric event system,
 * but has a few additions and independent of minecraft's NMS.
 * @see EventFactory Making events with EventFactory.
 * @param <T> A type of event you want to source / subscribe.
 */
@ApiStatus.NonExtendable
public abstract class Event<T> {
    protected final AtomicReference<T> invoker = new AtomicReference<>();

    /**
     * Returns an interface invoking all other listeners in an event.
     * Generally, behaviour of this invoker is set by user in
     * {@link EventFactory#createArrayBacked(Class, Function)} (and other similar methods) "eventFactory" function.
     * @return An interface invoking all other listeners in an event.
     */
    public abstract T invoker();

    /**
     * Registers a new listener which is fired by the {@link #invoker()} method,
     * handled by a "eventFactory" function in {@link EventFactory#createArrayBacked(Class, Function)} and other similar methods.
     * @param listener A type of data you want to receive.
     */
    public abstract void register(T listener);
}
