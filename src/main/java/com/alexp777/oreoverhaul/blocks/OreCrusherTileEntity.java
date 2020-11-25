package com.alexp777.oreoverhaul.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.alexp777.oreoverhaul.blocks.ModBlocks.ORE_CRUSHER_TILE_ENTITY;

public class OreCrusherTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler handler;

    private static final String INVENTORY_TAG = "inv";

    public OreCrusherTileEntity() {
        super(ORE_CRUSHER_TILE_ENTITY);
    }

    @Override
    public void tick() {

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) getHandler());
        }
        return super.getCapability(cap, side);
    }

    //Get the Capability Handler
    private ItemStackHandler getHandler() {
        if (handler == null) {
            handler = new ItemStackHandler(1);
        }
        return handler;
    }

    //Read NBT Tags
    @Override
    public void read(BlockState state, CompoundNBT tag) {
        CompoundNBT inventoryTag = tag.getCompound(INVENTORY_TAG);
        getHandler().deserializeNBT(inventoryTag);
        super.read(state, tag);
    }

    //Write NBT Tags
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT compoundInventory = getHandler().serializeNBT();
        tag.put(INVENTORY_TAG, compoundInventory);
        return super.write(tag);
    }

}
