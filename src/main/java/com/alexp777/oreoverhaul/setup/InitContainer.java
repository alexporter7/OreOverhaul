package com.alexp777.oreoverhaul.setup;

import com.alexp777.oreoverhaul.OreOverhaul;
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

}
