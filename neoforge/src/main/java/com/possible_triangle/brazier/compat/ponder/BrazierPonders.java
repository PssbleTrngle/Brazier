package com.possible_triangle.brazier.compat.ponder;

import net.createmod.ponder.foundation.PonderIndex;

public class BrazierPonders {

    public static void register() {
        PonderIndex.addPlugin(new BrazierPonderPlugin());
    }

}
