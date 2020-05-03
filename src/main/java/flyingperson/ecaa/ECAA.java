package flyingperson.ecaa;

import flyingperson.ecaa.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ECAA.MODID, name = ECAA.NAME, version = ECAA.VERSION)
public class ECAA
{
    public static final String MODID = "ecaa";
    public static final String NAME = "Ex Cinere Adscendere Addons";
    public static final String VERSION = "1.0";

    public static Logger logger;
    @SidedProxy(clientSide = "flyingperson.ecaa.proxy.ClientProxy", serverSide = "flyingperson.ecaa.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ECAA instance;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
