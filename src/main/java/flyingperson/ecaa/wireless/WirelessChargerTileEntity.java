package flyingperson.ecaa.wireless;

import baubles.api.BaublesApi;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WirelessChargerTileEntity extends TileEntity implements ITickable, IEnergySink {

    EnergyStorage storage = new EnergyStorage(4096);

    boolean loaded = false;
    boolean active = false;
    boolean actuallyActive = false;

    public boolean getActive() {//return actuallyActive;
        return storage.getEnergyStored() > 0;
    }

    @Override
    public void update() {
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        loaded = true;
        chargeAllPlayers();
        actuallyActive = active;
    }

    private void chargeAllPlayers() {
        active = false;
        List<EntityPlayer> playerList = world.playerEntities;
        for (int i = 0; i < playerList.size(); i++) {
            InventoryPlayer inv = playerList.get(i).inventory;
            if (!playerList.get(i).isSpectator()) {
                charge(inv.armorInventory);
                charge(inv.mainInventory);
                charge(inv.offHandInventory);
                IInventory baubles = getBaubles(playerList.get(i));
                if (baubles != null) {
                    for (int j = 0; j < baubles.getSizeInventory(); j++) {
                        ItemStack item = baubles.getStackInSlot(j);
                        if (!item.isEmpty()) {
                            charge(item);
                        }
                    }
                }
            }
        }
    }

    private void charge(ItemStack toCharge) {
        IEnergyStorage chargable = getCapability(toCharge);

        if (toCharge.getItem() instanceof IElectricItem) {
            double current = ElectricItem.manager.getCharge(toCharge);
            double max = ElectricItem.manager.getMaxCharge(toCharge);
            double chargedBy;
            if (storage.getEnergyStored()/4 > max - current) {
                chargedBy = ElectricItem.manager.charge(toCharge, max-current, ElectricItem.manager.getTier(toCharge), false, false);
            } else {
                chargedBy = ElectricItem.manager.charge(toCharge, storage.getEnergyStored()/4, ElectricItem.manager.getTier(toCharge), false, false);
            }
            int extracted = storage.extractEnergy((int) chargedBy*4, false);
            active = extracted != 0;
            if (Math.abs((extracted / 4) - chargedBy) > 1) {
                System.out.println("Power dupe - "+((extracted/4) - chargedBy)*4+"RF Generated");
            }

        } else if (chargable != null && toCharge.getCount() == 1) {
            int current = chargable.getEnergyStored();
            int max = chargable.getMaxEnergyStored();
            int chargedBy;
            if (storage.getEnergyStored() > max-current) {
                chargedBy = chargable.receiveEnergy(max-current, false);
            } else {
                chargedBy = chargable.receiveEnergy(storage.getEnergyStored(), false);
            }
            int extracted = storage.extractEnergy( chargedBy, false);
            active = extracted != 0;
            if (Math.abs(extracted - chargedBy) > 1) {
                System.out.println("Power dupe - "+(extracted - chargedBy)+"RF Generated");
            }
        }
    }
    private void charge(NonNullList<ItemStack> toCharge) {
        for (int i = 0; i < toCharge.size(); i++) {
            charge(toCharge.get(i));
        }
    }

    public boolean hasBaubles() {
        return Loader.isModLoaded("baubles");
    }

    public IInventory getBaubles(EntityPlayer player) {
        return hasBaubles() ? getBaublesInvUnsafe(player) : null;
    }

    private IInventory getBaublesInvUnsafe(EntityPlayer player) {
        return BaublesApi.getBaubles(player);
    }

    @SuppressWarnings("null")
    public static @Nullable IEnergyStorage getCapability(@Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null);
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

    //IC2 EUSink

    @Override
    public int getSinkTier() {
        return 3;
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
