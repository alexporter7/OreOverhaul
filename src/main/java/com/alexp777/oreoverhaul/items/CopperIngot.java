package com.alexp777.oreoverhaul.items;

import com.alexp777.oreoverhaul.OreOverhaul;
import net.minecraft.item.Item;

public class CopperIngot extends Item {

    public CopperIngot() {
        super(new Item.Properties()
                .group(OreOverhaul.modSetup.oreOverhaulItemGroup)
                .maxStackSize(64));
        setRegistryName("copper_ingot");
    }
}
