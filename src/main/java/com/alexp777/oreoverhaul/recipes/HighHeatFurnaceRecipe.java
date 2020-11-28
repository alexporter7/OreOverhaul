package com.alexp777.oreoverhaul.recipes;

import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.setup.InitRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class HighHeatFurnaceRecipe implements IRecipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String group;
    private final NBTIngredient original;
    //private final Ingredient original;
    private final ItemStack result;
    private final int freezeTime;

    public HighHeatFurnaceRecipe(ResourceLocation id, String group, NBTIngredient original, ItemStack result, int freezeTime) {
        this.id = id;
        this.group = group;
        this.original = original;
        this.result = result;
        this.freezeTime = freezeTime;
    }

//    public HighHeatFurnaceRecipe(ResourceLocation id, String group, Ingredient original, ItemStack result, int freezeTime) {
//        this.id = id;
//        this.group = group;
//        this.original = original;
//        this.result = result;
//        this.freezeTime = freezeTime;
//    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return this.original.test(inv.getStackInSlot(0));
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

    public int getFreezeTime() {
        return freezeTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, this.original);
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

        @Override
        public HighHeatFurnaceRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
            int time = JSONUtils.getInt(json, "freezetime", 0);
            return new HighHeatFurnaceRecipe(recipeId, group, ingredient, result, time);
        }

        @Override
        public HighHeatFurnaceRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(buffer);
            ItemStack result = buffer.readItemStack();
            int time = buffer.readVarInt();
            return new HighHeatFurnaceRecipe(recipeId, group, ingredient, result, time);
        }

        @Override
        public void write(PacketBuffer buffer, HighHeatFurnaceRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.original.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeInt(recipe.freezeTime);
        }

    }
}