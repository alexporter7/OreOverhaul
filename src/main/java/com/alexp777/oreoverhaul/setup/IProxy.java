package com.alexp777.oreoverhaul.setup;

import net.minecraft.world.World;

public interface IProxy {
    void init();
    World getClientWorld();
}
