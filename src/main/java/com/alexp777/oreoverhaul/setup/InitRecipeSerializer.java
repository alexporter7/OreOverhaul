package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.recipes.HighHeatFurnaceRecipe;
import com.alexp777.oreoverhaul.recipes.IHighHeatFurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitRecipeSerializer {

    //https://github.com/ChampionAsh5357/Custom-Ideas-2/blob/1.15.2-31.1.30/src/main/java/io/github/championash5357/customideas/item/crafting/FreezerRecipe.java
    
   public static class Type {
       public static final IRecipeType<HighHeatFurnaceRecipe> HIGH_HEAT_FURNACE =
               IRecipeType.register(OreOverhaul.MOD_ID + ":high_heat_furnace");
   }

   public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
           DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OreOverhaul.MOD_ID);

   public static final RegistryObject<IRecipeSerializer<HighHeatFurnaceRecipe>> HIGH_HEAT_FURNACE =
           RECIPE_SERIALIZER.register("high_heat_furnace", () -> (new HighHeatFurnaceRecipe.Serializer()));

}
