package com.alexp777.oreoverhaul.blocks.orecrusher;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class OreCrusherScreen extends ContainerScreen<OreCrusherContainer> {

    private ResourceLocation ORE_CRUSHER_GUI = new ResourceLocation(OreOverhaul.MOD_ID, "textures/gui/ore_crusher_gui.png");

    public OreCrusherScreen(OreCrusherContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(ORE_CRUSHER_GUI);
        int relativeX = (this.width - this.xSize) / 2;
        int relativeY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relativeX, relativeY, 0, 0, this.xSize, this.ySize);
    }



}
