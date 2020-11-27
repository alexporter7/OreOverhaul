package com.alexp777.oreoverhaul.items;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.setup.ModSetup;
import net.minecraft.item.Item;

public class CopperIngot extends Item {

    public CopperIngot() {
        super(new Item.Properties()
                .group(ModSetup.ORE_OVERHAUL_ITEM_GROUP)
                .maxStackSize(64));
    }
}
