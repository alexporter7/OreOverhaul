package com.alexp777.oreoverhaul.blocks.highheatfurnace;

import net.minecraft.nbt.CompoundNBT;

public class HighHeatFurnaceStateData {

    public static final String IS_LIT_TAG = "lit";
    public static final String TEMPERATURE_TAG = "temperature";

    public boolean lit;
    public int temperature;

    public void putIntoNBT(CompoundNBT nbtTagCompound) {
        nbtTagCompound.putBoolean(IS_LIT_TAG, lit);
        nbtTagCompound.putInt(TEMPERATURE_TAG, temperature);
    }

    public void readFromNBT(CompoundNBT nbtTagCompound) {
        lit = nbtTagCompound.getBoolean(IS_LIT_TAG);
        temperature = nbtTagCompound.getInt(TEMPERATURE_TAG);
    }

}
