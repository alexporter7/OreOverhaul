package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityType {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OreOverhaul.MOD_ID);

}
