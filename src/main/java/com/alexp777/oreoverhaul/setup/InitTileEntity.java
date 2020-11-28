package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.blocks.highheatfurnace.HighHeatFurnaceTileEntity;
import com.alexp777.oreoverhaul.blocks.orecrusher.OreCrusherTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitTileEntity {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OreOverhaul.MOD_ID);

    public static final RegistryObject<TileEntityType<OreCrusherTileEntity>> ORE_CRUSHER_TILE_ENTITY =
            TILE_ENTITY_TYPE
                                .register(ModSetup.ORE_CRUSHER_REGISTRY_NAME, () -> TileEntityType.Builder
                                .create(OreCrusherTileEntity::new, InitBlock.ORE_CRUSHER.get()).build(null));

    public static final RegistryObject<TileEntityType<HighHeatFurnaceTileEntity>> HIGH_HEAT_FURNACE_TILE_ENTITY =
            TILE_ENTITY_TYPE.register(
                    ModSetup.HIGH_HEAT_FURNACE_REGISTRY_NAME,
                    () -> TileEntityType.Builder.create(HighHeatFurnaceTileEntity::new, InitBlock.HIGH_HEAT_FURNACE.get())
                            .build(null));


}
