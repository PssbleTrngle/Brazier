package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.BrazierBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class Blocks extends BlockStateProvider {

    public Blocks(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Brazier.MODID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        Content.BRAZIER.ifPresent(b -> getVariantBuilder(b).forAllStates(s -> {
            boolean lit = s.get(BrazierBlock.LIT);
            ResourceLocation r = blockTexture(b);
            ResourceLocation model = lit ? new ResourceLocation(r.getNamespace(), r.getPath() + "_lit") : r;
            return ConfiguredModel.builder()
                    .modelFile(this.models().getExistingFile(model))
                    .build();
        }));

    }
}
