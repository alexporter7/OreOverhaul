package com.alexp777.oreoverhaul.blocks.orecrusher;

import com.alexp777.oreoverhaul.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class OreCrusherBlock extends ContainerBlock {

    public OreCrusherBlock() {
        //Create Block Properties
        super(Properties
                .create(ModSetup.MACHINE_MATERIAL)
                .sound(ModSetup.MACHINE_SOUND)
                .hardnessAndResistance(ModSetup.MACHINE_HARDNESS)
        );
        // Set the default Block State
        BlockState defaultBlockState = this.stateContainer.getBaseState();
        this.setDefaultState(defaultBlockState);
    }

    //Called when Client places down Block to get Tile Entity
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return createNewTileEntity(world);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new OreCrusherTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        // If the world is a client return a success action
        if(world.isRemote()) {
            return ActionResultType.SUCCESS;
        }
        INamedContainerProvider namedContainerProvider = getContainer(state, world, pos);
        System.out.println(namedContainerProvider);
        if(namedContainerProvider != null) {
            if (!(player instanceof ServerPlayerEntity)) {
                return ActionResultType.FAIL;
            }
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {}));
        }
        return ActionResultType.SUCCESS;

    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
