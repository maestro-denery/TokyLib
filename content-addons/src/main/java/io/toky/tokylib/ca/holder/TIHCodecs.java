package io.toky.tokylib.ca.holder;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.toky.tokylib.KeyCodecs;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ResourceKeyed;
import io.toky.tokylib.ca.holder.impl.TypeInstanceHolderImpl;

import java.util.Map;

public final class TIHCodecs {
    private TIHCodecs() {}

    public static <T, K extends ResourceKey<? extends ResourceKeyed<?>>, V extends ContentAddonContainer.TIHEntry<?>> Codec<ContentAddonContainer> typeInstanceHolderCodec(
            ResourceKey<? extends ResourceKeyed<T>> key,
            Codec<T> elementCodec
    ) {
        UnboundedMapCodec<K, V> unboundedMapCodec = (UnboundedMapCodec<K, V>) TIHCodecs.rawTIHCodec(key, elementCodec);
        return unboundedMapCodec.xmap(resourceKeyTIHEntryMap -> {
            final TypeInstanceHolderImpl typeInstanceHolder = new TypeInstanceHolderImpl();
            resourceKeyTIHEntryMap.forEach((resourceKey, instances) -> {
                typeInstanceHolder.holdAll((ResourceKey<? extends ResourceKeyed<T>>) resourceKey, (ContentAddonContainer.TIHEntry<T>) instances);
            });
            return typeInstanceHolder;
        }, typeInstanceHolder -> (Map<K, V>) ((TypeInstanceHolderImpl) typeInstanceHolder).getInstancesInternalMap());
    }

    public static <T> UnboundedMapCodec<? extends ResourceKey<? extends ResourceKeyed<?>>, ? extends ContentAddonContainer.TIHEntry<?>> rawTIHCodec(
            ResourceKey<? extends ResourceKeyed<T>> key,
            Codec<T> elementCodec
    ) {
        Pair<Codec<ResourceKey<? extends ResourceKeyed<T>>>, Codec<ContentAddonContainer.TIHEntry<T>>> codecCodecPair =
                TIHCodecs.standaloneFileCodec(key, elementCodec);
        return Codec.unboundedMap(codecCodecPair.getFirst(), codecCodecPair.getSecond());
    }

    public static <T> Pair<Codec<ResourceKey<? extends ResourceKeyed<T>>>, Codec<ContentAddonContainer.TIHEntry<T>>> standaloneFileCodec(
            ResourceKey<? extends ResourceKeyed<T>> key,
            ContentAddonContainer tih
    ) {
        final Codec<ResourceKey<? extends ResourceKeyed<T>>> codec = KeyCodecs.KEY_CODEC.xmap(ResourceKey::createResourceKeyedKey, ResourceKey::entryKey);
        final Codec<ContentAddonContainer.TIHEntry<T>> tihEntryCodec = tih.release().listOf().xmap(
                ts -> new ContentAddonContainer.TIHEntry<>(key, ts),
                tihEntry -> tihEntry.stream().toList()
        );
        Codec<ContentAddonContainer.TIHEntry<T>> type = codec.partialDispatch("type",
                keyedColl -> DataResult.success(keyedColl.key()),
                rkey -> DataResult.success(tihEntryCodec)
        );
        return Pair.of(codec, type);
    }
}
