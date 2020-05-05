package flyingperson.ecaa.handler;

import flyingperson.ecaa.worldgen.WorldProviderEcaaAsteroids;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.SkyProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkyProviderHandler {
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		WorldClient world = minecraft.world;

		if (world != null) {
			if (world.provider instanceof WorldProviderEcaaAsteroids) {
				if (world.provider.getSkyRenderer() == null) {
					world.provider
							.setSkyRenderer(new SkyProviderAsteroids((IGalacticraftWorldProvider) world.provider));
				}

				if (world.provider.getCloudRenderer() == null) {
					world.provider.setCloudRenderer(new CloudRenderer());
				}
			}
		}
	}
}
