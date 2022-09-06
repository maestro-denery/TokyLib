/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package io.toky.tokylib.event.test;

import io.toky.tokylib.event.Event;
import io.toky.tokylib.event.EventFactory;
import io.toky.tokylib.event.impl.NonBlockingArrayBackedEvent;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

@JCStressTest
@Outcome(id = "2", expect = Expect.ACCEPTABLE)
@State
public class EventRegisterTest {
    final Event<Sample> SAMPLE_EVENT = EventFactory.createArrayBacked(Sample.class, (listeners) -> i -> {
        for (Sample listener : listeners) {
            listener.sample(i);
        }
    });

    @Actor
    public void listener1() {
       SAMPLE_EVENT.register(i -> {});
    }

    @Actor
    public void listener2() {
        SAMPLE_EVENT.register(i -> {});
    }

    @Arbiter
    public void finish(I_Result result) {
        int length = ((NonBlockingArrayBackedEvent<Sample>) SAMPLE_EVENT).getListeners().get().length;
        result.r1 = length;
    }

    interface Sample {
        void sample(int i);
    }
}
