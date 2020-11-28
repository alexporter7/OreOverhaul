package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.recipes.HighHeatFurnaceRecipe;
import com.alexp777.oreoverhaul.recipes.HighHeatFurnaceRecipeSerializer;
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

    public static final IRecipeSerializer<HighHeatFurnaceRecipe> HIGH_HEAT_FURNACE_RECIPE_I_RECIPE_SERIALIZER =
            new HighHeatFurnaceRecipeSerializer();
    public static final IRecipeType<IHighHeatFurnaceRecipe> HIGH_HEAT_FURNACE_TYPE =
            registerType(IHighHeatFurnaceRecipe.RECIPE_TYPE_ID);

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OreOverhaul.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> HIGH_HEAT_FURNACE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZER.register(ModSetup.HIGH_HEAT_FURNACE_SERIALIZER_REGISTRY_NAME,
                    () -> HIGH_HEAT_FURNACE_RECIPE_I_RECIPE_SERIALIZER);


    private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
    }

    private static class RecipeType<T extends IRecipe<?>> implements  IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

}
