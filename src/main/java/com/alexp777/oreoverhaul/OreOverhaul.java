package com.alexp777.oreoverhaul;

import com.alexp777.oreoverhaul.blocks.*;
import com.alexp777.oreoverhaul.items.CopperIngot;
import com.alexp777.oreoverhaul.setup.ClientProxy;
import com.alexp777.oreoverhaul.setup.IProxy;
import com.alexp777.oreoverhaul.setup.ModSetup;
import com.alexp777.oreoverhaul.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oreoverhaul")
public class OreOverhaul {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //Create the proxy for both Server and Client side methods
    //Used to ensure that Client-Only commands aren't accessed from
    //the Server world. Throws an Illegal State Exception
    public static IProxy proxy = DistExecutor.runForDist(
            () -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    //Create the ModSetup Object
    public static ModSetup modSetup = new ModSetup();

    //Declare the Mod ID
    public static final String MOD_ID = "oreoverhaul";

    public OreOverhaul() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup); // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC); // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC); // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff); // Register the doClientStuff method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // That wonderful Pre-Init Code Runs after all blocks and items are registered
        LOGGER.info("Mod currently in Pre-Init phase");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        // Run the init() method in ModSetup and in Client/Server Proxy
        modSetup.init();
        proxy.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo("examplemod", "helloworld", () -> {
//            LOGGER.info("Hello world from the MDK");
//            return "Hello world";
//        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // Runs when Server Starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            LOGGER.info("Registering Blocks");
            blockRegistryEvent.getRegistry().register(new CopperBlock());
            blockRegistryEvent.getRegistry().register(new OreCrusherBlock().setRegistryName("ore_crusher_block"));
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            //Create the variable for the Item Group
            Item.Properties itemProperties = new Item.Properties()
                    .group(modSetup.oreOverhaulItemGroup)
                    .maxStackSize(64);
            //======= Items =======
            LOGGER.info("Registering Items");
            itemRegistryEvent.getRegistry().register(new CopperIngot());
            //======= Block Items =======
            LOGGER.info("Registering Block Items");
            itemRegistryEvent.getRegistry().register(
                    new BlockItem(ModBlocks.COPPER_BLOCK, itemProperties).setRegistryName(MOD_ID, ModSetup.COPPER_BLOCK_REGISTRY_NAME));
            itemRegistryEvent.getRegistry().register(
                    new BlockItem(ModBlocks.ORE_CRUSHER_BLOCK, itemProperties).setRegistryName(MOD_ID, ModSetup.ORE_CRUSHER_REGISTRY_NAME));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> tileEntityRegistryEvent) {
            //Register the Ore Crusher Tile Entity
            tileEntityRegistryEvent.getRegistry().register(TileEntityType.Builder
                    .create(OreCrusherTileEntity::new, ModBlocks.ORE_CRUSHER_BLOCK)
                    .build(null).setRegistryName(MOD_ID,ModSetup.ORE_CRUSHER_REGISTRY_NAME));

        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> containerRegistryEvent) {
            // Register the Ore Crusher Container
//            containerRegistryEvent.getRegistry().register(IForgeContainerType.create(
//                    ((windowId, inv, data) -> {
//                        BlockPos blockPos = data.readBlockPos();
//                        return new OreCrusherContainer(
//                                windowId, OreOverhaul.proxy.getClientWorld(), blockPos, inv, OreOverhaul.proxy.getClientPlayer());
//                    })
//            ).setRegistryName(ModSetup.ORE_CRUSHER_REGISTRY_NAME));
            containerRegistryEvent.getRegistry().register(
                    IForgeContainerType.create(OreCrusherContainer::createContainerClientSide)
                            .setRegistryName(MOD_ID, ModSetup.ORE_CRUSHER_REGISTRY_NAME));
        }

    }

}
