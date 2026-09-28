package com.possible_triangle.brazier.platform;

import com.possible_triangle.brazier.platform.services.*;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IConfigs CONFIGS = load(IConfigs.class);
    public static final IDatagen DATAGEN = load(IDatagen.class, StubDatagen::new);

    private static <T> T load(Class<T> clazz) {
        var classLoader = Services.class.getClassLoader();
        return ServiceLoader.load(clazz, classLoader)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    private static <T> T load(Class<T> clazz, Supplier<T> fallback) {
        var classLoader = Services.class.getClassLoader();
        return ServiceLoader.load(clazz, classLoader)
                .findFirst()
                .orElseGet(fallback);
    }

    public static class Client {
        public static final IClientHelper PLATFORM = load(IClientHelper.class);
    }

}
