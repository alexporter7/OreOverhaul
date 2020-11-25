package com.alexp777.oreoverhaul.setup;

import net.minecraft.world.World;

public class ServerProxy implements IProxy{

    @Override
    public void init() {

    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Can only be run on the Client!");
    }

}
