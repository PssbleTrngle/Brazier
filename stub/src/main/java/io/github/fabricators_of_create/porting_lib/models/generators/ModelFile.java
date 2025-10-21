package io.github.fabricators_of_create.porting_lib.models.generators;

import net.minecraft.resources.ResourceLocation;

/**
 * Redirect to allow blockstate generation to be defined in a common module
 */
public abstract class ModelFile extends net.minecraftforge.client.model.generators.ModelFile {

    protected ModelFile(ResourceLocation location) {
        super(location);
    }

    public abstract class ExistingModelFile extends net.minecraftforge.client.model.generators.ModelFile {

        protected ExistingModelFile(ResourceLocation location) {
            super(location);
        }

    }

}
