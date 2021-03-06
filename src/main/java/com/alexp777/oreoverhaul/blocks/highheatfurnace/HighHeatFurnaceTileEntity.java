package com.alexp777.oreoverhaul.blocks.highheatfurnace;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.alexp777.oreoverhaul.recipes.HighHeatFurnaceRecipe;
import com.alexp777.oreoverhaul.setup.InitRecipeSerializer;
import com.alexp777.oreoverhaul.setup.InitTileEntity;
import com.alexp777.oreoverhaul.util.HighHeatFurnaceItemHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class HighHeatFurnaceTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private ITextComponent customName;

    //======= Inventory Handler =======
    private HighHeatFurnaceItemHandler inventory;
    private static final int TOTAL_INVENTORY_SLOT_SIZE = 3;

    //======= State Data =======
    public int burnTime;
    public int cookTime;
    private int cookTimeTotal = 100;
    private int temperature;

    public static final int COOK_TIME_STATE_INDEX = 0;
    public static final int BURN_TIME_STATE_INDEX = 1;
    public static final int COOK_TIME_TOTAL_STATE_INDEX = 2;
    public static final int TEMPERATURE_STATE_INDEX = 3;

    public final IIntArray highHeatFurnaceData = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return HighHeatFurnaceTileEntity.this.cookTime;
                case 1:
                    return HighHeatFurnaceTileEntity.this.burnTime;
                case 2:
                    return HighHeatFurnaceTileEntity.this.cookTimeTotal;
                case 3:
                    return HighHeatFurnaceTileEntity.this.temperature;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    HighHeatFurnaceTileEntity.this.cookTime = value;
                    break;
                case 1:
                    HighHeatFurnaceTileEntity.this.burnTime = value;
                    break;
                case 2:
                    HighHeatFurnaceTileEntity.this.cookTimeTotal = value;
                    break;
                case 3:
                    HighHeatFurnaceTileEntity.this.temperature = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    //======= NBT Tag Variables =======
    public static final String CUSTOM_NAME_KEY = "customName";
    public static final String BURN_TIME_KEY = "burnTime";
    public static final String COOK_TIME_KEY = "cookTime";
    public static final String COOK_TIME_TOTAL_KEY = "cookTimeTotal";
    public static final String TEMPERATURE_KEY = "temperature";

    //======= Slot Information =======
    public static final int CATALYST_ITEM_SLOT = 0;
    public static final int INPUT_ITEM_SLOT = 1;
    public static final int OUTPUT_ITEM_SLOT = 2;

    public HighHeatFurnaceTileEntity() {
        this(InitTileEntity.HIGH_HEAT_FURNACE_TILE_ENTITY.get());
    }

    public HighHeatFurnaceTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.inventory = new HighHeatFurnaceItemHandler(TOTAL_INVENTORY_SLOT_SIZE);
    }

    @Nullable
    @Override
    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
        return new HighHeatFurnaceContainer(windowId, playerInventory, this);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        // Read the custom name if applicable
        if(nbt.contains(CUSTOM_NAME_KEY, Constants.NBT.TAG_STRING)) {
            this.customName = ITextComponent.Serializer.getComponentFromJson(nbt.getString(CUSTOM_NAME_KEY));
        }

        // Read the information of the TileEntity's inventory
        NonNullList<ItemStack> inventoryList = NonNullList.withSize(TOTAL_INVENTORY_SLOT_SIZE, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, inventoryList);
        this.inventory.setNonNullList(inventoryList);

        // Read the other State Data / NBT Data stored
        this.burnTime = nbt.getInt(BURN_TIME_KEY);
        this.cookTime = nbt.getInt(COOK_TIME_KEY);
        this.cookTimeTotal = nbt.getInt(COOK_TIME_TOTAL_KEY);
        this.temperature = nbt.getInt(TEMPERATURE_KEY);

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if(this.customName != null) {
            compound.putString(CUSTOM_NAME_KEY, ITextComponent.Serializer.toJson(this.customName));
        }

        ItemStackHelper.saveAllItems(compound, this.inventory.toNonNullList());
        compound.putInt(BURN_TIME_KEY, this.burnTime);
        compound.putInt(COOK_TIME_KEY, this.cookTime);
        compound.putInt(COOK_TIME_TOTAL_KEY, this.cookTimeTotal);
        compound.putInt(TEMPERATURE_KEY, this.temperature);

        return compound;
    }

    public int getSizeInventory() {
        return this.inventory.getSlots();
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void tick() {

        if( !this.world.isRemote ) {

            boolean dirty = false;

            if(this.isBurning()) {
                --this.burnTime;
                if ( temperature < 400 ) {
                    temperature++;
                }
            } else {
                if( temperature != 0) {
                    temperature--;
                }
            }

            // If the furnace IS NOT Burning, the INPUT and FUEL slot are NOT empty, and there is a VALID recipe
            if ( !this.isBurning()
                    && !this.inventory.getStackInSlot(INPUT_ITEM_SLOT).isEmpty()
                    && !this.inventory.getStackInSlot(CATALYST_ITEM_SLOT).isEmpty()
                    && ( this.getRecipe(this.inventory.getStackInSlot(INPUT_ITEM_SLOT)) != null )) {
                if ( this.isFuel(this.inventory.getStackInSlot(CATALYST_ITEM_SLOT))) {
                    // Set the Block State to be LIT
                    this.world.setBlockState(this.getPos(), this.getBlockState().with(HighHeatFurnaceBlock.LIT, true));
                    // Set the burn time of the furnace to the item's defined burn time
                    this.burnTime = this.inventory.getStackInSlot(CATALYST_ITEM_SLOT).getBurnTime();
                    this.inventory.decrStackSize(CATALYST_ITEM_SLOT, 1);
                    // Mark the tile entity as dirty
                    dirty = true;
                }
            } else if( this.isBurning() && (this.getRecipe(this.inventory.getStackInSlot(INPUT_ITEM_SLOT)) != null )) {
                this.cookTimeTotal = this.getRecipe(this.inventory.getStackInSlot(INPUT_ITEM_SLOT)).getBaseTime();
                if( this.cookTime < this.cookTimeTotal ) {
                    // If it's still cooking, increment the cookTime and mark dirty
                    this.cookTime++;
                    dirty = true;
                } else {
                    // The furnace is done smelting, set the cookTime to 0 and get the Output
                    this.cookTime = 0;
                    ItemStack outputItem = this.getRecipe(this.inventory.getStackInSlot(INPUT_ITEM_SLOT)).getRecipeOutput();
                    // Insert the output item into the OUTPUT slot and decrease the INPUT slot
                    this.inventory.insertItem(OUTPUT_ITEM_SLOT, outputItem.copy(), false);
                    this.inventory.decrStackSize(INPUT_ITEM_SLOT, 1);
                    // Set the BlockState for LIT to false
                    this.world.setBlockState(this.getPos(), this.getBlockState().with(HighHeatFurnaceBlock.LIT, false));
                    // Mark Dirty
                    dirty = true;
                }
            } else if ((this.getRecipe(this.inventory.getStackInSlot(INPUT_ITEM_SLOT)) == null)) {
                if(this.cookTime != 0) {
                    this.cookTime = 0;
                    dirty = true;
                }
            }

            if(dirty) {
                this.markDirty();
                this.world.notifyBlockUpdate(
                        this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
            }

        }

    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
    }

    private boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    private int getBurnTime(ItemStack fuel) {
        if(fuel.isEmpty()) {
            return 0;
        }
        return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel);
    }

    @Nullable
    private HighHeatFurnaceRecipe getRecipe(ItemStack stack) {
        // If the Stack is Blank then there aren't any recipes
        if(stack == null) {
            return null;
        }
        // Find all "High Heat Furnace Type Recipes"
        Set<IRecipe<?>> recipes = findRecipesByType(InitRecipeSerializer.Type.HIGH_HEAT_FURNACE, this.world);

        // Iterate through each recipe
        for(IRecipe<?> iRecipe : recipes) {
            HighHeatFurnaceRecipe recipe = (HighHeatFurnaceRecipe) iRecipe;
            //System.out.println(recipe.getIngredients() + " | " + recipe.getRecipeOutput());
            // Check if there is a match based on the stack input
            if(recipe.matches(new RecipeWrapper(this.inventory), this.world)) {
                return recipe;
            }
        }
        // If no match is returned after the for loop, then there aren't any matches
        return null;
    }

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        if (world != null) {
            return world.getRecipeManager().getRecipes().stream()
                    .filter(recipe -> recipe.getType() == typeIn)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn) {
        World world = Minecraft.getInstance().world;
        if (world != null) {
            return world.getRecipeManager().getRecipes().stream()
                    .filter(recipe -> recipe.getType() == typeIn)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> typeIn, World worldIn) {
        // Create a blank Set for the Item Inputs and fill a Set of Recipes
        Set<ItemStack> inputs = new HashSet<ItemStack>();
        Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);

        for(IRecipe<?> recipe : recipes) {
            // Fill a list of ingredients for each recipe
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            // Loop through ingredients and get the matching stack (Ingredient -> Item Stack)
            // Then add that to the input set
            ingredients.forEach(ingredient -> {
                for(ItemStack stack : ingredient.getMatchingStacks()) {
                    inputs.add(stack);
                }
            });
        }
        // Return our inputs
        return inputs;
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 42;
        return new SUpdateTileEntityPacket(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTagDescribingTileEntityState = pkt.getNbtCompound();
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTagDescribingTileEntityState);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getName();
    }

    public void setCustomName(ITextComponent name) {
        this.customName = name;
    }

    private ITextComponent getDefaultName() {
        //return new TranslationTextComponent(String.format("container.%s.high_heat_furnace", OreOverhaul.MOD_ID));
        return new StringTextComponent("High Heat Furnace");
    }

    public ITextComponent getName() {
        if(customName != null) {
            return customName;
        }
        return this.getDefaultName();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.customName;
    }
}
