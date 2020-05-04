package flyingperson.ecaa.block.extractor;


import codechicken.lib.math.MathHelper;
import flyingperson.ecaa.ECAA;
import morph.avaritia.client.gui.GuiMachineBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class GUIExtractor extends GuiMachineBase<ExtractorTileEntity, ContainerExtractor> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation(ECAA.MODID, "textures/gui/extractor.png");

    public GUIExtractor(InventoryPlayer player, ExtractorTileEntity machine) {
        super(new ContainerExtractor(player, machine));
        setBackgroundTexture(GUI_TEX);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = I18n.format("Dust Collector");
        float scaled_progress = scaleF(machineTile.getProgress(), ExtractorTileEntity.POWER_REQUIRED, 100);
        String progress = "Progress: " + MathHelper.round(scaled_progress, 10) + "%";
        String planet = "Planet: "+this.machineTile.getPlanet();

        fontRenderer.drawString(planet, xSize / 2  - fontRenderer.getStringWidth(planet) / 2, 16, 0x404040);
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
        fontRenderer.drawString(progress, xSize / 2 - fontRenderer.getStringWidth(progress) / 2, 60, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground();
    }
}