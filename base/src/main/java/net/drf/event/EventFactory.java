/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.event;

import net.drf.event.impl.ArrayBackedEvent;
import net.drf.event.impl.NonBlockingArrayBackedEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * A class providing utility functions for creating {@link Event} instances for creating custom events.
 */
public final class EventFactory {
    private EventFactory() {}

    /**
     * Creates a usual thread-safe event
     * @param type A class of a type of data you want to receive.
     * @param eventFactory A function firing listeners in a way defined by a user when "invoker" fires.
     * @return An event instance which other users can thread-safely subscribe.
     * @param <T> A type of data you want to source / subscribe.
     */
    public static <T> Event<T> createArrayBacked(Class<? extends T> type, Function<T[], T> eventFactory) {
        return new ArrayBackedEvent<>(type, eventFactory);
    }

    /**
     * !WARNING! This implementation is experimental and may cause race-conditions!
     * <p>
     * Same as {@link #createArrayBacked(Class, Function)} but uses faster non-blocking synchronization for providing thread-safety.
     * @return An event instance which other users can thread-safely subscribe in a non-blocking manner.
     */
    @ApiStatus.Experimental
    public static <T> Event<T> createNonBlockingArrayBacked(Class<? extends T> type, Function<T[], T> eventFactory) {
        return new NonBlockingArrayBackedEvent<>(type, eventFactory);
    }
}
