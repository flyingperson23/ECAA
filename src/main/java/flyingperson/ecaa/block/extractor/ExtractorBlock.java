package flyingperson.ecaa.block.extractor;

import java.util.List;

import javax.annotation.Nullable;

import codechicken.lib.util.ItemUtils;
import flyingperson.ecaa.ECAA;
import flyingperson.ecaa.compat.top.TOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExtractorBlock extends BlockContainer implements TOPInfoProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);


    public ExtractorBlock() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(10);
        setResistance(9);
        setUnlocalizedName("extractor");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(CreativeTabs.REDSTONE);
        setRegistryName("extractor");
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, facing);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = (EnumFacing)state.getValue(FACING);
        int facingbits = facing.getHorizontalIndex();
        return facingbits;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof ExtractorTileEntity)) {
            return false;
        }
        player.openGui(ECAA.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ExtractorTileEntity();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        ExtractorTileEntity collector = (ExtractorTileEntity) world.getTileEntity(pos);

        if (collector != null) {
            ItemUtils.dropInventory(world, pos, collector);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof ExtractorTileEntity) {
            ExtractorTileEntity extractorTileEntity = (ExtractorTileEntity) te;
            probeInfo.horizontal()
                    .text(TextFormatting.GRAY + "Currently Collecting: ");
            probeInfo.horizontal()
                    .item(extractorTileEntity.getCollected())
                    .text(TextFormatting.GRAY + extractorTileEntity.getName2());
            int percentage = (extractorTileEntity.getProgress() * 100) / extractorTileEntity.POWER_REQUIRED;
            probeInfo.horizontal()
                    .progress(percentage, 100, probeInfo.defaultProgressStyle().suffix("%"));
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (worldIn != null) addFlairs(tooltip);
    }

    public void addFlairs(List<String> tooltip) {
        tooltip.add("Attracts atmospheric dusts on Galacticraft planets");
        tooltip.add("Takes 1,000 RF/t or 250 EU/t (Max EV)");
    }




}