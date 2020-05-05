package flyingperson.ecaa.proxy;

<<<<<<< Updated upstream
import flyingperson.ecaa.Blocks;
import flyingperson.ecaa.ECAA;
import flyingperson.ecaa.Items;
import mezz.jei.input.InputHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
=======
import flyingperson.ecaa.ModBlocks;
import flyingperson.ecaa.handler.SkyProviderHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
>>>>>>> Stashed changes
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
    	MinecraftForge.EVENT_BUS.register(new SkyProviderHandler());
        super.preInit(e);

        OBJLoader.INSTANCE.addDomain(ECAA.MODID);

    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        Blocks.initModels();
    }
    
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        ModBlocks.initModels();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
<<<<<<< Updated upstream
        Blocks.initModels();
        Items.initModels();
=======
        ModBlocks.initModels();
>>>>>>> Stashed changes
    }

}