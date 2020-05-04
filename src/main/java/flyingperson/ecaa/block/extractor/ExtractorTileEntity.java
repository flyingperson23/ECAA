package flyingperson.ecaa.block.extractor;


import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.BlockUtils;
import codechicken.lib.util.ItemUtils;
import flyingperson.ecaa.Items;
import flyingperson.ecaa.block.neutron.NeutronBase2;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class ExtractorTileEntity extends NeutronBase2 implements IInventory, IEnergySink {

    public static final int POWER_REQUIRED = 10000;
    private boolean loaded = false;
    EnergyStorage storage;

    private ItemStack items = ItemStack.EMPTY;
    private int progress;

    public ExtractorTileEntity() {
        super();
        storage = new EnergyStorage(10000);
    }
    public String getPlanet() {
        if (world.provider.getDimension() == -31) {
            return("venus");
        } else if (world.provider.getDimension() == -29) {
            return("mars");
        } else if (world.provider.getDimension() == -30) {
            return("asteroid");
        } else if (world.provider.getDimension() == -28) {
            return("moon");
        } else {
            return ("none");
        }
    }
    public ItemStack getCollected() {
        if (getPlanet().equals("venus")) {
            return new ItemStack(Items.dustVenus);
        } else if (getPlanet().equals("mars")) {
            return new ItemStack(Items.dustMars);
        } else if (getPlanet().equals("asteroid")) {
            return new ItemStack(Items.dustAsteroid);
        } else if (getPlanet().equals("moon")) {
            return new ItemStack(Items.dustMoon);
        } else {
            return new ItemStack(Blocks.BARRIER);
        }
    }
    public String getName2() {
        if (getPlanet().equals("venus")) {
            return "Venus Dust";
        } else if (getPlanet().equals("mars")) {
            return "Mars Dust";
        } else if (getPlanet().equals("asteroid")) {
            return "Asteroid Dust";
        } else if (getPlanet().equals("moon")) {
            return "Moon Dust";
        } else {
            return "None";
        }
    }
    @Override
    public void doWork() {
        if (!loaded) MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        loaded = true;
        if (getCollectedItem2() != null) doWork2();
    }
    public ItemStack getCollectedItem2() {
        String dim = getPlanet();
        ItemStack next;
        if (dim.equals("venus")) {
            next = new ItemStack(Items.dustVenus);
        } else if (dim.equals("mars")) {
            next = new ItemStack(Items.dustMars);
        } else if (dim.equals("asteroid")) {
            next = new ItemStack(Items.dustAsteroid);
        } else if (dim.equals("moon")) {
            next = new ItemStack(Items.dustMoon);
        } else {
            next = null;
        }
        return next;
    }
    public void doWork2() {
        progress += storage.extractEnergy(1000, false);
        if (progress >= POWER_REQUIRED) {
            ItemStack next = getCollectedItem2();

            if (next != null) {
                if (items.isEmpty()) {
                    items = ItemUtils.copyStack(next, next.getCount());
                } else if (items.getItem().equals(next.getItem())) {
                    items.grow(1);
                }
            }
            progress = 0;
            markDirty();
        }
    }

    @Override
    protected void onWorkStopped() {
        progress = 0;
    }

    @Override
    protected boolean canWork() {
        return (items.isEmpty() || items.getCount() < 64) && getCollectedItem2() != null;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Items")) {
            items = new ItemStack(tag.getCompoundTag("Items"));
        }
        progress = tag.getInteger("Progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Progress", progress);
        if (items != null) {
            NBTTagCompound produce = new NBTTagCompound();
            items.writeToNBT(produce);
            tag.setTag("Items", produce);
        } else {
            tag.removeTag("Items");
        }
        return super.writeToNBT(tag);
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(progress);
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        progress = packet.readVarInt();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        } else if (capability == CapabilityEnergy.ENERGY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new InvWrapper(this));
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(storage);
        } else {
            return super.getCapability(capability, side);
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            if (decrement < items.getCount()) {
                ItemStack take = items.splitStack(decrement);
                if (items.getCount() <= 0) {
                    items = ItemStack.EMPTY;
                }
                return take;
            } else {
                ItemStack take = items;
                items = ItemStack.EMPTY;
                return take;
            }
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(getPos()) == this && BlockUtils.isEntityInRange(getPos(), player, 64);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        items = stack;
    }

    @Override
    public String getName() {
        return "container.extractor";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
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