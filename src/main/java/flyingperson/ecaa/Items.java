package flyingperson.ecaa;

import flyingperson.ecaa.item.DustAsteroid;
import flyingperson.ecaa.item.DustMars;
import flyingperson.ecaa.item.DustMoon;
import flyingperson.ecaa.item.DustVenus;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Items {
    @GameRegistry.ObjectHolder("ecaa:dustmoon")
    public static DustMoon dustMoon;

    @GameRegistry.ObjectHolder("ecaa:dustmars")
    public static DustMars dustMars;

    @GameRegistry.ObjectHolder("ecaa:dustvenus")
    public static DustVenus dustVenus;

    @GameRegistry.ObjectHolder("ecaa:dustasteroid")
    public static DustAsteroid dustAsteroid;


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        dustAsteroid.initModel();
        dustMars.initModel();
        dustVenus.initModel();
        dustMoon.initModel();
    }

}
