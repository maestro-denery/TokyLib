package io.toky.tokylib.ca.test;

import com.mojang.serialization.JsonOps;
import io.toky.tokylib.ResourceKey;
import io.toky.tokylib.ca.ContentAddonBootstrap;
import io.toky.tokylib.ca.InfrastructureContainer;
import io.toky.tokylib.ca.container.ContentAddonContainer;
import io.toky.tokylib.ca.lookup.DataLookerUpper;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy;
import io.toky.tokylib.ca.test.dummies.ContentAddonDummy2;
import io.toky.tokylib.ca.type.ContentAddonRegistry;
import io.toky.tokylib.io.JsonIO;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class ContentAddonCodecsTest {
    ContentAddonBootstrap bootstrap = new ContentAddonBootstrap();
    final Key key = Key.key("test", "dummy_type_registry");
    ResourceKey<ContentAddonDummy> resourceKey = null;
    ResourceKey<ContentAddonDummy2> resourceKey2 = null;

    @BeforeEach
    void before() {
        bootstrap.setContainer(new InfrastructureContainer());
        bootstrap.bootstrapSystem(ContentAddonRegistry.create(key), ContentAddonContainer.create(), DataLookerUpper.create());
        resourceKey = bootstrap.getContainer().contentAddonRegistry().register(ContentAddonDummy.class);
        resourceKey2 = bootstrap.getContainer().contentAddonRegistry().register(ContentAddonDummy2.class);
    }

    @Test
    void checkStoreStandalone() {
        for (int i = 0; i < 2; i++) {
            ContentAddonDummy contentAddonDummy = bootstrap.getContainer().contentAddonContainer().newInstance(resourceKey);
            contentAddonDummy.setSomeString("non native shit");
            contentAddonDummy.setSomeNativeString("native shit");
        }
        bootstrap.getContainer().dataLookerUpper().storeStandaloneFile(resourceKey);
    }

    @Test
    void checkLoadStandalone() {
        bootstrap.getContainer().dataLookerUpper().loadStandaloneFile(resourceKey);
        System.out.println(bootstrap.getContainer().contentAddonContainer().getHeld(resourceKey));
    }

    @Test
    void checkStore() {
        for (int i = 0; i < 2; i++) {
            ContentAddonDummy contentAddonDummy = bootstrap.getContainer().contentAddonContainer().newInstance(resourceKey);
            contentAddonDummy.setSomeString("non native shit");
            contentAddonDummy.setSomeNativeString("native shit");
        }
        for (int i = 0; i < 2; i++) {
            ContentAddonDummy2 contentAddonDummy2 = bootstrap.getContainer().contentAddonContainer().newInstance(resourceKey2);
            contentAddonDummy2.setSomeInt(134);
            contentAddonDummy2.setSomeNativeInt(152);
        }
        bootstrap.getContainer().dataLookerUpper().store(new File("src/test/resources/cacData.json"), JsonIO.INSTANCE, JsonOps.INSTANCE);
    }
}
