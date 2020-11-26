package com.alexp777.oreoverhaul.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.alexp777.oreoverhaul.blocks.ModBlocks.ORE_CRUSHER_TILE_ENTITY;

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

    public OreCrusherTileEntity() {
        super(ORE_CRUSHER_TILE_ENTITY);

        jawZoneContents = OreCrusherZoneContents.createForTileEntity(
                JAW_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        inputZoneContents = OreCrusherZoneContents.createForTileEntity(
                INPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        outputZoneContents = OreCrusherZoneContents.createForTileEntity(
                OUTPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
    }

    @Override
    public void tick() {

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

    //Read NBT Tags
    @Override
    public void read(BlockState state, CompoundNBT tag) {
        //Read the inventory NBT Data
        CompoundNBT inventoryTag = tag.getCompound(INVENTORY_TAG);
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(inventoryTag));
        super.read(state, tag);
    }

    //Write NBT Tags
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        //Write our inventory NBT Data
        handler.ifPresent(h -> {
                CompoundNBT compoundInventory = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
                tag.put(INVENTORY_TAG, compoundInventory);
        });
        return super.write(tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new OreCrusherContainer(windowId, playerInventory,
                jawZoneContents, inputZoneContents, outputZoneContents, oreCrusherStateData);
    }
}
