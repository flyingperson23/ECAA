package flyingperson.ecaa.block.neutron;

import codechicken.lib.util.ItemUtils;
import flyingperson.ecaa.ECAA;
import flyingperson.ecaa.top.TOPInfoProvider;
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

import javax.annotation.Nullable;
import java.util.List;

public class NeutronBlock extends BlockContainer implements TOPInfoProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);


    public NeutronBlock() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(10);
        setResistance(9);
        setUnlocalizedName("neutron");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(CreativeTabs.REDSTONE);
        setRegistryName("neutron");
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
        if (!(te instanceof NeutronTileEntity)) {
            return false;
        }
        player.openGui(ECAA.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new NeutronTileEntity();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        NeutronTileEntity collector = (NeutronTileEntity) world.getTileEntity(pos);

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
        if (te instanceof NeutronTileEntity) {
            NeutronTileEntity neutron = (NeutronTileEntity) te;
            if (neutron.getReactor() == null) {
                probeInfo.horizontal()
                        .text(TextFormatting.GRAY+"No active fusion reactor found");
            } else {
                probeInfo.horizontal()
                        .text(TextFormatting.GRAY+"Active fusion reactor found at x: "+neutron.getReactor().getX()+", y: "+neutron.getReactor().getY()+", z: "+neutron.getReactor().getZ());
            }
            int percentage = (neutron.getProgress() * 100) / neutron.PRODUCTION_TICKS;
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
        tooltip.add("Collects piles of neutrons");
        tooltip.add("Does not require power");
        tooltip.add("Must be within 7 blocks of an active fusion reactor");
    }




}