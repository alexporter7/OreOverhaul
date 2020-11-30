package com.alexp777.oreoverhaul.blocks.highheatfurnace;

import com.alexp777.oreoverhaul.setup.InitTileEntity;
import com.alexp777.oreoverhaul.setup.ModSetup;
import com.alexp777.oreoverhaul.util.HighHeatFurnaceItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class HighHeatFurnaceBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public HighHeatFurnaceBlock() {
        super(ModSetup.MACHINE_PROPERTIES);
        this.setDefaultState(this.getStateContainer().getBaseState()
                                                        .with(FACING, Direction.NORTH)
                                                        .with(LIT, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return InitTileEntity.HIGH_HEAT_FURNACE_TILE_ENTITY.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(LIT)) {
            return super.getLightValue(state, world, pos);
        }
        return 0;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if(stack.hasDisplayName()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof HighHeatFurnaceTileEntity) {
                ((HighHeatFurnaceTileEntity)tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F,
                        false);
            }

            Direction direction = stateIn.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn != null && !worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof HighHeatFurnaceTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)tileEntity, pos);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // This was causing issues when the block state would update so it
        // needs to be rewritten
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if(tileEntity instanceof HighHeatFurnaceTileEntity) {
//            HighHeatFurnaceTileEntity highHeatFurnaceTileEntity = (HighHeatFurnaceTileEntity)tileEntity;
//            ((HighHeatFurnaceItemHandler)highHeatFurnaceTileEntity.getInventory()).toNonNullList().forEach(item -> {
//                ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), item);
//                worldIn.addEntity(itemEntity);
//            });
//        }
//
        if(state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            worldIn.removeTileEntity(pos);
        }

    }
}
