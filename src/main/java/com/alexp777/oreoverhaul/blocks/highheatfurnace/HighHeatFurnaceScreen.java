package com.alexp777.oreoverhaul.blocks.highheatfurnace;

import com.alexp777.oreoverhaul.OreOverhaul;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HighHeatFurnaceScreen extends ContainerScreen<HighHeatFurnaceContainer> {

    private ResourceLocation HIGH_HEAT_FURNACE_GUI =
            new ResourceLocation(OreOverhaul.MOD_ID, "textures/gui/high_heat_furnace_gui.png");

    public HighHeatFurnaceScreen(HighHeatFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 180;
        this.ySize = 152;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, this.title.getUnformattedComponentText(), 8.0f, 8.0f, 0x404040);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getUnformattedComponentText(),
                8.0f, 70.0f, 0x404040);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(HIGH_HEAT_FURNACE_GUI);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
