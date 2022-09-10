package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;
import io.toky.tokylib.io.IOUnit;

import java.util.stream.Stream;

public interface ContentLookup<T, S> {
    /**
     * @return Natives lazy stream matching DataAddon requirements.
     */
    Stream<S> natives();

    Stream<Pair<T, S>> lookup();

    <D> IOUnit<T, D> io();
}
