package com.alexp777.oreoverhaul.data;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.items.InitItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(final DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(final Consumer<IFinishedRecipe> consumer) {

        ShapedRecipeBuilder.shapedRecipe(InitBlock.COPPER_BLOCK.get())
                .patternLine("CCC")
                .patternLine("CCC")
                .patternLine("CCC")
                .key('C', InitItem.COPPER_INGOT.get())
                .addCriterion("has_copper_ingot", hasItem(InitItem.COPPER_INGOT.get()))
                .build(consumer, new ResourceLocation(OreOverhaul.MOD_ID, "copper_block"));

    }

    @Override
    public String getName() {
        return "Ore Overhaul Recipes";
    }
}
