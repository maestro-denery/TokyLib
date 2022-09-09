package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;

import java.util.stream.Stream;

public interface MarkedContentLookup<T, N> extends ContentLookup<T, N> {
    /**
     * @return A mark, see {@link TypeMark}
     */
    TypeMark<T, N> mark();

    /**
     * @return A lazy-loading lookup obtaining instances of ContentAddon instances.
     */
    @Override
    default Stream<Pair<T, N>> lookup() {
        return natives().filter(n -> mark().matches(n)).map(n -> Pair.of(mark().convert(n), n));
    }
}
