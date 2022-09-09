package io.toky.tokylib.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;

import java.io.File;

public record IOConfig<T>(Codec<T> elementCodec, DynamicOps<?> ops, File dataFile) {
}
