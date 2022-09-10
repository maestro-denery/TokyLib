package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;

import java.util.function.Function;
import java.util.stream.Stream;

public interface ReferencedContentLookup<T, S> extends ContentLookup<T, S> {

    Function<S, T> fromReference();

    @Override
    default Stream<Pair<T, S>> lookup() {
        return natives().map(n -> Pair.of(fromReference().apply(n), n));
    }
}
