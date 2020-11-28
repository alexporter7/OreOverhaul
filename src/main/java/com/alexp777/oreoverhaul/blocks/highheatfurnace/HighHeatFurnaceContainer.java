package com.alexp777.oreoverhaul.blocks.highheatfurnace;

import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.blocks.orecrusher.OreCrusherContainer;
import com.alexp777.oreoverhaul.setup.InitContainer;
import com.alexp777.oreoverhaul.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class HighHeatFurnaceContainer extends Container {

    //======= Get the total count of slots for the custom input slots =======
    public static final int CATALYST_SLOTS_COUNT = 1;
    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 1;
    public static final int HIGH_HEAT_FURNACE_SLOTS_COUNT = CATALYST_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    //======= Player Inventory Variables =======
    public static final int HOTBAR_SLOTS_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COL_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOTS_COUNT = PLAYER_INVENTORY_COL_COUNT * PLAYER_INVENTORY_ROW_COUNT;

    //======= Get the Index for the Player Inventory Counts + Ours =======
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    public static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOTS_COUNT;
    public static final int CATALYST_SLOTS_FIRST_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOTS_COUNT;
    public static final int INPUT_SLOT_FIRST_INDEX = CATALYST_SLOTS_FIRST_INDEX + CATALYST_SLOTS_COUNT;
    public static final int OUTPUT_SLOT_FIRST_INDEX = INPUT_SLOT_FIRST_INDEX + INPUT_SLOTS_COUNT;

    //======= Set the Player Inventory Offset =======
    public static final int PLAYER_INVENTORY_XPOS = 10;
    public static final int PLAYER_INVENTORY_YPOS = 70;

    //======= Create Fields for Server Constructor =======
    private HighHeatFurnaceTileEntity tileEntity;
    private IWorldPosCallable canInteractWithCallable;
    public FunctionalIntReferenceHolder currentSmeltTime;
    public FunctionalIntReferenceHolder currentBurnTime;

    // This is the constructor for the Server
    public HighHeatFurnaceContainer(final int windowId, final PlayerInventory playerInventory,
                                       final HighHeatFurnaceTileEntity highHeatFurnaceTileEntity) {
        super(InitContainer.HIGH_HEAT_FURNACE.get(), windowId);

        this.tileEntity = highHeatFurnaceTileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(highHeatFurnaceTileEntity.getWorld(),
                                                                highHeatFurnaceTileEntity.getPos());

        // ======= State the X and Y spacing for the Slots =======
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_X_POS = 10; // 8
        final int HOTBAR_Y_POS = 128; //183
        // Add the Inventory Slots
        // In the default GUI screen the slots start 10 pixels
        // from the left-hand side and 70 pixels from the top
        //======= Add the Hot Bar =======
        for (int slotNumber = 0; slotNumber < HOTBAR_SLOTS_COUNT; slotNumber++) {
            addSlot(new Slot(playerInventory, slotNumber, HOTBAR_X_POS + SLOT_X_SPACING * slotNumber, HOTBAR_Y_POS));
        }
        //======= Add the Player Inventory =======
        for(int slotRow = 0; slotRow < PLAYER_INVENTORY_ROW_COUNT; slotRow++) {
            for(int slotCol = 0; slotCol < PLAYER_INVENTORY_COL_COUNT; slotCol++) {
                int slotNumber = HOTBAR_SLOTS_COUNT + slotRow * PLAYER_INVENTORY_COL_COUNT + slotCol;
                int xPos = PLAYER_INVENTORY_XPOS + slotCol * SLOT_X_SPACING;
                int yPos = PLAYER_INVENTORY_YPOS + slotRow * SLOT_Y_SPACING;
                this.addSlot(new Slot(playerInventory, slotNumber, xPos, yPos));
            }
        }
        //======= Add the Custom Slots =======
        final int CATALYST_SLOTS_XPOS = 41;
        final int CATALYST_SLOTS_YPOS = 26;

        this.addSlot(new SlotItemHandler(tileEntity.getInventory(), 0, CATALYST_SLOTS_XPOS, CATALYST_SLOTS_YPOS));

        final int INPUT_SLOTS_XPOS = 62;
        final int INPUT_SLOTS_YPOS = 26;

        this.addSlot(new SlotItemHandler(tileEntity.getInventory(), 1, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));

        final int OUTPUT_SLOTS_XPOS = 112;
        final int OUTPUT_SLOTS_YPOS = 26;

        this.addSlot(new SlotItemHandler(tileEntity.getInventory(), 2, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));

//        this.trackInt(currentSmeltTime = new FunctionalIntReferenceHolder(
//                () -> this.tileEntity.cookTime,
//                value -> this.tileEntity.cookTime = value
//        ));

        this.trackIntArray(tileEntity.highHeatFurnaceData);


    }

    // This is the constructor for the Client
    public HighHeatFurnaceContainer(final int windowId, final PlayerInventory playerInventory,
                                    final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static HighHeatFurnaceTileEntity getTileEntity(final PlayerInventory playerInventory,
                                                            final PacketBuffer packetBuffer) {
        Objects.requireNonNull(playerInventory, "playerInventory CANNOT be null");
        Objects.requireNonNull(packetBuffer, "packetBuffer CANNOT be null");

        final TileEntity tileEntityAtPos = playerInventory.player.world.getTileEntity(packetBuffer.readBlockPos());
        if (tileEntityAtPos instanceof HighHeatFurnaceTileEntity) {
            return (HighHeatFurnaceTileEntity) tileEntityAtPos;
        }
        throw new IllegalStateException("Tile entity is of invalid type " + tileEntityAtPos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, InitBlock.HIGH_HEAT_FURNACE.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int sourceSlotIndex) {

        //Get the Source Slot
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if(sourceSlot == null || !sourceSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceItemStack = sourceSlot.getStack();
        ItemStack sourceStackBeforeMerge = sourceItemStack.copy();
        boolean successfulTransfer = false;

        HighHeatFurnaceContainer.SlotZone sourceZone = HighHeatFurnaceContainer.SlotZone.getZoneFromIndex(sourceSlotIndex);

        switch(sourceZone) {
            case INPUT_ZONE:
            case CATALYST_ZONE:
                successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_INVENTORY, sourceItemStack, false);
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                }
                break;

            case OUTPUT_ZONE:
                successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_INVENTORY, sourceItemStack, true);
                }
                if(successfulTransfer) {
                    sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
                }
                break;

            case PLAYER_HOTBAR:
            case PLAYER_INVENTORY:
                //Attempt to place into JAWS ZONE
                successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.CATALYST_ZONE, sourceItemStack, false);
                //If JAWS ZONE fails attempt tp lace into INPUT ZONE
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.INPUT_ZONE, sourceItemStack, false);
                }
                //If INPUT ZONE fails attempt to place in PLAYER INVENTORY or PLAYER HOTBAR
                if(!successfulTransfer) {
                    if (sourceZone == HighHeatFurnaceContainer.SlotZone.PLAYER_HOTBAR) {
                        successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_INVENTORY, sourceItemStack, false);
                    } else {
                        successfulTransfer = mergeInto(HighHeatFurnaceContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Unexpected Source Zone: " + sourceZone);

        }

        if(!successfulTransfer) {
            return ItemStack.EMPTY;
        }

        // If the source stack is empty (which means the stack moved) set the Slot Contents to Empty
        if (sourceItemStack.isEmpty()) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        //If the source stack is the exact same then something messed up
        if (sourceItemStack.getCount() == sourceStackBeforeMerge.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(playerIn, sourceItemStack);
        return sourceStackBeforeMerge;

    }

    private boolean mergeInto(HighHeatFurnaceContainer.SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.lastIndexPlus1, fillFromEnd);
    }

    private enum SlotZone {

        CATALYST_ZONE(CATALYST_SLOTS_FIRST_INDEX, CATALYST_SLOTS_COUNT),
        INPUT_ZONE(INPUT_SLOT_FIRST_INDEX, INPUT_SLOTS_COUNT),
        OUTPUT_ZONE(OUTPUT_SLOT_FIRST_INDEX, OUTPUT_SLOTS_COUNT),
        PLAYER_INVENTORY(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOTS_COUNT),
        PLAYER_HOTBAR(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOTS_COUNT);

        public final int firstIndex;
        public final int slotCount;
        public final int lastIndexPlus1;

        SlotZone(int firstIndex, int numberOfSlots) {
            this.firstIndex = firstIndex;
            this.slotCount = numberOfSlots;
            this.lastIndexPlus1 = firstIndex + numberOfSlots;
        }

        public static HighHeatFurnaceContainer.SlotZone getZoneFromIndex(int slotIndex) {
            for (HighHeatFurnaceContainer.SlotZone slotZone : HighHeatFurnaceContainer.SlotZone.values()) {
                if (slotIndex >= slotZone.firstIndex && slotIndex < slotZone.lastIndexPlus1) {
                    return slotZone;
                }
            }
            throw new IndexOutOfBoundsException("Unexpected Slot Index");
        }

    }
}
