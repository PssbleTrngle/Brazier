package io.github.fabricators_of_create.porting_lib.models.generators;

import net.minecraftforge.client.model.generators.ConfiguredModel.Builder;

/**
 * Redirect to allow blockstate generation to be defined in a common module
 */
public class ConfiguredModel {

    public static Builder<?> builder() {
        return net.minecraftforge.client.model.generators.ConfiguredModel.builder();
    }

}
