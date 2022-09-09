package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;

import java.util.function.Function;
import java.util.stream.Stream;

public interface ReferencedContentLookup<T, N> extends ContentLookup<T, N> {

    Function<N, T> fromReference();

    @Override
    default Stream<Pair<T, N>> lookup() {
        return natives().map(n -> Pair.of(fromReference().apply(n), n));
    }
}
