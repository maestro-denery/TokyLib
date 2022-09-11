package io.toky.tokylib.ca.lookup;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.holder.TIHCodecs;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
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

    <D> Optional<StandaloneFileOps<T, D>> standaloneFileOps();

    final class StandaloneFileOps<T, D> {
        private final IO<D> io;
        private final Codec<T> elementCodec;
        private final DynamicOps<D> ops;
        private final File dataFile;
        final Codec<TypeInstanceHolder.TIHEntry<T>> standaloneFileCodec;
        public StandaloneFileOps(IO<D> io, ResourceKey<? extends ResourceKeyed<T>> key, Codec<T> elementCodec, DynamicOps<D> ops, File dataFile) {
            this.io = io;
            this.elementCodec = elementCodec;
            this.ops = ops;
            this.dataFile = dataFile;
            this.standaloneFileCodec = TIHCodecs.standaloneFileCodec(key, elementCodec).getSecond();
        }

        public DataResult<TypeInstanceHolder.TIHEntry<T>> read() {
            return io.read(dataFile).flatMap(dataFormat -> standaloneFileCodec.parse(this.ops, dataFormat));
        }

        public DataResult<Unit> write(final TypeInstanceHolder.TIHEntry<T> t) {
            return this.standaloneFileCodec.encodeStart(ops, t).flatMap(data -> io.write(data, dataFile));
        }
    }
}
