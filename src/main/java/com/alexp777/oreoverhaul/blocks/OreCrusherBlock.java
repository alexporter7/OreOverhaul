package com.alexp777.oreoverhaul.blocks;

import com.alexp777.oreoverhaul.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class OreCrusherBlock extends Block {

    public OreCrusherBlock() {
        //Create Block Properties
        super(Properties
                .create(ModSetup.MACHINE_MATERIAL)
                .sound(ModSetup.MACHINE_SOUND)
                .hardnessAndResistance(ModSetup.MACHINE_HARDNESS)
        );
        //Create Localized Registry name
        setRegistryName("ore_crusher_block");
    }

    //Called when Client places down Block to get Tile Entity
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OreCrusherTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }
}
