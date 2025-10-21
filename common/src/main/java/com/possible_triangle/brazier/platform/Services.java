package com.possible_triangle.brazier.platform;

import com.possible_triangle.brazier.platform.services.IClientHelper;
import com.possible_triangle.brazier.platform.services.IConfigs;
import com.possible_triangle.brazier.platform.services.IDatagen;
import com.possible_triangle.brazier.platform.services.IPlatformHelper;
import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IConfigs CONFIGS = load(IConfigs.class);
    public static final IDatagen DATAGEN = load(IDatagen.class);

    private static <T> T load(Class<T> clazz) {
        var classLoader = Services.class.getClassLoader();
        return ServiceLoader.load(clazz, classLoader)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    public static class Client {
        public static final IClientHelper PLATFORM = load(IClientHelper.class);
    }

}