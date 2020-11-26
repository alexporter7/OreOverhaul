package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.blocks.ModBlocks;
import com.alexp777.oreoverhaul.blocks.OreCrusherScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        //Register the Ore Crusher Screen
        ScreenManager.registerFactory(ModBlocks.ORE_CRUSHER_CONTAINER, OreCrusherScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
