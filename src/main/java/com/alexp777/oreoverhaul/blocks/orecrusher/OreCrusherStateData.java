package com.alexp777.oreoverhaul.blocks.orecrusher;

import net.minecraft.util.IIntArray;
import net.minecraft.nbt.CompoundNBT;

/**
 * Created by TGG on 4/04/2020.
 * This class is used to store some state data for the furnace (eg burn time, smelting time, etc)
 * 1) The Server TileEntity uses it to store the data permanently, including NBT serialisation and deserialization
 * 2) The server container uses it to
 *    a) read/write permanent data back into the TileEntity
 *    b) synchronise the server container data to the client container using the IIntArray interface (via Container::trackIntArray)
 * 3) The client container uses it to store a temporary copy of the data, for rendering / GUI purposes
 * The TileEntity and the client container both use it by poking directly into its member variables.  That's not good
 *   practice but it's easier to understand than the vanilla method which uses an anonymous class/lambda functions
 *
 *  The IIntArray interface collates all the separate member variables into a single array for the purposes of transmitting
 *     from server to client (handled by Vanilla)
 */

public class OreCrusherStateData implements IIntArray {

    public static final int FUEL_SLOTS_COUNT = 1; //Remove this

    //State the amount of indexes that we have for NBT data
    public static final int TOTAL_INDEX_SIZE = 2;

    public static final String PROCESS_TIME_TAG = "processTimeElapsed";
    public static final String PROCESS_TIME_FOR_COMPLETION_TAG = "processTimeForCompletion";

    // Number of Ticks machine has run && the number of ticks required to complete one operation
    public int processTimeElapsed;
    public int processTimeForCompletion;

    /* The initial fuel value of the currently burning fuel in each slot (in ticks of burn duration) */
    //public int [] burnTimeInitialValues = new int[FUEL_SLOTS_COUNT];
    /** The number of burn ticks remaining on the current piece of fuel in each slot */
    //public int [] burnTimeRemaining = new int[FUEL_SLOTS_COUNT];

    // --------- read/write to NBT for permanent storage (on disk, or packet transmission) - used by the TileEntity only

    public void putIntoNBT(CompoundNBT nbtTagCompound) {
        nbtTagCompound.putInt(PROCESS_TIME_TAG, processTimeElapsed);
        nbtTagCompound.putInt(PROCESS_TIME_FOR_COMPLETION_TAG, processTimeForCompletion);
    }

    public void readFromNBT(CompoundNBT nbtTagCompound) {
        processTimeElapsed = nbtTagCompound.getInt(PROCESS_TIME_TAG);
        processTimeForCompletion = nbtTagCompound.getInt(PROCESS_TIME_FOR_COMPLETION_TAG);
    }

    // -------- used by vanilla, not intended for mod code
//  * The ints are mapped (internally) as:
//  * 0 = cookTimeElapsed
//  * 1 = cookTimeForCompletion
//  * 2 .. FUEL_SLOTS_COUNT+1 = burnTimeInitialValues[]
//  * FUEL_SLOTS_COUNT + 2 .. 2*FUEL_SLOTS_COUNT +1 = burnTimeRemaining[]
//  *

    private final int PROCESS_TIME_INDEX = 0;
    private final int PROCESS_TIME_FOR_COMPLETION_INDEX = 1;

    @Override
    public int get(int index) {
        validateIndex(index);
        switch(index) {
            case PROCESS_TIME_INDEX:
                return processTimeElapsed;
            case PROCESS_TIME_FOR_COMPLETION_INDEX:
                return processTimeForCompletion;
            default:
                throw new IllegalArgumentException("Somehow you threw a weird index (" + index + ")");
        }
    }

    @Override
    public void set(int index, int value) {
        validateIndex(index);
        switch(index) {
            case 0:
                processTimeElapsed = value;
                break;
            case 1:
                processTimeForCompletion = value;
                break;
            default:
                throw new IllegalArgumentException("Somehow you threw a weird index (" + index + ")");
        }
    }

    @Override
    public int size() {
        return TOTAL_INDEX_SIZE;
    }

    private void validateIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

}
