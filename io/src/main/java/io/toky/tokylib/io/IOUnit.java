package io.toky.tokylib.io;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.io.File;

/**
 *
 * @param elementCodec
 * @param ops
 * @param dataFile
 * @param <D>
 * @param <T>
 */
public record IOUnit<T, D>(IO<D> io, Codec<T> elementCodec, DynamicOps<D> ops, File dataFile) {
    public DataResult<T> read() {
        return io.read(dataFile).flatMap(dataFormat -> elementCodec.parse(this.ops, dataFormat));
    }

    public DataResult<Unit> write(T t) {
        return this.elementCodec.encodeStart(ops, t).flatMap(data -> io.write(data, dataFile));
    }
}
