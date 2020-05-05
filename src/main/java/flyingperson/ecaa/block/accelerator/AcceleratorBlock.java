package flyingperson.ecaa.block.accelerator;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import flyingperson.ecaa.compat.top.TOPInfoProvider;

import java.util.List;

public abstract class AcceleratorBlock extends Block implements ITileEntityProvider, TOPInfoProvider {


    public AcceleratorBlock(String name) {
        super(Material.IRON);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);
        setHarvestLevel("pickaxe", 2);
        setHardness(10);
        setSoundType(SoundType.METAL);
        setResistance(9);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return createNewTileEntity();
    }

    public abstract AcceleratorTileEntity createNewTileEntity();

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }


    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof AcceleratorTileEntity) {
            AcceleratorTileEntity acceleratorTileEntity = (AcceleratorTileEntity) te;
            probeInfo.horizontal()
                    .text(TextFormatting.GRAY + "Radius: " + acceleratorTileEntity.getRange());
            probeInfo.horizontal()
                    .text(TextFormatting.GRAY + "Speed: " + acceleratorTileEntity.getSpeed()+"x");
            String active = acceleratorTileEntity.getActive() ? "True" : "False";
            probeInfo.horizontal()
                    .text(TextFormatting.GRAY + "Active: " + active);
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (worldIn != null) addFlairs(tooltip);
    }



    public void addFlairs(List<String> tooltip) {
        tooltip.add("Radius: "+this.createNewTileEntity().getRange());
        tooltip.add("Speed: "+this.createNewTileEntity().getSpeed()+"x");
        tooltip.add("Needs "+this.createNewTileEntity().usage+"RF/t, or "+this.createNewTileEntity().usage/4+"EU/t");
        tooltip.add("Max Power: "+this.createNewTileEntity().capacity+"RF, or "+this.createNewTileEntity().capacity/4+"EU");
        tooltip.add("Accepts RF or EU (Max EV)");
        tooltip.add("Disabled by redstone");
    }


}
