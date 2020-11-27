package com.alexp777.oreoverhaul.blocks.orecrusher;

import com.alexp777.oreoverhaul.setup.InitTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OreCrusherTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private OreCrusherZoneContents jawZoneContents;
    private OreCrusherZoneContents inputZoneContents;
    private OreCrusherZoneContents outputZoneContents;

    public static final int JAW_SLOTS_COUNT = 1;
    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 1;

    private final OreCrusherStateData oreCrusherStateData = new OreCrusherStateData();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler).cast();

    private static final String INVENTORY_TAG = "inv";
    private static final String JAWS_ZONE_TAG = "jawSlot";
    private static final String INPUT_ZONE_TAG = "inputSlot";
    private static final String OUTPUT_ZONE_TAG = "outputSlot";

    public OreCrusherTileEntity() {
        super(InitTileEntity.ORE_CRUSHER_TILE_ENTITY.get());

        jawZoneContents = OreCrusherZoneContents.createForTileEntity(
                JAW_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        inputZoneContents = OreCrusherZoneContents.createForTileEntity(
                INPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        outputZoneContents = OreCrusherZoneContents.createForTileEntity(
                OUTPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
    }

    @Override
    public void tick() {
        if(world.isRemote()) {
            return;
        }

        markDirty();
    }

    public boolean canPlayerAccessInventory(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
            final double X_CENTRE_OFFSET = 0.5;
            final double Y_CENTRE_OFFSET = 0.5;
            final double Z_CENTRE_OFFSET = 0.5;
            final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
            return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    //Get the Capability Handler
    private IItemHandler createHandler() {
        return new ItemStackHandler(3);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        oreCrusherStateData.readFromNBT(tag);

        CompoundNBT inventoryNBT = tag.getCompound(JAWS_ZONE_TAG);
        jawZoneContents.deserializeNBT(inventoryNBT);;

        inventoryNBT = tag.getCompound(INPUT_ZONE_TAG);
        inputZoneContents.deserializeNBT(inventoryNBT);

        inventoryNBT = tag.getCompound(OUTPUT_ZONE_TAG);
        outputZoneContents.deserializeNBT(inventoryNBT);

        if (jawZoneContents.getSizeInventory() != JAW_SLOTS_COUNT
                || inputZoneContents.getSizeInventory() != INPUT_SLOTS_COUNT
                || outputZoneContents.getSizeInventory() != OUTPUT_SLOTS_COUNT) {
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected");
        }

    }

    //Write NBT Tags
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        //Write our inventory NBT Data
        super.write(tag);

        oreCrusherStateData.putIntoNBT(tag);
        tag.put(JAWS_ZONE_TAG, jawZoneContents.serializeNBT());
        tag.put(INPUT_ZONE_TAG, inputZoneContents.serializeNBT());
        tag.put(OUTPUT_ZONE_TAG, outputZoneContents.serializeNBT());

        return tag;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 42;
        return new SUpdateTileEntityPacket(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTagDescribingTileEntityState = pkt.getNbtCompound();
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTagDescribingTileEntityState);
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag) {
        read(blockState, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Ore Crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new OreCrusherContainer(windowId, playerInventory,
                jawZoneContents, inputZoneContents, outputZoneContents, oreCrusherStateData);
    }
}
