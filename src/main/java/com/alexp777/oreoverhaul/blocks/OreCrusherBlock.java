package com.alexp777.oreoverhaul.blocks;

import com.alexp777.oreoverhaul.setup.ModSetup;
import com.alexp777.oreoverhaul.setup.ModTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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

    //Set the Facing Block State depending on where the entity is facing
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
//        if (entity != null) {
//            worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, entity)), 2);
//        }
//    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    //Return the direction of where the entity is facing
//    public static Direction getFacingFromEntity(BlockPos placedBlock, LivingEntity entity) {
//        return Direction.getFacingFromVector(
//                (float)(entity.getPosX() - placedBlock.getX()),
//                (float)(entity.getPosY() - placedBlock.getY()),
//                (float)(entity.getPosZ() - placedBlock.getZ())
//        );
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }
}
