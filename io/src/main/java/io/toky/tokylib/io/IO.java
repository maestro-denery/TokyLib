package io.toky.tokylib.io;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface IO<D> {

    DataResult<D> read(File file);

    DataResult<D> read(InputStream file);

    DataResult<Unit> write(D data, File file);

    DataResult<Unit> write(D data, OutputStream file);
}
