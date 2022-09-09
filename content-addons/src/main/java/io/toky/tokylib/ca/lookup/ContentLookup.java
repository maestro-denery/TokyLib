package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;
import io.toky.tokylib.io.IO;

import java.util.stream.Stream;

public interface ContentLookup<T, N> {
    /**
     * @return Natives lazy stream matching DataAddon requirements.
     */
    Stream<N> natives();

    Stream<Pair<T, N>> lookup();

    IO<T> io();
}
