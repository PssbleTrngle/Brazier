package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.config.IClientConfig;
import com.possible_triangle.brazier.config.IServerConfig;

public interface IConfigs {

    IServerConfig server();

    IClientConfig client();

    void receiveSyncedConfig(IServerConfig config);

    void register();

    boolean getValueByKey(String key);

}
