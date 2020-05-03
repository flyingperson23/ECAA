package flyingperson.ecaa.block.gravity;


import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.item.IArmorGravity;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;


public abstract class TileEntityGravity extends TileEntity implements ITickable {

    @NetworkedField(targetSide = Side.CLIENT)
    public int processTimeRequired = 1;

    @NetworkedField(targetSide = Side.CLIENT)
    public static int processTicks = 0;

    public static HashSet<BlockVec3Dim> loadedTiles = new HashSet();

    private int radius;
    private boolean poweredByRedstone;


    private AxisAlignedBB aabb;
    private boolean initialised = false;
    public static boolean check = false;

    public boolean shouldRenderEffects = false;


    public TileEntityGravity(int radius) {
        this.radius = radius;
    }
    public TileEntityGravity(int radius, int max, int usage, String str) {
        this(radius);
    }

    @Override
    public void validate() {
        super.validate();
        if (!this.world.isRemote) this.loadedTiles.add(new BlockVec3Dim(this));
    }
    @Override
    public void onLoad() {
        if (!this.world.isRemote)
            this.loadedTiles.add(new BlockVec3Dim(this));
    }
    @Override
    public void onChunkUnload() {
        if (!this.world.isRemote)
            this.loadedTiles.remove(new BlockVec3Dim(this));

        super.onChunkUnload();
    }
    @Override
    public void invalidate() {
        if (!this.world.isRemote) {
            this.loadedTiles.remove(new BlockVec3Dim(this));
        }

        super.invalidate();
    }
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(pos, 1, nbttagcompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }
    public void updatePower() {
        poweredByRedstone = world.isBlockPowered(this.getPos());
    }
    @Override
    public void update() {
        updatePower();
        doStuff();
    }
    public void doStuff() {
        if (this.world.rand.nextInt(4) == 0)
            this.world.notifyLightSet(this.getPos());
        if (this.processTicks == 0) {
            this.processTicks = this.processTimeRequired;
        } else {
            if (--this.processTicks <= 0) {
                this.smeltItem();
                this.processTicks = this.processTimeRequired;
            }
        }
    }

    public static boolean canProcess() {
        return true;
    }
    public void smeltItem() {
        aabb = new AxisAlignedBB(this.getPos().getX() - getGravityRadius(), this.getPos().getY() - 4,
                this.getPos().getZ() - getGravityRadius(),

                this.getPos().getX() + getGravityRadius(), this.getPos().getY() + 16,
                this.getPos().getZ() + getGravityRadius());

        if (this.world.provider instanceof IGalacticraftWorldProvider) {
            final double g;
            if (this.world.provider instanceof WorldProviderSpaceStation)
                g = 0.80665D;
            else
                g = (1.0 - ((IGalacticraftWorldProvider) world.provider).getGravity()) / 0.08F;
            final List list = world.getEntitiesWithinAABB(Entity.class, aabb);
            if(!world.isRemote) {
                for (Object e : list) {
                    if(e instanceof IAntiGrav) continue;
                    Entity entity = (Entity) e;
                    entity.fallDistance -= g * 10.0F;
                    if(e instanceof EntityLivingBase && !(e instanceof EntityPlayer))
                        ((EntityLivingBase)e).motionY -= (g / 200);
                    if(entity.fallDistance < 0) {
                        entity.fallDistance = 0.0F;
                    }
                }
            }
            else {
                for(Object e: list) {
                    if(e instanceof EntityLivingBase) {
                        EntityLivingBase living = (EntityLivingBase)e;
                        if(e instanceof EntityPlayer) {
                            EntityPlayer p = (EntityPlayer)living;
                            if (p.capabilities.isFlying)
                                continue;
                            if (!p.inventory.armorItemInSlot(0).isEmpty()
                                    && p.inventory.armorItemInSlot(0).getItem() instanceof IArmorGravity
                                    && ((IArmorGravity) p.inventory.armorItemInSlot(0).getItem())
                                    .gravityOverrideIfLow(p) > 0)
                                continue;
                        }
                        living.motionY -= (g / 200);
                    }
                }
            }
            check = true;
            if (this.shouldRenderEffects && world.isRemote) {
                for (int yy = -4; yy < 16; yy++) {
                    for (int ix = -getGravityRadius(); ix <= getGravityRadius() + 1; ix++) {
                        if(ix == -getGravityRadius() || ix == getGravityRadius() + 1 || yy == 15 || yy == -4) {
                            world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.getPos().getX() + ix + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getY() + yy + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getZ() - getGravityRadius() + this.world.rand.nextFloat() - 0.5F, 0.0D, 0.0D,
                                    0.0D);
                            world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.getPos().getX() + ix + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getY() + yy + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getZ() + getGravityRadius() + 1 + this.world.rand.nextFloat() - 0.5F, 0.0D, 0.0D,
                                    0.0D);
                        }
                    }
                    for (int iz = -getGravityRadius(); iz <= getGravityRadius() + 1; iz++) {
                        if(iz == -getGravityRadius() || iz == getGravityRadius() + 1 || yy == 15 || yy == -4) {
                            world.spawnParticle(EnumParticleTypes.CRIT_MAGIC,
                                    this.getPos().getX() - getGravityRadius() + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getY() + yy + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getZ() + iz + this.world.rand.nextFloat() - 0.5F, 0.0D, 0.0D, 0.0D);
                            world.spawnParticle(EnumParticleTypes.CRIT_MAGIC,
                                    this.getPos().getX() + getGravityRadius() + 1 + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getY() + yy + this.world.rand.nextFloat() - 0.5F,
                                    this.getPos().getZ() + iz + this.world.rand.nextFloat() - 0.5F, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.initialised = true;
        this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
        if(par1NBTTagCompound.hasKey("gravityradius")) {
            int grav = par1NBTTagCompound.getInteger("gravityradius");
            this.setGravityRadius(grav == 0 ? 1 : grav);
        }
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
        par1NBTTagCompound.setInteger("gravityradius", radius > 16 ? 16 : radius);
        return par1NBTTagCompound;
    }
    public void setGravityRadius(int radius) {
        this.radius = radius;
    }
    public int getGravityRadius() {
        return this.radius;
    }
    public boolean inGravityZone(World world, EntityPlayer player) {
        if(player.posX > this.pos.getX() - getGravityRadius() &&
                player.posY > this.pos.getY() - 4 &&
                player.posZ > this.pos.getZ() - getGravityRadius() &&

                player.posX < this.pos.getX() + getGravityRadius() &&
                player.posY < this.pos.getY() + 16 &&
                player.posZ < this.pos.getZ() + getGravityRadius())
            return true;
        return false;
    }
    public int[] getSlotsForFace(EnumFacing side) {
        return null;
    }
}
