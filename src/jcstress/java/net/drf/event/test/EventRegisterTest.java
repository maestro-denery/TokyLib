/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.event.test;

import net.drf.event.Event;
import net.drf.event.EventFactory;
import net.drf.event.impl.NonBlockingArrayBackedEvent;
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
