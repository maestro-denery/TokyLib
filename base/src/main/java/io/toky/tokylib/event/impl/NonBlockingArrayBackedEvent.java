/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.event.impl;

import io.toky.tokylib.event.Event;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@ApiStatus.Internal
@ApiStatus.Experimental
public final class NonBlockingArrayBackedEvent<T> extends Event<T> {
    private AtomicReference<T[]> listeners = new AtomicReference<>();
    private final Class<? extends T> type;
    private final Function<T[], T> handler;

    public NonBlockingArrayBackedEvent(Class<? extends T> type, Function<T[], T> handler) {
        this.type = type;
        this.handler = handler;
        this.listeners.set((T[]) Array.newInstance(type, 0));
        this.invoker.set(handler.apply(listeners.get()));
    }

    @Override
    public T invoker() {
        return invoker.get();
    }

    @Override
    public void register(final T listener) {
        for (;;) {
            final T[] oldListeners = incrementLength();
            final T[] newArr = oldListeners.clone();
            newArr[oldListeners.length - 1] = listener;
            if (listeners.compareAndSet(oldListeners, newArr)) {
                this.invoker.set(handler.apply(newArr));
                return;
            }
        }
    }

    private T[] incrementLength() {
        for (;;) {
            final T[] oldListeners = this.listeners.get();
            if (oldListeners.length != 0 && oldListeners[oldListeners.length - 1] == null)
                return oldListeners;
            final T[] newArr = (T[]) Array.newInstance(type, oldListeners.length + 1);
            System.arraycopy(oldListeners, 0, newArr, 0, oldListeners.length);
            if (listeners.compareAndSet(oldListeners, newArr))
                return newArr;
        }
    }

    // Impl detail methods

    public AtomicReference<T[]> getListeners() {
        return listeners;
    }
}
