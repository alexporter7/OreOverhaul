package com.alexp777.oreoverhaul.blocks;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    //======= Metal Blocks =======
    @ObjectHolder("oreoverhaul:copper_block")
    public static CopperBlock COPPER_BLOCK;

    //======= Machine Blocks =======
    @ObjectHolder("oreoverhaul:ore_crusher_block")
    public static OreCrusherBlock ORE_CRUSHER_BLOCK;

    //======= Tile Entities =======
    @ObjectHolder("oreoverhaul:ore_crusher_block")
    public static TileEntityType<OreCrusherTileEntity> ORE_CRUSHER_TILE_ENTITY;

    //======= Containers =======
    @ObjectHolder("oreoverhaul:ore_crusher_block")
    public static ContainerType<OreCrusherContainer> ORE_CRUSHER_CONTAINER;

}
