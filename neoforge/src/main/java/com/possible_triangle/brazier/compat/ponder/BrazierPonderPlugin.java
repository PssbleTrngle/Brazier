package com.possible_triangle.brazier.compat.ponder;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.index.BrazierBlocks;

import java.util.function.Function;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BrazierPonderPlugin implements PonderPlugin {

    private static Function<Holder<?>, ResourceLocation> byHolder() {
        return it -> it.getKey().location();
    }

    @Override
    public String getModId() {
        return BrazierConstants.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        var holderHelper = helper.withKeyFunction(byHolder());

        holderHelper
                .forComponents(BrazierBlocks.BRAZIER)
                .addStoryBoard("construction", BrazierPonderPlugin::construction);
    }

    private static void construction(SceneBuilder scene, SceneBuildingUtil util) {
        scene.scaleSceneView(0.75F);

        scene.title("construction", "Constructing the brazier");
        scene.configureBasePlate(0, 0, 9);
        scene.showBasePlate();

        var brazierPos = placeLevels(scene, util, 0, 1, true);

        scene.idleSeconds(5);

        scene.world().hideSection(util.select().position(brazierPos), Direction.WEST);

        scene.idle(20);
        scene.world().restoreBlocks(util.select().position(brazierPos));

        scene.addKeyframe();
        placeLevels(scene, util, 1, 4, false);
    }

    private static BlockPos placeLevels(SceneBuilder scene, SceneBuildingUtil util, int fromY, int toY, boolean blockByBlock) {
        assert fromY < toY;

        var origin = util.grid().at(4, 1, 4);

        for (int y = fromY; y < toY; y++) {
            if (blockByBlock) {
                for (int x = 2; x >= -2; x--)
                    for (int z = 2; z >= -2; z--) {
                        if (Math.abs(x) == 2 && Math.abs(z) == 2) continue;
                        var selection = util.select().position(origin.offset(x, y, z));
                        scene.world().showSection(selection, Direction.DOWN);
                        scene.idle(3);
                    }
            } else {
                scene.world().showSection(util.select().layer(origin.getY() + y), Direction.DOWN);
                scene.idle(3);
            }
        }

        var brazierPos = origin.atY(toY + 1);
        scene.world().setBlock(
                brazierPos,
                BrazierBlocks.BRAZIER.getDefaultState(),
                false
        );

        scene.world().showSection(util.select().position(brazierPos), Direction.DOWN);

        scene.idle(20);

        scene.world().cycleBlockProperty(brazierPos, BlockStateProperties.LIT);

        return brazierPos;
    }

}
