/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.toky.tokylib.KeyCodecs;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.container.impl.ContentAddonContainerImpl;

import java.util.Map;

public final class ContentAddonCodecs {
    private ContentAddonCodecs() {}

    public static <T> Codec<ContentAddonContainer> contentAddonContainerCodec(
            ContentAddonRegistry registry
    ) {
        final Codec<ResourceKey<T>> resourceKeyCodec = KeyCodecs.KEY_CODEC.xmap(key -> ResourceKey.create(registry, key), ResourceKey::entryKey);
        final Codec<ContentAddonContainer.Entry<T>> type = resourceKeyCodec.partialDispatch("type",
                entry -> DataResult.success(entry.key()),
                resourceKey -> DataResult.success(ContentAddonCodecs.standaloneFileCodec(resourceKey, registry.getContentLookup(resourceKey).codec()))
        );

        return captureMap(Codec.unboundedMap(resourceKeyCodec, type));
    }

    public static <K extends ResourceKey<?>, V extends ContentAddonContainer.Entry<?>> Codec<ContentAddonContainer> captureMap(UnboundedMapCodec<K, V> mapCodec) {
        return mapCodec.xmap(kvMap -> {
            return ContentAddonContainerImpl.createFromMap((Map<ResourceKey<?>, ContentAddonContainer.Entry<?>>) kvMap);
        }, contentAddonContainer -> {
            return (Map<K, V>) ((ContentAddonContainerImpl) contentAddonContainer).getInstancesInternalMap();
        });
    }

    public static <T> Codec<ContentAddonContainer.Entry<T>> standaloneFileCodec(
            ResourceKey<T> key,
            Codec<T> elementCodec
    ) {
        return elementCodec.listOf().xmap(
                ts -> ContentAddonContainer.Entry.create(key, ts),
                entry -> entry.stream().toList()
        );
    }
}
