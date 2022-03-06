package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.tile.render.BrazierRenderer;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import com.possible_triangle.brazier.entity.render.CrazedRender;
import com.possible_triangle.brazier.particle.FlameParticle;
import com.possible_triangle.brazier.particle.ParticleRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class ClientContent {

    private ClientContent() {}

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {
        Content.FLAME_PARTICLE.ifPresent(type -> ParticleRegistry.registerFactory(type, FlameParticle::new));
    }

    @Environment(EnvType.CLIENT)
    public static void setup() {
        Content.CRAZED.ifPresent(type -> EntityRenderers.register(type, CrazedRender::new));
        Content.CRAZED_FLAME.ifPresent(type -> EntityRenderers.register(type, CrazedFlameRenderer::new));

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL, Content.SPAWN_POWDER, Content.LIVING_LANTERN)
                .filter(RegistrySupplier::isPresent)
                .map(RegistrySupplier::get)
                .map(Block.class::cast)
                .forEach(block -> RenderTypeRegistry.register(RenderType.cutout(), block));

        Content.BRAZIER_TILE.ifPresent(tile -> net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(tile, $ -> new BrazierRenderer()));
    }

}
