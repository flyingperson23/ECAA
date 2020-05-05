package flyingperson.ecaa.proxy;

import flyingperson.ecaa.Blocks;
import flyingperson.ecaa.ECAA;
import flyingperson.ecaa.block.accelerator.t1.Acceleratort1Block;
import flyingperson.ecaa.block.accelerator.t1.Acceleratort1TileEntity;
import flyingperson.ecaa.block.accelerator.t2.Acceleratort2Block;
import flyingperson.ecaa.block.accelerator.t2.Acceleratort2TileEntity;
import flyingperson.ecaa.block.accelerator.t3.Acceleratort3Block;
import flyingperson.ecaa.block.accelerator.t3.Acceleratort3TileEntity;
import flyingperson.ecaa.block.accelerator.t4.Acceleratort4Block;
import flyingperson.ecaa.block.accelerator.t4.Acceleratort4TileEntity;
import flyingperson.ecaa.block.accelerator.t5.Acceleratort5Block;
import flyingperson.ecaa.block.accelerator.t5.Acceleratort5TileEntity;
import flyingperson.ecaa.block.extractor.ExtractorBlock;
import flyingperson.ecaa.block.extractor.ExtractorTileEntity;
import flyingperson.ecaa.block.gravity.t1.GravityBlockt1;
import flyingperson.ecaa.block.gravity.t1.GravityTEt1;
import flyingperson.ecaa.block.gravity.t2.GravityBlockt2;
import flyingperson.ecaa.block.gravity.t2.GravityTEt2;
import flyingperson.ecaa.block.gravity.t3.GravityBlockt3;
import flyingperson.ecaa.block.gravity.t3.GravityTEt3;
import flyingperson.ecaa.block.gravity.t4.GravityBlockt4;
import flyingperson.ecaa.block.gravity.t4.GravityTEt4;
import flyingperson.ecaa.block.gravity.t5.GravityBlockt5;
import flyingperson.ecaa.block.gravity.t5.GravityTEt5;
import flyingperson.ecaa.block.neutron.NeutronInit;
import flyingperson.ecaa.compat.top.TOPCompatibility;
import flyingperson.ecaa.item.DustAsteroid;
import flyingperson.ecaa.item.DustMars;
import flyingperson.ecaa.item.DustMoon;
import flyingperson.ecaa.item.DustVenus;
import flyingperson.ecaa.wireless.WirelessCharger;
import flyingperson.ecaa.wireless.WirelessChargerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
        NeutronInit.init();
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ECAA.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new Acceleratort1Block());
        GameRegistry.registerTileEntity(Acceleratort1TileEntity.class, "acceleratort1");

        event.getRegistry().register(new Acceleratort2Block());
        GameRegistry.registerTileEntity(Acceleratort2TileEntity.class, "acceleratort2");

        event.getRegistry().register(new Acceleratort3Block());
        GameRegistry.registerTileEntity(Acceleratort3TileEntity.class, "acceleratort3");

        event.getRegistry().register(new Acceleratort4Block());
        GameRegistry.registerTileEntity(Acceleratort4TileEntity.class, "acceleratort4");

        event.getRegistry().register(new Acceleratort5Block());
        GameRegistry.registerTileEntity(Acceleratort5TileEntity.class, "acceleratort5");

        event.getRegistry().register(new WirelessCharger());
        GameRegistry.registerTileEntity(WirelessChargerTileEntity.class, "charger");

        event.getRegistry().register(new GravityBlockt1());
        GameRegistry.registerTileEntity(GravityTEt1.class, "gravityt1");

        event.getRegistry().register(new GravityBlockt2());
        GameRegistry.registerTileEntity(GravityTEt2.class, "gravityt2");

        event.getRegistry().register(new GravityBlockt3());
        GameRegistry.registerTileEntity(GravityTEt3.class, "gravityt3");

        event.getRegistry().register(new GravityBlockt4());
        GameRegistry.registerTileEntity(GravityTEt4.class, "gravityt4");

        event.getRegistry().register(new GravityBlockt5());
        GameRegistry.registerTileEntity(GravityTEt5.class, "gravityt5");

        event.getRegistry().register(new ExtractorBlock());
        GameRegistry.registerTileEntity(ExtractorTileEntity.class, "extractor");
        
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(Blocks.acceleratort1).setRegistryName(Blocks.acceleratort1.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.acceleratort2).setRegistryName(Blocks.acceleratort2.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.acceleratort3).setRegistryName(Blocks.acceleratort3.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.acceleratort4).setRegistryName(Blocks.acceleratort4.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.acceleratort5).setRegistryName(Blocks.acceleratort5.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.charger).setRegistryName(Blocks.charger.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.gravityt1).setRegistryName(Blocks.gravityt1.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.gravityt2).setRegistryName(Blocks.gravityt2.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.gravityt3).setRegistryName(Blocks.gravityt3.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.gravityt4).setRegistryName(Blocks.gravityt4.getRegistryName()));

        event.getRegistry().register(new ItemBlock(Blocks.gravityt5).setRegistryName(Blocks.gravityt5.getRegistryName()));
        
        event.getRegistry().register(new ItemBlock(Blocks.extractor).setRegistryName(Blocks.extractor.getRegistryName()));

        event.getRegistry().register(new DustAsteroid());
        event.getRegistry().register(new DustMars());
        event.getRegistry().register(new DustMoon());
        event.getRegistry().register(new DustVenus());

    }
}
