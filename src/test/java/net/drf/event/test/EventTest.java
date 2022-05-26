/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package net.drf.event.test;

import net.drf.event.Event;
import net.drf.event.EventFactory;
import org.junit.jupiter.api.Test;

class EventTest {
    @Test
    void test() {
        SomeCallback.EVENT.register((i, b) -> {
            System.out.println(i + " : " + b);
        });

        SomeCallback.EVENT.register((i, b) -> {
            System.out.println(i + " -- " + b);
        });

        SomeCallback.EVENT.invoker().some(12, false);
        SomeCallback.EVENT.invoker().some(14, true);

    }

    interface SomeCallback {
        Event<SomeCallback> EVENT = EventFactory.createArrayBacked(SomeCallback.class, (listeners) -> (int i, boolean b) -> {
            for (SomeCallback listener : listeners) {
                listener.some(i, b);
            }
        });

        void some(int i, boolean b);
    }
}
