package flyingperson.ecaa.block.gravity;

import flyingperson.ecaa.ECAA;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import net.java.games.input.Component.Identifier.Axis;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ZeroGEvent {
	
	// THIS IS A SIMPLE TEST
	
	// this just checks if these even do anything or not
	// They kind of do, adding a Gravity Block does do some
	// interesting things to say the least
	
	@SubscribeEvent
	public void ZeroGravityEvent(ZeroGravityEvent event) {
		EntityLivingBase e = event.getEntityLiving();
		if (e instanceof EntityPlayer) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void ZeroGravityEvent(ZeroGravityEvent.InFreefall event) {
		EntityLivingBase e = event.getEntityLiving();
		if (e instanceof EntityPlayer) {
			event.setCanceled(true);
		}
	}
}
