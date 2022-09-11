package io.toky.tokylib.ca.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.holder.TIHCodecs;
import io.toky.tokylib.ca.holder.TypeInstanceHolder;
import io.toky.tokylib.ca.holder.impl.TypeInstanceHolderImpl;
import io.toky.tokylib.ca.type.TypeRegistry;
import io.toky.tokylib.ca.type.impl.TypeRegistryImpl;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

class TIHCodecsTest {
    @Test
    void check() {
        TypeInstanceHolder tih = new TypeInstanceHolderImpl();
        Key test_type_reg = Key.key("test_type_reg");
        TypeRegistry tr = new TypeRegistryImpl(test_type_reg);
        TIHCodecs.typeInstanceHolderCodec(ResourceKey.createResourceKeyedKey(test_type_reg), Some.CODEC);
    }

    record Some(String s, int i) {
        public static final Codec<Some> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(Codec.STRING.fieldOf("s").forGetter(Some::s), Codec.INT.fieldOf("i").forGetter(Some::i)).apply(instance, Some::new));
    }
}
