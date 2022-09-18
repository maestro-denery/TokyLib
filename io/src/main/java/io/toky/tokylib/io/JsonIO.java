package io.toky.tokylib.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class JsonIO implements IO<JsonElement> {
    private JsonIO() {}

    public static final JsonIO INSTANCE = new JsonIO();

    @Override
    public DataResult<JsonElement> read(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return this.read(fileInputStream);
        } catch (IOException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @Override
    public DataResult<JsonElement> read(InputStream file) {
        InputStreamReader inputStreamReader = new InputStreamReader(file, StandardCharsets.UTF_8);
        return DataResult.success(JsonParser.parseReader(inputStreamReader));
    }

    @Override
    public DataResult<Unit> write(JsonElement data, File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            return write(data, outputStream);
        } catch (IOException e) {
            return DataResult.error(e.getMessage());
        }
    }

    @Override
    public DataResult<Unit> write(JsonElement data, OutputStream file) {
        try {
            file.write(data.toString().getBytes(StandardCharsets.UTF_8));
            return DataResult.success(Unit.INSTANCE);
        } catch (IOException e) {
            return DataResult.error(e.getMessage());
        }
    }
}
