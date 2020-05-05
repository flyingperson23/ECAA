package flyingperson.ecaa;

import org.apache.logging.log4j.Logger;

import flyingperson.ecaa.block.gravity.ZeroGEvent;
import flyingperson.ecaa.proxy.CommonProxy;
import flyingperson.ecaa.worldgen.BiomeEcaaAsteroids;
import flyingperson.ecaa.worldgen.ChunkProviderEcaaAsteroids;
import flyingperson.ecaa.worldgen.WorldProviderEcaaAsteroids;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.TeleportTypeAsteroids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ECAA.MODID, name = ECAA.NAME, version = ECAA.VERSION, acceptedMinecraftVersions = ECAA.MCVERSION, dependencies = ECAA.DEPENDENCIES_FORGE
		+ ECAA.DEPENDENCIES_MODS)
public class ECAA {
	public static final String MODID = "ecaa";
	public static final String NAME = "Ex Cinere Adscendere Addons";
	public static final String VERSION = "1.0";

	public static final String MCVERSION = "[1.12.2]";
	public static final String DEPENDENCIES_FORGE = "required-after:forge@[14.23.5.2847,);";
	public static final String DEPENDENCIES_MODS = "required-after:galacticraftcore;required-after:galacticraftplanets;";

	public static Logger logger;
	@SidedProxy(clientSide = "flyingperson.ecaa.proxy.ClientProxy", serverSide = "flyingperson.ecaa.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static ECAA instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		Asteroids.registerData();
        MinecraftForge.EVENT_BUS.register(new ZeroGEvent());
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// DO NOT TOUCH
		AsteroidsModule.planetAsteroids
				.setDimensionInfo(ConfigManagerAsteroids.dimensionIDAsteroids, WorldProviderEcaaAsteroids.class)
				.setTierRequired(3);
		AsteroidsModule.planetAsteroids
				.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.375F, 1.375F))
				.setRelativeOrbitTime(45.0F).setPhaseShift((float) (Math.random() * (2 * Math.PI)));
		AsteroidsModule.planetAsteroids
				.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/asteroid.png"));
		AsteroidsModule.planetAsteroids.setAtmosphere(new AtmosphereInfo(false, false, false, -1.5F, 0.05F, 0.0F));
		AsteroidsModule.planetAsteroids.addChecklistKeys("equip_oxygen_suit", "craft_grapple_hook", "thermal_padding");
		GalaxyRegistry.registerPlanet(AsteroidsModule.planetAsteroids);
		GalacticraftRegistry.registerTeleportType(WorldProviderEcaaAsteroids.class, new TeleportTypeAsteroids());
		AsteroidsModule.planetAsteroids.setDimensionInfo(ConfigManagerAsteroids.dimensionIDAsteroids,
				WorldProviderEcaaAsteroids.class, true);
		AsteroidsModule.planetAsteroids.setBiomeInfo(BiomeEcaaAsteroids.asteroid);
		proxy.init(event);

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		ChunkProviderEcaaAsteroids.reset();
	}
}
