package flyingperson.ecaa.proxy;

import flyingperson.ecaa.block.extractor.ContainerExtractor;
import flyingperson.ecaa.block.extractor.ExtractorTileEntity;
import flyingperson.ecaa.block.extractor.GUIExtractor;
import flyingperson.ecaa.block.neutron.ContainerNeutronCollector;
import flyingperson.ecaa.block.neutron.GUINeutronCollector;
import flyingperson.ecaa.block.neutron.NeutronTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof NeutronTileEntity) {
            return new ContainerNeutronCollector(player.inventory, (NeutronTileEntity) te);
        } else if (te instanceof ExtractorTileEntity) {
            return new ContainerExtractor(player.inventory, (ExtractorTileEntity) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof NeutronTileEntity) {
            NeutronTileEntity containerTileEntity = (NeutronTileEntity) te;
            return new GUINeutronCollector(player.inventory, containerTileEntity);
        } else if (te instanceof ExtractorTileEntity) {
            ExtractorTileEntity extractorTileEntity = (ExtractorTileEntity) te;
            return new GUIExtractor(player.inventory, extractorTileEntity);
        }
        return null;
    }
}