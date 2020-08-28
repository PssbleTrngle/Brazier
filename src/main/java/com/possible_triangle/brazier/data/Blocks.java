package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.block.LazyWallTorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;

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

        Content.LIVING_TORCH_BLOCK.ifPresent(b -> simpleBlock(b,
                models().singleTexture(
                        b.getRegistryName().getPath(),
                        mcLoc("block/template_torch"),
                        blockTexture(b))
                )
        );

        Content.LIVING_TORCH_BLOCK_WALL.ifPresent(b -> {
                    ModelFile model = models().singleTexture(
                            b.getRegistryName().getPath(),
                            mcLoc("block/template_torch_wall"),
                            blockTexture(Content.LIVING_TORCH_BLOCK.get())
                    );

                    getVariantBuilder(b).forAllStates(state -> {
                        Direction facing = state.get(WallTorchBlock.HORIZONTAL_FACING);
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .rotationY((int) facing.getHorizontalAngle() + 90)
                                .build();
                    });
                }
        );

    }
}
