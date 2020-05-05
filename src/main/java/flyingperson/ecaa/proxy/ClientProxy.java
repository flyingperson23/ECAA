package flyingperson.ecaa.proxy;

import flyingperson.ecaa.ECAA;
import flyingperson.ecaa.Items;
import flyingperson.ecaa.Blocks;
import flyingperson.ecaa.block.neutron.NeutronInit;
import flyingperson.ecaa.handler.SkyProviderHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
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
		//Blocks.initModels();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		Blocks.initModels();
		Items.initModels();
		NeutronInit.initModel();
	}

}