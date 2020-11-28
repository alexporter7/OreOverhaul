package com.alexp777.oreoverhaul.recipes;

import com.alexp777.oreoverhaul.setup.InitRecipeSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class HighHeatFurnaceRecipe implements IHighHeatFurnaceRecipe {

    private final ResourceLocation id;
    private Ingredient input;
    private ItemStack output;

    public HighHeatFurnaceRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, this.input);
    }

    @Override
    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        if(this.input.test(inv.getStackInSlot(0))) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return InitRecipeSerializer.HIGH_HEAT_FURNACE_RECIPE_SERIALIZER.get();
    }

}
