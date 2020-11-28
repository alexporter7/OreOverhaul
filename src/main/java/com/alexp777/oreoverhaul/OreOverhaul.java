package com.alexp777.oreoverhaul;

import com.alexp777.oreoverhaul.blocks.InitBlock;
import com.alexp777.oreoverhaul.blocks.highheatfurnace.HighHeatFurnaceScreen;
import com.alexp777.oreoverhaul.blocks.orecrusher.OreCrusherScreen;
import com.alexp777.oreoverhaul.data.DataGenerators;
import com.alexp777.oreoverhaul.items.InitItem;
import com.alexp777.oreoverhaul.setup.InitContainer;
import com.alexp777.oreoverhaul.setup.InitRecipeSerializer;
import com.alexp777.oreoverhaul.setup.ModSetup;
import com.alexp777.oreoverhaul.setup.InitTileEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oreoverhaul")
@Mod.EventBusSubscriber(modid = OreOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreOverhaul {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //Declare the Mod ID
    public static final String MOD_ID = "oreoverhaul";

    //Create a static reference to the EventBus for Registration Events
    public static IEventBus MOD_EVENT_BUS;

    public static OreOverhaul instance;

    public OreOverhaul() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.addListener(this::setup);
        MOD_EVENT_BUS.addListener(this::doClientStuff);
        //MOD_EVENT_BUS.addListener(RegistryEvents::onItemsRegistry);
        MOD_EVENT_BUS.addListener(DataGenerators::gatherData);

        InitItem.ITEMS.register(MOD_EVENT_BUS);
        InitBlock.BLOCKS.register(MOD_EVENT_BUS);
        InitTileEntity.TILE_ENTITY_TYPE.register(MOD_EVENT_BUS);
        InitContainer.CONTAINER_TYPE.register(MOD_EVENT_BUS);
        InitRecipeSerializer.RECIPE_SERIALIZER.register(MOD_EVENT_BUS);

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
       //Called after Registry has been completed
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        ScreenManager.registerFactory(InitContainer.ORE_CRUSHER.get(), OreCrusherScreen::new);
        ScreenManager.registerFactory(InitContainer.HIGH_HEAT_FURNACE.get(), HighHeatFurnaceScreen::new);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("HELLO from server starting"); // Runs when Server Starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            final IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();
            InitBlock.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(
                    block -> {
                        final BlockItem blockItem = new BlockItem(block, ModSetup.REGULAR_ITEM_PROPERTIES);
                        blockItem.setRegistryName(block.getRegistryName());
                        registry.register(blockItem);
                    }
            );
            LOGGER.debug("Registered Block Items");
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> tileEntityRegistryEvent) {
            //Old, Register Moved to StartCommon in oreoverhaul.blocks
            //Register the Ore Crusher Tile Entity
//            tileEntityRegistryEvent.getRegistry().register(TileEntityType.Builder
//                    .create(OreCrusherTileEntity::new, ModBlocks.ORE_CRUSHER_BLOCK)
//                    .build(null).setRegistryName(MOD_ID,ModSetup.ORE_CRUSHER_REGISTRY_NAME));

        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> containerRegistryEvent) {
            //Old Register Moved to StartupCommon in oreoverhaul.blocks
//            containerRegistryEvent.getRegistry().register(
//                    IForgeContainerType.create(OreCrusherContainer::createContainerClientSide)
//                            .setRegistryName(MOD_ID, ModSetup.ORE_CRUSHER_REGISTRY_NAME));
        }

    }

}
