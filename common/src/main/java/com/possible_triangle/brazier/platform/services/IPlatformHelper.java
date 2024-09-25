package com.possible_triangle.brazier.platform.services;

import com.tterrag.registrate.AbstractRegistrate;

public interface IPlatformHelper {

    AbstractRegistrate<?> getRegistrate();

    boolean isModLoaded(String modid);

}
