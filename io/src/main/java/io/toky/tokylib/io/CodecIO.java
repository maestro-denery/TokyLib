package io.toky.tokylib.io;

import com.mojang.serialization.DataResult;

import java.io.File;

public interface CodecIO<E> extends IO<E> {

    IOConfig<E> config();
}
