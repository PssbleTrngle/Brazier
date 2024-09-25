package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.Brazier;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.Registrate;
import net.minecraftforge.fml.ModList;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public AbstractRegistrate<?> getRegistrate() {
        return Registrate.create(Brazier.MOD_ID);
    }

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

}
