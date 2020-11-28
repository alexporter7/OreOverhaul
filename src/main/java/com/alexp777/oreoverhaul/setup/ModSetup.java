package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.InitBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.Hashtable;

public class ModSetup {

    //======= Registry Names =======
    public static final String COPPER_BLOCK_REGISTRY_NAME = "copper_block";

    public static final String ORE_CRUSHER_REGISTRY_NAME = "ore_crusher_block";
    public static final String ORE_CRUSHER_TE_REGISTRY_NAME = "ore_crusher_tile_entity";
    public static final String ORE_CRUSHER_CONTAINER_REGISTRY_NAME = "ore_crusher_container";

    public static final String HIGH_HEAT_FURNACE_REGISTRY_NAME = "high_heat_furnace_block";
    public static final String HIGH_HEAT_FURNACE_CONTAINER_REGISTRY_NAME = "high_heat_furnace_container";
    public static final String HIGH_HEAT_FURNACE_SERIALIZER_REGISTRY_NAME = "high_heat_furnace_recipe_serializer";

    //======= Machine Related Block Properties =======
    public static final float MACHINE_HARDNESS = 4.0f;
    public static final SoundType MACHINE_SOUND = SoundType.METAL;
    public static final Material MACHINE_MATERIAL = Material.IRON;

    //======= Ore Related Block Properties =======
    public static final float ORE_HARDNESS = 3.0f;
    public static final SoundType ORE_SOUND = SoundType.STONE;
    public static final Material ORE_MATERIAL = Material.ROCK;

    //======= Machine Block Properties =======
    public static final AbstractBlock.Properties MACHINE_PROPERTIES = AbstractBlock.Properties
                                                                        .create(MACHINE_MATERIAL)
                                                                        .hardnessAndResistance(MACHINE_HARDNESS)
                                                                        .sound(MACHINE_SOUND);

    //======= Item Properties =======
    public static final int REGULAR_MAX_STACK_SIZE = 64;
    public static final ItemGroup ORE_OVERHAUL_ITEM_GROUP = new ItemGroup(OreOverhaul.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(InitBlock.COPPER_BLOCK.get());
        }
    };
    // Create the Item Properties for Regular Items (Ingots, etc)
    public static final Item.Properties REGULAR_ITEM_PROPERTIES = new Item.Properties()
                                                                    .maxStackSize(REGULAR_MAX_STACK_SIZE)
                                                                    .group(ORE_OVERHAUL_ITEM_GROUP);

    //======= Alloy Combinations =======
    // These are broken up into floats that add up to 1
    public static final Hashtable<String, Double> bronze =
            new Hashtable<String, Double>() {{put("copper", 0.75);put("tin", 0.25);}};

    //======= Ore List =======
    // These are only here for me to write down ideas. Sometimes it's easier to just add them to an array versus
    // Opening up a completely different document :P
    public static final String[] regularOres = {"copper", "tin", "silver", "aluminum", "cadmium", "chromium",
                                                "lead", "nickel", "lithium", "magnesium", "quartz", "tungsten",
                                                "titanium"};

    public static final String[] nonMetalAlloyComponents = {"carbon dust", "silicon powder"};

    public void init() {

    }

}
