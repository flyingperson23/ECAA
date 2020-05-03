package flyingperson.ecaa.block.accelerator;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.ArrayList;

public abstract class AcceleratorTileEntity extends TileEntity implements ITickable, IEnergySink {

    private boolean loaded = false;
    private boolean poweredByRedstone = false;
    private ArrayList<BlockPos> toTick = new ArrayList<BlockPos>();

    private boolean active = false;

    private int range;
    private int speed;

    private int counter = 0;

    EnergyStorage storage;
    public int capacity;
    public int usage;

    public ArrayList<BlockPos> getToTick() {
        return toTick;
    }

    public int getRange() {return range;}
    public int getSpeed() {return speed;}
    public boolean getActive() {return active;}

    public AcceleratorTileEntity(int range, int speed, int capacity, int usage) {
        this.range = range;
        this.speed = speed;
        storage = new EnergyStorage(capacity);
        this.capacity = capacity;
        this.usage = usage;

    }

    public AcceleratorTileEntity() {
        this.range = range;
        this.speed = speed;
        storage = new EnergyStorage(capacity);
        this.capacity = capacity;
        this.usage = usage;
    }

    private void updateList() {
        toTick = new ArrayList<BlockPos>();
        for (int x = -1 * range; x < range; x++) {
            for (int y = -1 * range; y < range; y++) {
                for (int z = -1 * range; z < range; z++) {
                    BlockPos pos = new BlockPos(this.getPos().add(x, y, z));
                    if (isTickable(pos)) {
                        toTick.add(pos);
                    }
                }
            }
        }
    }

    public void updatePower() {
        poweredByRedstone = world.isBlockPowered(this.getPos());
    }

    @Override
    public void update() {
        if (!loaded) MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        loaded = true;
        
        if (storage.getEnergyStored() >= usage) {
            tickBlocksAndCount();
        } else {
            active = false;
        }
    }
    private void tickBlocksAndCount() {
        updatePower();
        if (!this.world.isRemote) {
            if (!this.poweredByRedstone) {
                storage.extractEnergy(usage, false);
                this.tickBlocks();
                active = true;
            } else {
                active = false;
            }
        }
        if (counter > 18) {
            counter = 0;
            updateList();
        } else {
            counter++;
        }
    }
    private void tickBlocks() {
        for (int i = 0; i < toTick.size(); i++) {
            if (isTickable(toTick.get(i))) {
                TileEntity tile = world.getTileEntity(toTick.get(i));
                for (int j = 0; j < speed; j++) {
                    ((ITickable) tile).update();
                }
            }
        }
    }
    private boolean isTickable(BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block != null && !(block instanceof BlockFluidBase)) {
            if (world.getBlockState(pos).getBlock().hasTileEntity()) {
                TileEntity tile = this.world.getTileEntity(pos);
                if (tile == null || tile.isInvalid() || tile instanceof AcceleratorTileEntity) {
                    return false;
                } else if (!tile.isInvalid() && tile instanceof ITickable) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capObject, EnumFacing side) {
        if (capObject == CapabilityEnergy.ENERGY) {
            return true;
        } else {
            return super.hasCapability(capObject, side);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capObject, EnumFacing side) {
        if (capObject == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(storage);
        } else {
            return super.getCapability(capObject, side);
        }
    }

    //EU Compat
    @Override
    public int getSinkTier() {
        return 4;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return true;
    }


    @Override
    public void invalidate() {
        this.tileEntityInvalid = true;
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public void onChunkUnload() {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public double getDemandedEnergy() {
        double demand = storage.getMaxEnergyStored() - storage.getEnergyStored();
        if (demand > 0) {
            return Math.min(demand, 512);
        }
        return 0;
    }

    @Override
    public double injectEnergy(EnumFacing enumFacing, double amount, double tier) {
        double ratio = 4;
        return amount - (this.storage.receiveEnergy((int) (amount*ratio), false) / ratio);
    }

}
