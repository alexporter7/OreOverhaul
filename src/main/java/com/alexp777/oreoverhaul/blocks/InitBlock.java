package com.alexp777.oreoverhaul.blocks;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.orecrusher.OreCrusherBlock;
import com.alexp777.oreoverhaul.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class InitBlock {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OreOverhaul.MOD_ID);

    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", CopperBlock::new);
    public static final RegistryObject<Block> ORE_CRUSHER = BLOCKS.register(ModSetup.ORE_CRUSHER_REGISTRY_NAME, OreCrusherBlock::new);

}
