/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.type.ContentAddonCodecs;
import io.toky.tokylib.io.IO;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public interface ContentLookup<T, S> {
    /**
     * @return Natives lazy stream matching ContentAddon requirements.
     */
    Stream<S> natives();

    Stream<Pair<T, S>> lookup();

    Codec<T> codec();

    Optional<StandaloneFileOps<T, ?>> standaloneFileOps(ResourceKey<T> resourceKey);

    record StandaloneFileOps<T, D>(ContentLookup<T, ?> lookup, IO<D> io, DynamicOps<D> ops, File dataFile) {
        public DataResult<ContentAddonContainer.Entry<T>> read(final ResourceKey<T> resourceKey) {
            return io.read(dataFile).flatMap(dataFormat -> ContentAddonCodecs.standaloneFileCodec(resourceKey, lookup.codec()).parse(this.ops, dataFormat));
        }

        public DataResult<Unit> write(final ResourceKey<T> resourceKey, final ContentAddonContainer.Entry<T> t) {
            return ContentAddonCodecs.standaloneFileCodec(resourceKey, lookup.codec()).encodeStart(ops, t).flatMap(data -> io.write(data, dataFile));
        }
    }
}
