package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.blocks.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class ModSetup {

    //======= Machine Related Block Properties =======
    public static final float MACHINE_HARDNESS = 4.0f;
    public static final SoundType MACHINE_SOUND = SoundType.METAL;
    public static final Material MACHINE_MATERIAL = Material.IRON;

    //======= Ore Related Block Properties =======
    public static final float ORE_HARDNESS = 3.0f;
    public static final SoundType ORE_SOUND = SoundType.STONE;
    public static final Material ORE_MATERIAL = Material.ROCK;

    //======= Create the Mod Item Group for Creative Menu =======
    public ItemGroup oreOverhaulItemGroup = new ItemGroup("oreoverhaul") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.COPPER_BLOCK);
        }
    };

    public void init() {

    }

}