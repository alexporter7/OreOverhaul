package com.alexp777.oreoverhaul.data.client;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.InitBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, OreOverhaul.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //======= Generate Block Files =======
        simpleBlock(InitBlock.COPPER_BLOCK.get());

    }

}
