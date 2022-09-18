package io.toky.tokylib.ca.test.dummies;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.lookup.ReferencedContentLookup;
import io.toky.tokylib.io.JsonIO;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContentAddonDummy2Lookup implements ReferencedContentLookup<ContentAddonDummy2, ContentAddonDummy2Lookup.NativeDummy> {
    private final File dummyFile = new File("src/test/resources/dummyData2.json");

    private final Collection<ContentAddonDummy2Lookup.NativeDummy> nativeDummiesContainer = Lists.newArrayList(
            new ContentAddonDummy2Lookup.NativeDummy(12)
    );

    @Override
    public Stream<ContentAddonDummy2Lookup.NativeDummy> natives() {
        return nativeDummiesContainer.stream();
    }

    @Override
    public Codec<ContentAddonDummy2> codec() {
        return ContentAddonDummy2.CODEC;
    }

    @Override
    public Optional<StandaloneFileOps<ContentAddonDummy2, ?>> standaloneFileOps(ResourceKey<ContentAddonDummy2> resourceKey) {
        return Optional.of(new StandaloneFileOps<>(this, JsonIO.INSTANCE, JsonOps.INSTANCE, dummyFile));
    }

    @Override
    public Function<ContentAddonDummy2Lookup.NativeDummy, ContentAddonDummy2> fromReference() {
        return nativeDummy -> {
            final ContentAddonDummy2 contentAddonDummy = new ContentAddonDummy2();
            contentAddonDummy.setSomeNativeInt(nativeDummy.getSomeNativeString());
            return contentAddonDummy;
        };
    }

    public static class NativeDummy {
        private final int someNativeInt;
        public NativeDummy(int nativeInt) {
            this.someNativeInt = nativeInt;
        }

        public int getSomeNativeString() {
            return someNativeInt;
        }
    }
}
