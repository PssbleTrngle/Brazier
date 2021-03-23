package com.possible_triangle.brazier.forge.data;

import com.google.gson.JsonObject;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Conditional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootInjectModifier extends LootModifier {

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new LootInjectModifier.Serializer().setRegistryName(new ResourceLocation(Brazier.MOD_ID, "loot_inject")));
    }

    private boolean enabled;

    private LootInjectModifier(boolean enabled, LootItemCondition[] conditions) {
        super(Optional.ofNullable(conditions).orElse(new LootItemCondition[0]));
        this.enabled = enabled;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> list, LootContext ctx) {
        if(enabled) Conditional.injectLoot(ctx.getQueriedLootTableId(), builder -> {
            LootPool pool = builder.build();
            pool.addRandomItems(list::add, ctx);
        });
        return list;
    }

    private static class Serializer extends GlobalLootModifierSerializer<LootInjectModifier> {

        @Override
        public JsonObject write(LootInjectModifier modifier) {
            JsonObject json = new JsonObject();
            json.addProperty("enabled", modifier.enabled);
            return json;
        }

        @Override
        public LootInjectModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            return new LootInjectModifier(object.get("enabled").getAsBoolean(), conditions);
        }
    }

}
