package com.possible_triangle.brazier.entity.fabric;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.function.Function;

public class CustomEntityNetworking {

    private static final HashMap<String, Function<Level, ? extends Entity>> HANDLERS = new HashMap<>();

    public static void register() {
        //HANDLERS.forEach((name, creator) ->
        //        ClientSidePacketRegistryImpl.INSTANCE.register(new ResourceLocation(Brazier.MOD_ID, name), (ctx, buffer) -> {
        //            EntityData data = new EntityData(buffer);
        //            ctx.getTaskQueue().execute(() -> {
        //                Entity entity = creator.apply(ctx.getPlayer().level);
        //                entity.setPos(data.x, data.y, data.z);
        //                entity.setId(data.id);
        //                entity.setUUID(data.uuid);
        //                ((ClientLevel) ctx.getPlayer().level).putNonPlayerEntity(data.id, entity);
        //            });
        //        })
        //);
    }

    public static void registerHandler(String name, Function<Level, ? extends Entity> creator) {
        HANDLERS.put(name, creator);
    }

    public static void notifyClients(Entity entity, String name) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        buf.writeVarInt(entity.getId());
        buf.writeUUID(entity.getUUID());
        buf.writeDouble(entity.getX());
        buf.writeDouble(entity.getY());
        buf.writeDouble(entity.getZ());

        for (ServerPlayer player : ((ServerLevel) entity.level).players()) {
            //ServerSidePacketRegistryImpl.INSTANCE.sendToPlayer(player, new ResourceLocation(Brazier.MOD_ID, name), buf);
        }
    }

}
