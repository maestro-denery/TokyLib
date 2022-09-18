/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;

import java.util.stream.Stream;

public interface MarkedContentLookup<T, S> extends ContentLookup<T, S> {
    /**
     * @return A mark, see {@link ContentMark}
     */
    ContentMark<T, S> mark();

    /**
     * @return A lazy-loading lookup obtaining instances of ContentAddon instances.
     */
    @Override
    default Stream<Pair<T, S>> lookup() {
        return natives().filter(n -> mark().matches(n)).map(n -> Pair.of(mark().convert(n), n));
    }
}
