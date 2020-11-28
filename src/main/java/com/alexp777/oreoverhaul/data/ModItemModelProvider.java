package com.alexp777.oreoverhaul.data;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.setup.ModSetup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public static final String COPPER_BLOCK_LOCATION = String.format("block/%s", ModSetup.COPPER_BLOCK_REGISTRY_NAME);

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, OreOverhaul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //======= Generate Items =======
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        getBuilder("copper_ingot").parent(itemGenerated).texture("layer0", "item/copper_ingot");
        //======= Generate Block Item Files =======
        withExistingParent(ModSetup.COPPER_BLOCK_REGISTRY_NAME, modLoc(COPPER_BLOCK_LOCATION));
    }
}
