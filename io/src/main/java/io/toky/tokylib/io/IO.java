package io.toky.tokylib.io;

import com.mojang.serialization.DataResult;

import java.io.File;

public interface IO<E> {

    DataResult<E> read(File file);

    DataResult<Void> write(File file);
}
