package io.toky.tokylib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;

public final class KeyCodecs {
    private KeyCodecs() {}

    public static final Codec<Key> KEY_CODEC = Codec.STRING.comapFlatMap(KeyCodecs::readKey, Key::asString);

    public static <T> Codec<ResourceKey<T>> codec(ResourceKey<? extends ResourceKeyed<T>> resourceKeyedKey) {
        return KeyCodecs.KEY_CODEC.xmap(key -> ResourceKey.create(resourceKeyedKey, key), ResourceKey::entryKey);
    }

    public static DataResult<Key> readKey(String s) {
        try {
            return DataResult.success(Key.key(s));
        } catch (InvalidKeyException e) {
            return DataResult.error("Invalid key format: " + s + " " + e.getMessage());
        }
    }
}
