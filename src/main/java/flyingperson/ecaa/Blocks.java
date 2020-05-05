package flyingperson.ecaa;

import flyingperson.ecaa.block.accelerator.t1.Acceleratort1Block;
import flyingperson.ecaa.block.accelerator.t2.Acceleratort2Block;
import flyingperson.ecaa.block.accelerator.t3.Acceleratort3Block;
import flyingperson.ecaa.block.accelerator.t4.Acceleratort4Block;
import flyingperson.ecaa.block.accelerator.t5.Acceleratort5Block;
import flyingperson.ecaa.block.extractor.ExtractorBlock;
import flyingperson.ecaa.block.gravity.t1.GravityBlockt1;
import flyingperson.ecaa.block.gravity.t2.GravityBlockt2;
import flyingperson.ecaa.block.gravity.t3.GravityBlockt3;
import flyingperson.ecaa.block.gravity.t4.GravityBlockt4;
import flyingperson.ecaa.block.gravity.t5.GravityBlockt5;
import flyingperson.ecaa.block.neutron.NeutronBlock;
import flyingperson.ecaa.wireless.WirelessCharger;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Blocks {



    @GameRegistry.ObjectHolder("ecaa:acceleratort1")
    public static Acceleratort1Block acceleratort1;

    @GameRegistry.ObjectHolder("ecaa:acceleratort2")
    public static Acceleratort2Block acceleratort2;

    @GameRegistry.ObjectHolder("ecaa:acceleratort3")
    public static Acceleratort3Block acceleratort3;

    @GameRegistry.ObjectHolder("ecaa:acceleratort4")
    public static Acceleratort4Block acceleratort4;

    @GameRegistry.ObjectHolder("ecaa:acceleratort5")
    public static Acceleratort5Block acceleratort5;

    @GameRegistry.ObjectHolder("ecaa:charger")
    public static WirelessCharger charger;


    @GameRegistry.ObjectHolder("ecaa:gravityt1")
    public static GravityBlockt1 gravityt1;

    @GameRegistry.ObjectHolder("ecaa:gravityt2")
    public static GravityBlockt2 gravityt2;

    @GameRegistry.ObjectHolder("ecaa:gravityt3")
    public static GravityBlockt3 gravityt3;

    @GameRegistry.ObjectHolder("ecaa:gravityt4")
    public static GravityBlockt4 gravityt4;

    @GameRegistry.ObjectHolder("ecaa:gravityt5")
    public static GravityBlockt5 gravityt5;

    @GameRegistry.ObjectHolder("ecaa:extractor")
    public static ExtractorBlock extractor;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        acceleratort1.initModel();
        acceleratort2.initModel();
        acceleratort3.initModel();
        acceleratort4.initModel();
        acceleratort5.initModel();
        charger.initModel();

        gravityt1.initModel();
        gravityt2.initModel();
        gravityt3.initModel();
        gravityt4.initModel();
        gravityt5.initModel();

        extractor.initModel();
    }

}
