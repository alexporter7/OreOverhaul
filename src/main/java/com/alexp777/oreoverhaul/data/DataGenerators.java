package com.alexp777.oreoverhaul.data;

import com.alexp777.oreoverhaul.OreOverhaul;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = OreOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    private DataGenerators() { }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));

        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, existingFileHelper));

        generator.addProvider(new ModRecipeProvider(generator));

    }

}
