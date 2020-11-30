package com.alexp777.oreoverhaul.recipes;

import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.setup.InitRecipeSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HighHeatFurnaceRecipe implements IRecipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient original;
    private final ItemStack result;
    private final int baseTime;

    public HighHeatFurnaceRecipe(ResourceLocation id, String group, Ingredient original, ItemStack result, int baseTime) {
        this.id = id;
        this.group = group;
        this.original = original;
        this.result = result;
        this.baseTime = baseTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return this.original.test(inv.getStackInSlot(1));
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getBaseTime() {
        return baseTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredientNonNullList = NonNullList.create();
        ingredientNonNullList.add(this.original);
        return ingredientNonNullList;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(RecipeWrapper inv) {
        return NonNullList.create();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(InitBlock.HIGH_HEAT_FURNACE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return InitRecipeSerializer.HIGH_HEAT_FURNACE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return InitRecipeSerializer.Type.HIGH_HEAT_FURNACE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
                                    implements IRecipeSerializer<HighHeatFurnaceRecipe> {

//        @Override
//        public HighHeatFurnaceRecipe read(ResourceLocation recipeId, JsonObject json) {
//            String group = JSONUtils.getString(json, "group", "");
//            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(JSONUtils.getJsonObject(json, "ingredient"));
//            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
//            int time = JSONUtils.getInt(json, "freezetime", 0);
//            return new HighHeatFurnaceRecipe(recipeId, group, ingredient, result, time);
//        }

        @Override
        @Nonnull
        public HighHeatFurnaceRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            // Get the Group
            final String group = JSONUtils.getString(json, "group", "");
            // Get the Input Element
            final JsonElement inputElement =
                    JSONUtils.isJsonArray(json, "input")
                            ? JSONUtils.getJsonArray(json, "input") // If it's an array
                            : JSONUtils.getJsonObject(json, "input"); // If it's NOT an array
            final Ingredient inputIngredient = Ingredient.deserialize(inputElement);
            // Get the Output Element
            final ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            // Get the Time required
            final int time = JSONUtils.getInt(json, "base_time");

            return new HighHeatFurnaceRecipe(recipeId, group, inputIngredient, output, time);

        }

//        @Override
//        public HighHeatFurnaceRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
//            String group = buffer.readString(32767);
//            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(buffer);
//            ItemStack result = buffer.readItemStack();
//            int time = buffer.readVarInt();
//            return new HighHeatFurnaceRecipe(recipeId, group, ingredient, result, time);
//        }

        @Override
        @Nullable
        public HighHeatFurnaceRecipe read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
            final String group = buffer.readString(32767);
            final Ingredient input = Ingredient.read(buffer);
            final ItemStack output = buffer.readItemStack();
            final int time = buffer.readVarInt();

            return new HighHeatFurnaceRecipe(recipeId, group, input, output, time);
        }

//        @Override
//        public void write(PacketBuffer buffer, HighHeatFurnaceRecipe recipe) {
//            buffer.writeString(recipe.group);
//            recipe.original.write(buffer);
//            buffer.writeItemStack(recipe.result);
//            buffer.writeInt(recipe.baseTime);
//        }

        @Override
        public void write(PacketBuffer buffer, HighHeatFurnaceRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.original.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeInt(recipe.baseTime);
        }

    }
}