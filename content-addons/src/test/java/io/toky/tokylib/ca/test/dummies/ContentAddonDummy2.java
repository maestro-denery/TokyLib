package io.toky.tokylib.ca.test.dummies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.toky.tokylib.ca.annotation.ContentAddon;

@ContentAddon(
        identifier = "test:dummy2",
        lookup = ContentAddonDummy2Lookup.class
)
public final class ContentAddonDummy2 {
    private int someInt;
    private int someNativeInt;

    public static final Codec<ContentAddonDummy2> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("someInt").forGetter(ContentAddonDummy2::someInt),
            Codec.INT.fieldOf("someNativeInt").forGetter(ContentAddonDummy2::someNativeInt)
    ).apply(instance, (s, s2) -> {
        ContentAddonDummy2 contentAddonDummy = new ContentAddonDummy2();
        contentAddonDummy.setSomeInt(s);
        contentAddonDummy.setSomeNativeInt(s2);
        return contentAddonDummy;
    }));

    public int someInt() {
        return someInt;
    }

    public void setSomeInt(int someInt) {
        this.someInt = someInt;
    }

    public int someNativeInt() {
        return someNativeInt;
    }

    public void setSomeNativeInt(int someNativeInt) {
        this.someNativeInt = someNativeInt;
    }
}
