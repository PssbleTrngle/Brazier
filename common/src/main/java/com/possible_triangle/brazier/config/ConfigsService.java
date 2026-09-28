package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.platform.services.IConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigsService implements IConfigs {

    protected final Pair<IServerConfig, ModConfigSpec> serverConfig;
    protected final Pair<IClientConfig, ModConfigSpec> clientConfig;

    @Nullable
    private IServerConfig syncedServerConfig;

    protected ConfigsService() {
        serverConfig = new ModConfigSpec.Builder().configure(ServerConfig::new);
        clientConfig = new ModConfigSpec.Builder().configure(ClientConfig::new);
    }

    @Override
    public final IServerConfig server() {
        if (syncedServerConfig != null) return syncedServerConfig;
        return serverConfig.getLeft();
    }

    @Override
    public boolean getValueByKey(String key) {
        return serverConfig.getRight().getSpec().getRaw(key);
    }

    @Override
    public final IClientConfig client() {
        return clientConfig.getLeft();
    }

    @Override
    public final void receiveSyncedConfig(IServerConfig config) {
        this.syncedServerConfig = config;
    }
}
