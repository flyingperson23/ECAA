package flyingperson.ecaa.worldgen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import net.minecraft.world.biome.Biome;


/**
 * This class is from Galacticraft Planets, required for BiomeEcaaAsteroids
 * 
 * 
 * all credit for the original class go to the Galacticraft developement team
 * 
 * Modified by: ROMVoid95
 * Created: 5/4/2020
 *
 */
public class BiomeProviderAsteroids extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeEcaaAsteroids.asteroid;
    }
}
