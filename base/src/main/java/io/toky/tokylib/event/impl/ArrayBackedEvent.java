/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.event.impl;

import io.toky.tokylib.event.Event;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

@ApiStatus.Internal
public final class ArrayBackedEvent<T> extends Event<T> {
    private T[] listeners;
    private final Class<? extends T> type;
    private final Function<T[], T> handler;
    private final ReentrantLock lock = new ReentrantLock();

    public ArrayBackedEvent(Class<? extends T> type, Function<T[], T> handler) {
        this.type = type;
        this.handler = handler;
        this.listeners = (T[]) Array.newInstance(type, 0);
        this.invoker.set(handler.apply(listeners));
    }

    @Override
    public T invoker() {
        return invoker.get();
    }

    @Override
    public void register(T listener) {
        lock.lock();
        incrementLength();
        this.listeners[listeners.length - 1] = listener;
        this.invoker.set(handler.apply(listeners));
        lock.unlock();
    }

    private void incrementLength() {
        final T[] oldListeners = this.listeners;
        final T[] newArr = (T[]) Array.newInstance(type, oldListeners.length + 1);
        System.arraycopy(oldListeners, 0, newArr, 0, oldListeners.length);
        this.listeners = newArr;
    }
}
