package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.blocks.highheatfurnace.HighHeatFurnaceContainer;
import com.alexp777.oreoverhaul.blocks.orecrusher.OreCrusherContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitContainer {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, OreOverhaul.MOD_ID);

    public static final RegistryObject<ContainerType<OreCrusherContainer>> ORE_CRUSHER =
            CONTAINER_TYPE.register(ModSetup.ORE_CRUSHER_REGISTRY_NAME,
                    () -> IForgeContainerType.create(OreCrusherContainer::createContainerClientSide));

    public static final RegistryObject<ContainerType<HighHeatFurnaceContainer>> HIGH_HEAT_FURNACE =
            CONTAINER_TYPE.register(ModSetup.HIGH_HEAT_FURNACE_CONTAINER_REGISTRY_NAME,
                    () -> IForgeContainerType.create(HighHeatFurnaceContainer::new));

}
