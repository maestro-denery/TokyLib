/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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
