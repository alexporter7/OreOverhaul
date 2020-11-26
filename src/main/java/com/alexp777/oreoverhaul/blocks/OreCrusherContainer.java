package com.alexp777.oreoverhaul.blocks;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.setup.ClientProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static com.alexp777.oreoverhaul.blocks.ModBlocks.ORE_CRUSHER_CONTAINER;

public class OreCrusherContainer extends Container {

    //======= Get the total count of slots for the custom input slots =======
    public static final int JAWS_SLOTS_COUNT = 1;
    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 1;
    public static final int ORE_CRUSHER_SLOTS_COUNT = JAWS_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    //======= Iterate through the rest of the Slots for the Enum =======
    public static final int HOTBAR_SLOTS_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COL_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOTS_COUNT = PLAYER_INVENTORY_COL_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOTS_COUNT = HOTBAR_SLOTS_COUNT + PLAYER_INVENTORY_SLOTS_COUNT;

    //======= Get the Index for the Player Inventory Counts + Ours =======
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    public static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOTS_COUNT;
    public static final int JAWS_SLOT_FIRST_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOTS_COUNT;
    public static final int INPUT_SLOT_FIRST_INDEX = JAWS_SLOT_FIRST_INDEX + JAWS_SLOTS_COUNT;
    public static final int OUTPUT_SLOT_FIRST_INDEX = INPUT_SLOT_FIRST_INDEX + INPUT_SLOTS_COUNT;

    //======= Set the Player Inventory Offset =======
    public static final int PLAYER_INVENTORY_XPOS = 10;
    public static final int PLAYER_INVENTORY_YPOS = 70;

    //======= Set the Class Variables for Contents && State Data =======
    private final OreCrusherZoneContents jawZoneContents;
    private final OreCrusherZoneContents inputZoneContents;
    private final OreCrusherZoneContents outputZoneContents;
    private final OreCrusherStateData oreCrusherStateData;


    public static OreCrusherContainer createContainerClientSide(int windowId,
                                                                PlayerInventory playerInventory,
                                                                PacketBuffer extraData) {
        
        OreCrusherZoneContents jawZoneContents = OreCrusherZoneContents.createForClientSideContainer(JAWS_SLOTS_COUNT);
        OreCrusherZoneContents inputZoneContents = OreCrusherZoneContents.createForClientSideContainer(INPUT_SLOTS_COUNT);
        OreCrusherZoneContents outputZoneContents = OreCrusherZoneContents.createForClientSideContainer(OUTPUT_SLOTS_COUNT);
        OreCrusherStateData oreCrusherStateData = new OreCrusherStateData();

        return new OreCrusherContainer(windowId, playerInventory,
                jawZoneContents, inputZoneContents, outputZoneContents, oreCrusherStateData);
    }

    public static OreCrusherContainer createContainerClientSide(int windowId, PlayerInventory playerInventory,
                                                                OreCrusherZoneContents jawZoneContents,
                                                                OreCrusherZoneContents inputZoneContents,
                                                                OreCrusherZoneContents outputZoneContents,
                                                                OreCrusherStateData oreCrusherStateData) {
        return new OreCrusherContainer(windowId, playerInventory,
                jawZoneContents, inputZoneContents, outputZoneContents, oreCrusherStateData);
    }

    // After Enumerating all of the Slot Counts && Zones it makes the code easier to read
    // The way it's currently setup is:
    //      - Slots 0 - 8: Hotbar Slots
    //      - Slots 9 - 35: Player Inventory Slots
    //      - Slot 36: Jaws Slot
    //      - Slot 37: Input Slot
    //      - Slot 38: Output Slot

    public OreCrusherContainer(int windowId, PlayerInventory playerInventory,
                               OreCrusherZoneContents jawZoneContents,
                               OreCrusherZoneContents inputZoneContents,
                               OreCrusherZoneContents outputZoneContents,
                               OreCrusherStateData oreCrusherStateData) {

        // Call the Super on the Ore Crusher Container
        super(ORE_CRUSHER_CONTAINER, windowId);
        // Make sure that Ore Crusher Container has been initialized
        if(ORE_CRUSHER_CONTAINER == null) {
            throw new IllegalStateException("Must initialize OreCrusherContainer before construction");
        }
        // Set the Class Variables
        this.jawZoneContents = jawZoneContents;
        this.inputZoneContents = inputZoneContents;
        this.outputZoneContents = outputZoneContents;
        this.oreCrusherStateData = oreCrusherStateData;

        // ======= State the X and Y spacing for the Slots
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
                addSlot(new Slot(playerInventory, slotNumber, xPos, yPos));
            }
        }
        //======= Add the Custom Slots =======
        final int JAWS_SLOTS_XPOS = 41;
        final int JAWS_SLOTS_YPOS = 26;

        addSlot(new Slot(playerInventory, JAWS_SLOT_FIRST_INDEX, JAWS_SLOTS_XPOS, JAWS_SLOTS_YPOS));

        final int INPUT_SLOTS_XPOS = 62;
        final int INPUT_SLOTS_YPOS = 26;

        addSlot(new Slot(playerInventory, INPUT_SLOT_FIRST_INDEX, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));

        final int OUTPUT_SLOTS_XPOS = 112;
        final int OUTPUT_SLOTS_YPOS = 26;

        addSlot(new Slot(playerInventory, OUTPUT_SLOT_FIRST_INDEX, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));

    }

    //Create and implement Shift + Click Logic
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

        SlotZone sourceZone = SlotZone.getZoneFromIndex(sourceSlotIndex);

        switch(sourceZone) {
            case INPUT_ZONE:
            case JAWS_ZONE:
                successfulTransfer = mergeInto(SlotZone.PLAYER_INVENTORY, sourceItemStack, false);
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                }
                break;

            case OUTPUT_ZONE:
                successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(SlotZone.PLAYER_INVENTORY, sourceItemStack, true);
                }
                if(successfulTransfer) {
                    sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
                }
                break;

            case PLAYER_HOTBAR:
            case PLAYER_INVENTORY:
                //Attempt to place into JAWS ZONE
                successfulTransfer = mergeInto(SlotZone.JAWS_ZONE, sourceItemStack, false);
                //If JAWS ZONE fails attempt tp lace into INPUT ZONE
                if(!successfulTransfer) {
                    successfulTransfer = mergeInto(SlotZone.INPUT_ZONE, sourceItemStack, false);
                }
                //If INPUT ZONE fails attempt to place in PLAYER INVENTORY or PLAYER HOTBAR
                if(!successfulTransfer) {
                    if (sourceZone == SlotZone.PLAYER_HOTBAR) {
                        successfulTransfer = mergeInto(SlotZone.PLAYER_INVENTORY, sourceItemStack, false);
                    } else {
                        successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
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

    private boolean mergeInto(SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.lastIndexPlus1, fillFromEnd);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return jawZoneContents.isUsableByPlayer(playerIn)
                && inputZoneContents.isUsableByPlayer(playerIn)
                && outputZoneContents.isUsableByPlayer(playerIn);
    }

    private enum SlotZone {

        JAWS_ZONE(JAWS_SLOT_FIRST_INDEX, JAWS_SLOTS_COUNT),
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

        public static SlotZone getZoneFromIndex(int slotIndex) {
            for (SlotZone slotZone : SlotZone.values()) {
                if (slotIndex >= slotZone.firstIndex && slotIndex < slotZone.lastIndexPlus1) {
                    return slotZone;
                }
            }
            throw new IndexOutOfBoundsException("Unexpected Slot Index");
        }

    }
}
