package com.possible_triangle.brazier;

import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ForgeRegistrate extends MultikultiRegistrate<ForgeRegistrate> {

    public ForgeRegistrate() {
        super(Brazier.MOD_ID);
    }

    @Override
    public ForgeRegistrate registerEventListeners(IEventBus bus) {
        return super.registerEventListeners(bus);
    }

}
