package flyingperson.ecaa.block.neutron;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Consumer;

public class NeutronInit {
    public static NeutronBlock neutron;
    public static void init() {
        neutron = registerBlock(new NeutronBlock());
        registerItemBlock(neutron);
        GameRegistry.registerTileEntity(NeutronTileEntity.class, "neutron");
    }

    public static <V extends Block> V registerBlock(V block) {
        registerImpl(block, ForgeRegistries.BLOCKS::register);
        return block;
    }

    public static <V extends Item> V registerItem(V item) {
        registerImpl(item, ForgeRegistries.ITEMS::register);
        return item;
    }

    public static <V extends IForgeRegistryEntry<V>> V registerImpl(V registryObject, Consumer<V> registerCallback) {
        registerCallback.accept(registryObject);

        if (registryObject instanceof IModelRegister) {
            Avaritia.proxy.addModelRegister((IModelRegister) registryObject);
        }

        return registryObject;
    }

    public static ItemBlock registerItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        registerItem(itemBlock.setRegistryName(block.getRegistryName()));
        return itemBlock;
    }
}
