package com.alexp777.oreoverhaul.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import static com.alexp777.oreoverhaul.setup.ModSetup.COPPER_BLOCK_REGISTRY_NAME;

public class CopperBlock extends Block {

    public CopperBlock() {
        //Create Block Properties
        super(Properties
                .create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(3.0f)
        );
        //Create localized Registry name
        setRegistryName(COPPER_BLOCK_REGISTRY_NAME);
    }

}
